package edu.stanford.bmir.protege.web.client.ui.projectfeed;

import com.google.gwt.user.client.ui.Composite;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class ProjectFeedBasePanel extends Composite {

    private ProjectFeedPanel eventPanel;

    public ProjectFeedBasePanel(final ProjectId projectId, HasEventHandlerManagement eventHandlerMan) {
        eventPanel = new ProjectFeedPanel(projectId);
        initWidget(eventPanel);
        eventHandlerMan.addProjectEventHandler(ProjectChangedEvent.TYPE, new ProjectChangedHandler() {
            @Override
            public void handleProjectChanged(ProjectChangedEvent event) {
                if (event.getProjectId().equals(projectId)) {
                    eventPanel.postChangeEvent(event);
                }
            }
        });
        eventHandlerMan.addProjectEventHandler(NotePostedEvent.TYPE, new NotePostedHandler() {
            @Override
            public void handleNotePosted(NotePostedEvent event) {
                if (event.getProjectId().equals(projectId)) {
                    eventPanel.postNotePostedEvent(event);
                }
            }
        });
        eventHandlerMan.addProjectEventHandler(UserStartingViewingProjectEvent.TYPE, new UserStartedViewingProjectHandler() {
            @Override
            public void handleUserStartedViewingProject(UserStartingViewingProjectEvent event) {
                if (event.getProjectId().equals(projectId)) {
                    eventPanel.postUserStartedViewingProjectEvent(event);
                }
            }
        });
        eventHandlerMan.addProjectEventHandler(UserStoppedViewingProjectEvent.TYPE, new UserStoppedViewingProjectHandler() {
            @Override
            public void handleUserStoppedViewingProject(UserStoppedViewingProjectEvent event) {
                if (event.getProjectId().equals(projectId)) {
                    eventPanel.postUserStoppedViewingProjectEvent(event);
                }
            }
        });
    }
}
