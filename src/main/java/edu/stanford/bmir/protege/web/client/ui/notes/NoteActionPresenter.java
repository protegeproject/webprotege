package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.notes.DiscussionThread;
import edu.stanford.bmir.protege.web.shared.notes.Note;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class NoteActionPresenter {

    private NoteActionView view;

    private final DispatchServiceManager dispatchServiceManager;

    public NoteActionPresenter(NoteActionView noteActionView, DispatchServiceManager dispatchServiceManager) {
        this.view = noteActionView;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void setNote(Note note, DiscussionThread context) {
        UserId userId = Application.get().getUserId();
        view.setReplyToNoteHandler(new ReplyToNoteHandlerImpl(note.getNoteId(), dispatchServiceManager));
        view.setCanReply(!userId.isGuest());
        view.setDeleteNoteHandler(new DeleteNoteHandlerImpl(dispatchServiceManager, note.getNoteId()));
        view.setCanDelete(!context.hasReplies(note.getNoteId()) && note.getAuthor().equals(userId));
    }

    public NoteActionView getView() {
        return view;
    }

    public Widget getWidget() {
        return view.getWidget();
    }
}
