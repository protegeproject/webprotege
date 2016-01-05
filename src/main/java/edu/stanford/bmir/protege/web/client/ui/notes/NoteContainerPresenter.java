package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.notes.DiscussionThread;
import edu.stanford.bmir.protege.web.shared.notes.Note;

import javax.inject.Inject;

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

    @Inject
    public NoteContainerPresenter(NoteContainerView noteContainerView) {
        this.noteContainerView = noteContainerView;
    }

    public void setNote(Note note, DiscussionThread context) {
        this.note = note;
        noteContainerView.setNote(note, context);
    }

    public NoteContainerView getNoteContainerView() {
        return noteContainerView;
    }

    public Widget getWidget() {
        return noteContainerView.getWidget();
    }


}
