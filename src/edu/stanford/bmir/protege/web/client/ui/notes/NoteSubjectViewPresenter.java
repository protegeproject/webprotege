package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2013
 */
public class NoteSubjectViewPresenter {

    private NoteSubjectView noteSubjectView;

    public NoteSubjectViewPresenter(NoteSubjectView noteSubjectView) {
        this.noteSubjectView = noteSubjectView;
    }

    public void setSubject(Optional<String> subject) {
        noteSubjectView.setSubject(subject);
    }

    public void setAuthor(UserId userId) {
        noteSubjectView.setAuthor(userId);
    }

    public NoteSubjectView getView() {
        return noteSubjectView;
    }
}
