package edu.stanford.bmir.protege.web.client.events;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2013
 */
public interface UserLoggedInHandler {

    void handleUserLoggedIn(UserLoggedInEvent event);
}
