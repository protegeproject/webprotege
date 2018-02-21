package edu.stanford.bmir.protege.web.shared.event;

import com.google.web.bindery.event.shared.Event;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public interface HasEventHandlerManagement {

    /**
     * Adds an event handler to this handler manager.  When the handler manager is destroyed the event handler will
     * authomatically be removed.
     * @param type The type of event.  Not {@code null}.
     * @param handler The handler for the event. Not {@code null}.
     * @param <T> The event type
     * @throws NullPointerException if any parameter is {@code null}.
     */
    <T> void addProjectEventHandler(Event.Type<T> type, T handler);

    /**
     * Adds an event handler to this handler manager.  When the handler manager is destroyed the event handler will
     * authomatically be removed.
     * @param type The type of event.  Not {@code null}.
     * @param handler The handler for the event. Not {@code null}.
     * @param <T> The event type
     * @throws NullPointerException if any parameter is {@code null}.
     */
    <T> void addApplicationEventHandler(Event.Type<T> type, T handler);


}
