package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;
import edu.stanford.bmir.protege.web.shared.events.EventTag;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/05/2013
 * <p>
 *     An interface to an object which can be used to post events.
 * </p>
 */
public interface HasPostEvents<E extends WebProtegeEvent<?>> {

    /**
     * Posts an event to this event manager.
     * @param event The event to be posted.  Not {@code null}.
     * @return The tag after posting the events.
     * @throws NullPointerException if {@code event} is {@code null}.
     */
    EventTag postEvent(E event);

    /**
     * Posts a list of events to this event manager.
     * @param events The list of events to be posted.  Not {@code null}.
     * @return The tag after posting the events.
     * @throws NullPointerException if {@code events} is {@code null}.
     */
    EventTag postEvents(List<E> events);
}
