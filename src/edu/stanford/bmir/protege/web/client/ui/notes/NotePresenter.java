package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.notes.Note;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class NotePresenter  {

    private NoteView noteView;

    private Note note;

    public NotePresenter(NoteView noteView) {
        this.noteView = noteView;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
        String author = note.getAuthor().getUserName();
        noteView.setAuthor(author);
        long timestamp = note.getTimestamp();
        noteView.setTimestamp(timestamp);
        SafeHtml body = new SafeHtmlBuilder().appendHtmlConstant(note.getBody()).toSafeHtml();
        noteView.setBody(body);
    }

    public NoteView getNoteView() {
        return noteView;
    }

    public Widget getWidget() {
        return noteView.getWidget();
    }
}
