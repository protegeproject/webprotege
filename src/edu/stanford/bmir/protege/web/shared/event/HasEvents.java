package edu.stanford.bmir.protege.web.shared.event;

import com.google.web.bindery.event.shared.Event;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/03/2013
 */
public interface HasEvents {

    List<Event<?>> getEvents();
}
