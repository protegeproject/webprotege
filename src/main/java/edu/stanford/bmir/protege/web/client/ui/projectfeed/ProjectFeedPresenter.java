package edu.stanford.bmir.protege.web.client.ui.projectfeed;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class ProjectFeedPresenter {

    private final ProjectFeedView view;

    private final Provider<ProjectChangeEventPanel> projectChangeEventViewProvider;

    private final Provider<NotePostedEventPanel> notePostedEventViewProvider;

    private final Provider<UserStartedViewingProjectEventPanel> userStartedViewingProjectEventViewProvider;

    private final Provider<UserStoppedViewingProjectEventPanel> userStoppedViewingProjectEventViewProvider;

    private final Set<NoteId> noteIds = new HashSet<>();

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);


    @Inject
    public ProjectFeedPresenter(ProjectFeedView view,
                                Provider<ProjectChangeEventPanel> projectChangeEventViewProvider,
                                Provider<NotePostedEventPanel> notePostedEventViewProvider,
                                Provider<UserStartedViewingProjectEventPanel> userStartedViewingProjectEventViewProvider,
                                Provider<UserStoppedViewingProjectEventPanel> userStoppedViewingProjectEventViewProvider) {
        this.view = view;
        this.projectChangeEventViewProvider = projectChangeEventViewProvider;
        this.notePostedEventViewProvider = notePostedEventViewProvider;
        this.userStartedViewingProjectEventViewProvider = userStartedViewingProjectEventViewProvider;
        this.userStoppedViewingProjectEventViewProvider = userStoppedViewingProjectEventViewProvider;
    }

    public void bind(HasEventHandlerManagement eventHandlerMan) {
        eventHandlerMan.addProjectEventHandler(ProjectChangedEvent.TYPE, event -> postChangeEvent(event));
        eventHandlerMan.addProjectEventHandler(NotePostedEvent.TYPE, event -> postNotePostedEvent(event));
        eventHandlerMan.addProjectEventHandler(UserStartingViewingProjectEvent.TYPE, event -> postUserStartedViewingProjectEvent(event));
        eventHandlerMan.addProjectEventHandler(UserStoppedViewingProjectEvent.TYPE, event -> postUserStoppedViewingProjectEvent(event));
    }

    public void postUserStartedViewingProjectEvent(UserStartingViewingProjectEvent event) {
        UserStartedViewingProjectEventPanel panel = userStartedViewingProjectEventViewProvider.get();
        panel.setUserId(event.getUserId());
        panel.setTimestamp(new Date().getTime());
        view.insertWidgetIntoFeed(panel);
    }

    public void postUserStoppedViewingProjectEvent(UserStoppedViewingProjectEvent event) {
        UserStoppedViewingProjectEventPanel panel = userStoppedViewingProjectEventViewProvider.get();
        panel.setUserId(event.getUserId());
        panel.setTimestamp(new Date().getTime());
        view.insertWidgetIntoFeed(panel);
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

    public void postChangeEvent(ProjectChangedEvent event) {
        if(event.getRevisionNumber().getValue() <= lastRevisionNumber.getValue()) {
            return;
        }
        lastRevisionNumber = event.getRevisionNumber();
        final ProjectChangeEventPanel changePanel = projectChangeEventViewProvider.get();
        changePanel.setUserName(event.getUserId().getUserName());
        changePanel.setTimestamp(event.getTimestamp());
        changePanel.setChangedEntities(event.getSubjects());
        changePanel.setDescription(event.getRevisionSummary().getDescription());
        view.insertWidgetIntoFeed(changePanel);
    }

    public void postNotePostedEvent(NotePostedEvent event) {
        final NoteId noteId = event.getNoteDetails().getNoteHeader().getNoteId();
        if(noteIds.contains(noteId)) {
            return;
        }
        noteIds.add(noteId);
        final NotePostedEventPanel notePostedEventPanel = notePostedEventViewProvider.get();
        notePostedEventPanel.setValue(event);
        view.insertWidgetIntoFeed(notePostedEventPanel);

    }

}
