package edu.stanford.bmir.protege.web.shared.dispatch;

import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.SerializableEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public abstract class AbstractHasEventListResult<E extends SerializableEvent<?>> implements Result, HasEventList<E> {

    private EventList<E> eventList;

    /**
     * For serialization purposes only!
     */
    protected AbstractHasEventListResult() {
    }

    public AbstractHasEventListResult(EventList<E> eventList) {
        this.eventList = eventList;
    }

    final public EventList<E> getEventList() {
        return eventList;
    }
}
