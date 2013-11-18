package edu.stanford.bmir.protege.web.shared.events;

import edu.stanford.bmir.protege.web.shared.event.SerializableEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 * <p>
 *     Represents a list of {@link SerializableEvent}s between to points denoted by {@link EventTag}s.
 * </p>
 */
public class EventList<E extends SerializableEvent<?>> implements Serializable {

    private EventTag startTag;

    private EventTag endTag;

    private List<E> events;


    /**
     * For serialization only
     */
    private EventList() {
    }

    public EventList(EventTag startTag, EventTag endTag) {
        this.startTag = checkNotNull(startTag);
        this.endTag = checkNotNull(endTag);
        this.events = null;
    }

    public EventList(EventTag startTag, Collection<E> events, EventTag endTag) {
        this.startTag = checkNotNull(startTag);
        this.endTag = checkNotNull(endTag);
        this.events = new ArrayList<E>(checkNotNull(events));
    }

    public int size() {
        if(events == null) {
            return 0;
        }
        else {
            return events.size();
        }
    }

    public boolean isEmpty() {
        return events == null || events.size() == 0;
    }

    public EventTag getStartTag() {
        return startTag;
    }

    public EventTag getEndTag() {
        return endTag;
    }

    public List<E> getEvents() {
        if (events == null) {
            return Collections.emptyList();
        }
        else {
            return Collections.unmodifiableList(events);
        }
    }
}
