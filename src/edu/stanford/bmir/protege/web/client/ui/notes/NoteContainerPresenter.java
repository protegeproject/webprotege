package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.notes.Note;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class NoteContainerPresenter {

    private NoteContainerView noteContainerView;

    private Note note;

    public Note getNote() {
        return note;
    }

    public NoteContainerPresenter(NoteContainerView noteContainerView) {
        this.noteContainerView = noteContainerView;
    }

    public void setNote(Note note) {
        this.note = note;
        noteContainerView.setNote(note);
    }

    public NoteContainerView getNoteContainerView() {
        return noteContainerView;
    }

    public Widget getWidget() {
        return noteContainerView.getWidget();
    }


}
