package edu.stanford.bmir.protege.web.client.project;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/04/2013
 */
public interface ActiveProjectChangedHandler {

    void handleActiveProjectChanged(ActiveProjectChangedEvent event);
}
