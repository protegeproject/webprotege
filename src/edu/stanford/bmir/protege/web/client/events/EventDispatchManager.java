package edu.stanford.bmir.protege.web.client.events;

import com.google.web.bindery.event.shared.Event;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/03/2013
 */
public interface EventDispatchManager {

    void dispatchEvents(List<Event<?>> eventList);
}
