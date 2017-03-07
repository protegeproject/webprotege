package edu.stanford.bmir.protege.web.client.projectfeed;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.issues.CommentId;
import edu.stanford.bmir.protege.web.shared.issues.CommentPostedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.issues.CommentPostedEvent.ON_COMMENT_POSTED;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class ProjectFeedPresenter {

    private final ProjectId projectId;

    private final ProjectFeedView view;

    private final Provider<ProjectChangeEventView> projectChangeEventViewProvider;

    private final Provider<CommentPostedEventView> notePostedEventViewProvider;

    private final Provider<UserStartedViewingProjectEventView> userStartedViewingProjectEventViewProvider;

    private final Provider<UserStoppedViewingProjectEventView> userStoppedViewingProjectEventViewProvider;

    private final Set<CommentId> commentIds = new HashSet<>();

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);


    @Inject
    public ProjectFeedPresenter(ProjectId projectId,
                                ProjectFeedView view,
                                Provider<ProjectChangeEventView> projectChangeEventViewProvider,
                                Provider<CommentPostedEventView> notePostedEventViewProvider,
                                Provider<UserStartedViewingProjectEventView> userStartedViewingProjectEventViewProvider,
                                Provider<UserStoppedViewingProjectEventView> userStoppedViewingProjectEventViewProvider) {
        this.projectId = projectId;
        this.view = view;
        this.projectChangeEventViewProvider = projectChangeEventViewProvider;
        this.notePostedEventViewProvider = notePostedEventViewProvider;
        this.userStartedViewingProjectEventViewProvider = userStartedViewingProjectEventViewProvider;
        this.userStoppedViewingProjectEventViewProvider = userStoppedViewingProjectEventViewProvider;
    }

    public void start(WebProtegeEventBus eventBus) {
        eventBus.addProjectEventHandler(projectId, ProjectChangedEvent.TYPE, event -> postChangeEvent(event));
        eventBus.addProjectEventHandler(projectId, ON_COMMENT_POSTED, event -> postNotePostedEvent(event));
        eventBus.addProjectEventHandler(projectId, UserStartingViewingProjectEvent.TYPE, event -> postUserStartedViewingProjectEvent(event));
        eventBus.addProjectEventHandler(projectId, UserStoppedViewingProjectEvent.TYPE, event -> postUserStoppedViewingProjectEvent(event));
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

    public void postNotePostedEvent(CommentPostedEvent event) {
        final CommentId noteId = event.getComment().getId();
        if(commentIds.contains(noteId)) {
            return;
        }
        commentIds.add(noteId);
        final CommentPostedEventView eventView = notePostedEventViewProvider.get();
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
