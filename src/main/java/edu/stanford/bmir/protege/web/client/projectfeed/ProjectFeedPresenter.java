package edu.stanford.bmir.protege.web.client.projectfeed;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
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

    private final Provider<ProjectChangeEventView> projectChangeEventViewProvider;

    private final Provider<NotePostedEventView> notePostedEventViewProvider;

    private final Provider<UserStartedViewingProjectEventView> userStartedViewingProjectEventViewProvider;

    private final Provider<UserStoppedViewingProjectEventView> userStoppedViewingProjectEventViewProvider;

    private final Set<NoteId> noteIds = new HashSet<>();

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);


    @Inject
    public ProjectFeedPresenter(ProjectFeedView view,
                                Provider<ProjectChangeEventView> projectChangeEventViewProvider,
                                Provider<NotePostedEventView> notePostedEventViewProvider,
                                Provider<UserStartedViewingProjectEventView> userStartedViewingProjectEventViewProvider,
                                Provider<UserStoppedViewingProjectEventView> userStoppedViewingProjectEventViewProvider) {
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
        final ProjectChangeEventView eventView = projectChangeEventViewProvider.get();
        eventView.setUserName(event.getUserId().getUserName());
        eventView.setTimestamp(event.getTimestamp());
        eventView.setChangedEntities(event.getSubjects());
        eventView.setDescription(event.getRevisionSummary().getDescription());
        view.insertWidgetIntoFeed(eventView);
    }

    public void postNotePostedEvent(NotePostedEvent event) {
        final NoteId noteId = event.getNoteDetails().getNoteHeader().getNoteId();
        if(noteIds.contains(noteId)) {
            return;
        }
        noteIds.add(noteId);
        final NotePostedEventView eventView = notePostedEventViewProvider.get();
        eventView.setValue(event);
        view.insertWidgetIntoFeed(eventView);
    }


    public void postUserStartedViewingProjectEvent(UserStartingViewingProjectEvent event) {
        UserStartedViewingProjectEventView eventView = userStartedViewingProjectEventViewProvider.get();
        eventView.setUserId(event.getUserId());
        eventView.setTimestamp(new Date().getTime());
        view.insertWidgetIntoFeed(eventView);
    }

    public void postUserStoppedViewingProjectEvent(UserStoppedViewingProjectEvent event) {
        UserStoppedViewingProjectEventView eventView = userStoppedViewingProjectEventViewProvider.get();
        eventView.setUserId(event.getUserId());
        eventView.setTimestamp(new Date().getTime());
        view.insertWidgetIntoFeed(eventView);
    }


}
