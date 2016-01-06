package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetDiscussionThreadAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetDiscussionThreadResult;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.HandlerRegistrationManager;
import edu.stanford.bmir.protege.web.shared.event.NotePostedEvent;
import edu.stanford.bmir.protege.web.shared.event.NotePostedHandler;
import edu.stanford.bmir.protege.web.shared.notes.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class DiscussionThreadPresenter implements HasDispose {

    private final DispatchServiceManager dispatchServiceManager;

    private DiscussionThreadView view;

    private OWLEntity currentTarget;

    private ProjectId projectId;

    private final HandlerRegistrationManager handlerRegistrationManager;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private final ActiveProjectManager activeProjectManager;

    private final Provider<NoteContainerPresenter> noteContainerPresenterProvider;

    @Inject
    public DiscussionThreadPresenter(ProjectId projectId,
                                     EventBus eventBus,
                                     DispatchServiceManager dispatchServiceManager,
                                     LoggedInUserProjectPermissionChecker permissionChecker,
                                     DiscussionThreadView view,
                                     ActiveProjectManager activeProjectManager,
                                     Provider<NoteContainerPresenter> noteContainerPresenterProvider) {
        this.view = view;
        this.dispatchServiceManager = dispatchServiceManager;
        this.noteContainerPresenterProvider = noteContainerPresenterProvider;
        this.permissionChecker = permissionChecker;
        handlerRegistrationManager = new HandlerRegistrationManager(eventBus);
        this.projectId = checkNotNull(projectId);
        this.activeProjectManager = activeProjectManager;

        handlerRegistrationManager.registerHandlerToProject(projectId, NotePostedEvent.TYPE, new NotePostedHandler() {
            @Override
            public void handleNotePosted(NotePostedEvent event) {
                refreshForNotePosted(event);
            }
        });

        handlerRegistrationManager.registerHandlerToProject(projectId, NoteDeletedEvent.TYPE, new NoteDeletedHandler() {
            @Override
            public void handleNoteDeleted(NoteDeletedEvent event) {
                reload();
            }
        });

        handlerRegistrationManager.registerHandler(UserLoggedInEvent.TYPE, new UserLoggedInHandler() {
            @Override
            public void handleUserLoggedIn(UserLoggedInEvent event) {

            }
        });

        handlerRegistrationManager.registerHandler(UserLoggedOutEvent.TYPE, new UserLoggedOutHandler() {
            @Override
            public void handleUserLoggedOut(UserLoggedOutEvent event) {

            }
        });
    }


    public void clearTarget() {
        view.removeAllNotes();
        currentTarget = null;
        view.setPostNewTopicEnabled(false);
        view.setPostNewTopicHandler(new PostNewTopicHandlerImpl(Optional.fromNullable(currentTarget), dispatchServiceManager, activeProjectManager));
    }

    public void setTarget(OWLEntity target) {
        currentTarget = target;
        view.setPostNewTopicEnabled(false);
        view.setPostNewTopicHandler(new PostNewTopicHandlerImpl(Optional.fromNullable(currentTarget), dispatchServiceManager, activeProjectManager));
        if(currentTarget != null) {
            permissionChecker.hasCommentPermission(new DispatchServiceCallback<Boolean>() {
                @Override
                public void handleSuccess(Boolean hasPermission) {
                    view.setPostNewTopicEnabled(currentTarget != null && hasPermission);
                }
            });
        }
        reload();
    }

    private void reload() {
        if(currentTarget == null) {
            return;
        }
        dispatchServiceManager.execute(new GetDiscussionThreadAction(projectId, currentTarget), new DispatchServiceCallback<GetDiscussionThreadResult>() {

            @Override
            public void handleSuccess(GetDiscussionThreadResult result) {
                displayDiscussionThread(result.getThread());
            }
        });

    }

    private void refreshForNotePosted(NotePostedEvent event) {
//        if(event.getInReplyTo().isPresent()) {
//            if(currentNoteIds.isEmpty() || currentNoteIds.contains(event.getInReplyTo().get())) {
                reload();
//            }
//        }
//        else if(event.getTargetAsEntityData().isPresent()) {
//           if(currentTarget.equals(event.getTargetAsEntityData().get().getEntity())) {
//               reload();
//           }
//        }
    }


    private void displayDiscussionThread(DiscussionThread thread) {
        view.removeAllNotes();
        final List<Note> rootNotes = new ArrayList<Note>(thread.getRootNotes());
        Collections.sort(rootNotes, new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                return (int) -(o1.getTimestamp() - o2.getTimestamp());
            }
        });
        for(Note rootNote : rootNotes) {
            appendNote(rootNote, 0, thread);
        }
    }


    private void appendNote(Note note, int depth, DiscussionThread discussionThread) {
        NoteContainerPresenter noteContainerPresenter = noteContainerPresenterProvider.get();
        noteContainerPresenter.setNote(note, discussionThread);
        view.addNote(noteContainerPresenter, depth);
        for(Note childNote : discussionThread.getReplies(note.getNoteId())) {
            appendNote(childNote, depth + 1, discussionThread);
        }
    }

    public Widget getWidget() {
        return view.getWidget();
    }

    @Override
    public void dispose() {
        handlerRegistrationManager.removeHandlers();
    }
}
