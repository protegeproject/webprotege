package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public interface ProjectCreatedHandler extends EventHandler {

    void handleProjectCreated(ProjectCreatedEvent event);
}
