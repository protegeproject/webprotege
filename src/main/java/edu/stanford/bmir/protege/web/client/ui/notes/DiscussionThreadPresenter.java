package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.Application;
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

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class DiscussionThreadPresenter implements HasDispose {

    private DiscussionThreadView view;

    private OWLEntity currentTarget;

    private ProjectId projectId;

    private Set<NoteId> currentNoteIds = new HashSet<NoteId>();

    private HandlerRegistrationManager handlerRegistrationManager = new HandlerRegistrationManager();


    public DiscussionThreadPresenter(ProjectId projectId) {
        view = new DiscussionThreadViewImpl();
        this.projectId = checkNotNull(projectId);

        handlerRegistrationManager.registerHandlerToProject(projectId, NotePostedEvent.TYPE, new NotePostedHandler() {
            @Override
            public void handleNotePosted(NotePostedEvent event) {
                refreshForNotePosted(event);
            }
        });

        handlerRegistrationManager.registerHandlerToProject(projectId, NoteDeletedEvent.TYPE, new NoteDeletedHandler() {
            @Override
            public void handleNoteDeleted(NoteDeletedEvent event) {
                GWT.log("Received delete");
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
        view.setPostNewTopicHandler(new PostNewTopicHandlerImpl(Optional.fromNullable(currentTarget)));
    }

    public void setTarget(OWLEntity target) {
        currentTarget = target;
        UserId userId = Application.get().getUserId();
        view.setPostNewTopicEnabled(currentTarget != null && !userId.isGuest());
        view.setPostNewTopicHandler(new PostNewTopicHandlerImpl(Optional.fromNullable(currentTarget)));
        reload();
    }

    private void reload() {
        GWT.log("Current target: " + currentTarget);
        if(currentTarget == null) {
            return;
        }
        DispatchServiceManager.get().execute(new GetDiscussionThreadAction(projectId, currentTarget), new DispatchServiceCallback<GetDiscussionThreadResult>() {

            @Override
            public void handleSuccess(GetDiscussionThreadResult result) {
                displayDiscussionThread(result.getThread());
            }
        });

    }

    private void refreshForNotePosted(NotePostedEvent event) {
        GWT.log("Should refresh for posted note");
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
        currentNoteIds.clear();
        currentNoteIds.addAll(thread.getNoteIds());
    }


    private void appendNote(Note note, int depth, DiscussionThread discussionThread) {
        NoteContainerPresenter noteContainerPresenter = new NoteContainerPresenter(new NoteContainerViewImpl());
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
