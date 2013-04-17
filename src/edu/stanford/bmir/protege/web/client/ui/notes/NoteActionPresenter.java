package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorDialog;
import edu.stanford.bmir.protege.web.shared.notes.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class NoteActionPresenter {

    private NoteActionView view;

    private NoteId currentNoteId;


    public NoteActionPresenter(NoteActionView noteActionView) {
        this.view = noteActionView;
    }

    public void setNoteId(NoteId noteId, DiscussionThread context) {
        this.currentNoteId = noteId;
        view.setReplyToNoteHandler(new ReplyToNoteHandlerImpl(currentNoteId));

        view.setDeleteNoteHandler(new DeleteNoteHandlerImpl(currentNoteId));

        view.setCanDelete(!context.hasReplies(noteId));
    }

    public NoteActionView getView() {
        return view;
    }

    public Widget getWidget() {
        return view.getWidget();
    }
}
