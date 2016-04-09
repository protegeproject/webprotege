package edu.stanford.bmir.protege.web.client.ui.projectfeed;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class ProjectFeedPresenter {

    private final ProjectFeedView view;

    @Inject
    public ProjectFeedPresenter(final ProjectFeedView view) {
        this.view = view;

    }

    public void bind(HasEventHandlerManagement eventHandlerMan) {
        eventHandlerMan.addProjectEventHandler(ProjectChangedEvent.TYPE, event -> {
            view.postChangeEvent(event);
        });
        eventHandlerMan.addProjectEventHandler(NotePostedEvent.TYPE, event -> {
            view.postNotePostedEvent(event);
        });
        eventHandlerMan.addProjectEventHandler(UserStartingViewingProjectEvent.TYPE, event -> {
            view.postUserStartedViewingProjectEvent(event);
        });
        eventHandlerMan.addProjectEventHandler(UserStoppedViewingProjectEvent.TYPE, event -> {
            view.postUserStoppedViewingProjectEvent(event);
        });
    }

    public IsWidget getView() {
        return view;
    }

    public void setUserActivityVisible(UserId userId, boolean visible) {
        view.setUserActivityVisible(userId, visible);
    }

    public void setOntologyChangesVisible(boolean visible) {
        view.setOntologyChangesVisible(visible);
    }
}
