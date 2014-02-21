package edu.stanford.bmir.protege.web.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2013
 */
public interface UserLoggedOutHandler extends EventHandler {

    void handleUserLoggedOut(UserLoggedOutEvent event);
}
