package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchService;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.shared.notes.DiscussionThread;
import edu.stanford.bmir.protege.web.shared.notes.Note;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class NoteActionPresenter {

    private final NoteActionView view;

    private final LoggedInUserProvider loggedInUserProvider;

    private final ReplyToNoteHandler replyToNoteHandler;

    private final DeleteNoteHandler deleteNoteHandler;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Inject
    public NoteActionPresenter(NoteActionView noteActionView,
                               LoggedInUserProjectPermissionChecker permissionChecker,
                               ReplyToNoteHandler replyToNoteHandler,
                               DeleteNoteHandler deleteNoteHandler,
                               LoggedInUserProvider loggedInUserProvider) {
        this.view = noteActionView;
        this.permissionChecker = permissionChecker;
        this.replyToNoteHandler = replyToNoteHandler;
        this.deleteNoteHandler = deleteNoteHandler;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    public void setNote(Note note, DiscussionThread context) {
        UserId userId = loggedInUserProvider.getCurrentUserId();
        view.setNoteId(note.getNoteId());
        view.setReplyToNoteHandler(replyToNoteHandler);
        view.setCanReply(false);
        view.setDeleteNoteHandler(deleteNoteHandler);
        view.setCanDelete(!context.hasReplies(note.getNoteId()) && note.getAuthor().equals(userId));
        permissionChecker.hasCommentPermission(new DispatchServiceCallback<Boolean>() {
            @Override
            public void handleSuccess(Boolean hasPermission) {
                view.setCanReply(hasPermission);
            }
        });
    }

    public NoteActionView getView() {
        return view;
    }

    public Widget getWidget() {
        return view.getWidget();
    }
}
