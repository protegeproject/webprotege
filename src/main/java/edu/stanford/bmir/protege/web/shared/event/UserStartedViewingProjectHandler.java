package edu.stanford.bmir.protege.web.shared.event;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/03/2013
 */
public interface UserStartedViewingProjectHandler {

    void handleUserStartedViewingProject(UserStartingViewingProjectEvent event);
}
