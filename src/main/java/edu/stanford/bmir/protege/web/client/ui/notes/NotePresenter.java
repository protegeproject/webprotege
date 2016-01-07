package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.notes.Note;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class NotePresenter  {

    private NoteView noteView;

    @Inject
    public NotePresenter(NoteView noteView) {
        this.noteView = noteView;
    }

    public void setNote(Note note) {
        String author = note.getAuthor().getUserName();
        noteView.setAuthor(author);
        long timestamp = note.getTimestamp();
        noteView.setTimestamp(timestamp);
        String bodyHtml = note.getBody().replace("\n", "<br>");
        SafeHtml body = new SafeHtmlBuilder().appendHtmlConstant(bodyHtml).toSafeHtml();
        noteView.setBody(body);
    }

    public NoteView getView() {
        return noteView;
    }
}
