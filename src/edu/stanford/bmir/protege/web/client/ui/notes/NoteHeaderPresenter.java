package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.shared.notes.Note;
import edu.stanford.bmir.protege.web.shared.notes.NoteField;
import edu.stanford.bmir.protege.web.shared.notes.NoteStatus;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2013
 */
public class NoteHeaderPresenter {

    private NoteHeaderView noteHeaderView;

    private Note note;

    public NoteHeaderPresenter(NoteHeaderView noteHeaderView) {
        this.noteHeaderView = noteHeaderView;
        noteHeaderView.setResolveNoteHandler(new ResolveNoteHandler() {
            @Override
            public void handleResolvePressed() {
                MessageBox.alert("Resolve note: " + note.getNoteId());
            }
        });
    }

    public Widget getWidget() {
        return noteHeaderView.getWidget();
    }

    public void setNote(Note note) {
        this.note = note;
        noteHeaderView.setAuthor(note.getAuthor());
        noteHeaderView.setSubject(note.getContent().getFieldValue(NoteField.SUBJECT));
        Optional<NoteStatus> status = note.getContent().getFieldValue(NoteField.STATUS);
        noteHeaderView.setStatus(status);
        noteHeaderView.setResolveOptionVisible(!status.isPresent() && Application.get().getUserId().equals(note.getAuthor()));

    }
}
