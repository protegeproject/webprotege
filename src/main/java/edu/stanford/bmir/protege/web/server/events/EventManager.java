package edu.stanford.bmir.protege.web.server.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.SerializableEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class EventManager<E extends SerializableEvent<?>> implements HasDispose, HasPostEvents<E> {


    private static final int EVENT_LIST_SIZE_LIMIT = 200;

    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();

    private final Lock READ_LOCK = LOCK.readLock();

    private final Lock WRITE_LOCK = LOCK.writeLock();



    private final Queue<EventBucket> eventQueue = new LinkedList<EventBucket>();

    private final EventLifeTime eventLifeTime;

    private EventBus eventBus = new SimpleEventBus();


    private EventTag currentTag = EventTag.getFirst();

    private final Timer purgeSweepTimer;

    private List<HandlerRegistration> registeredHandlers = new ArrayList<HandlerRegistration>();


    @Inject
    public EventManager(EventLifeTime eventLifeTime) {
        this.eventLifeTime = checkNotNull(eventLifeTime);
        purgeSweepTimer = new Timer(true);
        final long eventLifeTimeInMilliseconds = eventLifeTime.getEventLifeTimeInMilliseconds();
        purgeSweepTimer.scheduleAtFixedRate(new PurgeExpiredEventsTask(), eventLifeTimeInMilliseconds, eventLifeTimeInMilliseconds);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Posts an event to this event manager.
     * @param event The event to be posted.  Not {@code null}.
     * @return The tag after posting the events.
     * @throws NullPointerException if {@code event} is {@code null}.
     */
    public EventTag postEvent(E event) {
        final List<E> events = new ArrayList<E>(1);
        events.add(checkNotNull(event, "event must not be null"));
        return postEvents(events);
    }

    /**
     * Posts a list of events to this event manager.
     * @param events The list of events to be posted.  Not {@code null}.
     * @return The tag after posting the events.
     * @throws NullPointerException if {@code events} is {@code null}.
     */
    public EventTag postEvents(List<E> events) {
        if(events.size() > EVENT_LIST_SIZE_LIMIT) {
            // Just don't bother
            return currentTag;
        }
        try {
            WRITE_LOCK.lock();
            currentTag = currentTag.next();
            EventBucket e = new EventBucket(System.currentTimeMillis(), checkNotNull(events, "events must not be null"), currentTag);
            eventQueue.add(e);
        }
        finally {
            WRITE_LOCK.unlock();
        }
        for(E event : new LinkedHashSet<E>(events)) {
            eventBus.fireEvent(event);
        }
        return currentTag;
    }

    /**
     * Gets the live events posted to this manager which have a tag greater or equal to the specified tag.  Events are coalesced
     * where possible.  That is, if event E1 is posted at time t1 and event E2 is posted at time t2 and E1 and E2 are
     * equal then a list containing only E2 will be returned.
     * @param fromTag The tag that denotes the point after which events will be retrieved.  Not {@code null}.
     * @return The list of live events that happened since the specified tag.  Not {@code null}.
     * @throws NullPointerException if {@code tag} is {@code null}.
     */
    public EventList<E> getEventsFromTag(EventTag fromTag) {
        checkNotNull(fromTag, "tag must not be null");
        List<E> resultList = new ArrayList<E>();
        final EventTag curTag;
        try {
            READ_LOCK.lock();
            curTag = currentTag;
            for (EventBucket bucket : eventQueue) {
                if(bucket.getTag().isGreaterOrEqualTo(fromTag)) {
                    resultList.addAll(bucket.getEvents());
                }
            }
        }
        finally {
            READ_LOCK.unlock();
        }
        final EventTag toTag = curTag.next();
        if(resultList.isEmpty()) {
            return new EventList<E>(fromTag, toTag);
        }
        // Prune duplicates
        LinkedHashSet<E> events = new LinkedHashSet<E>(resultList);
        return new EventList<E>(fromTag, events, toTag);
    }

    public EventTag getCurrentTag() {
        try {
            READ_LOCK.lock();
            return currentTag;
        }
        finally {
            READ_LOCK.unlock();
        }
    }


    public <T extends EventHandler> HandlerRegistration addHandler(Event.Type<T> type, T handler) {
        final HandlerRegistration handlerRegistration = eventBus.addHandler(type, handler);
        registeredHandlers.add(handlerRegistration);
        return handlerRegistration;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Instances of this class bind together a timestamp, event list and event list tag.
     */
    private class EventBucket {

        private final long timestamp;

        private final List<E> events;

        private final EventTag tag;

        /**
         * Constructs an EventBucket.
         * @param timestamp The timestamp of the bucket
         * @param events The list of events in the bucket. Not {@code null}.  A copy of the specified list will be taken.
         * @param tag The tag of the bucket.  Not {@code null}.
         * @throws NullPointerException if any parameters are {@code null}.
         */
        private EventBucket(long timestamp, List<E> events, EventTag tag) {
            this.timestamp = timestamp;
            this.events = new ArrayList<E>(checkNotNull(events));
            this.tag = checkNotNull(tag);
        }

        /**
         * Gets the timestamp of this bucket
         * @return The timestamp of this bucket
         */
        public long getTimestamp() {
            return timestamp;
        }

        /**
         * Gets the events in this bucket.
         * @return A {@link List} of events in this bucket. Not {@code null}.
         */
        public List<E> getEvents() {
            return events;
        }

        /**
         * Gets the {@link edu.stanford.bmir.protege.web.shared.events.EventTag} for this bucket.
         * @return The {@link edu.stanford.bmir.protege.web.shared.events.EventTag} of this bucket.  Not {@code null}.
         */
        public EventTag getTag() {
            return tag;
        }

        public boolean isExpired() {
            final long elapsedTime = System.currentTimeMillis() - timestamp;
            return elapsedTime > eventLifeTime.getEventLifeTimeInMilliseconds();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("EventManager$EventListBucket");
            sb.append("(");
            sb.append(tag);
            sb.append(" ");
            sb.append(timestamp);
            sb.append(" ");
            sb.append(events);
            sb.append(")");
            return sb.toString();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void purgeExpiredEvents() {
        try {
            WRITE_LOCK.lock();
            while (true) {
                if (eventQueue.isEmpty()) {
                    break;
                }
                EventBucket bucket = eventQueue.peek();
                if (bucket.isExpired()) {
                    eventQueue.poll();
                }
                else {
                    break;
                }
            }
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class PurgeExpiredEventsTask extends TimerTask {

        @Override
        public void run() {
            purgeExpiredEvents();
        }
    }

    @Override
    public void dispose() {
        purgeSweepTimer.cancel();
        removeRegisteredHandlersFromEventBus();
    }

    private void removeRegisteredHandlersFromEventBus() {
        for(HandlerRegistration handlerRegistration : registeredHandlers) {
            handlerRegistration.removeHandler();
        }
    }
}
