package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
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

    @Inject
    public NoteActionPresenter(NoteActionView noteActionView,
                               ReplyToNoteHandler replyToNoteHandler,
                               DeleteNoteHandler deleteNoteHandler,
                               LoggedInUserProvider loggedInUserProvider) {
        this.view = noteActionView;
        this.replyToNoteHandler = replyToNoteHandler;
        this.deleteNoteHandler = deleteNoteHandler;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    public void setNote(Note note, DiscussionThread context) {
        UserId userId = loggedInUserProvider.getCurrentUserId();
        view.setNoteId(note.getNoteId());
        view.setReplyToNoteHandler(replyToNoteHandler);
        view.setCanReply(!userId.isGuest());
        view.setDeleteNoteHandler(deleteNoteHandler);
        view.setCanDelete(!context.hasReplies(note.getNoteId()) && note.getAuthor().equals(userId));
    }

    public NoteActionView getView() {
        return view;
    }

    public Widget getWidget() {
        return view.getWidget();
    }
}
