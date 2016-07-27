package edu.stanford.bmir.protege.web.shared.dispatch;

import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public abstract class AbstractHasEventListResult<E extends WebProtegeEvent<?>> implements Result, HasEventList<E> {

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
