package edu.stanford.bmir.protege.web.server.notes.api;

import edu.stanford.bmir.protege.web.shared.notes.Note;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
@Deprecated
public class NoteStoreException extends RuntimeException {

    private Note note;

    public NoteStoreException(Throwable cause, Note note) {
        super(cause);
        this.note = note;
    }

    public NoteStoreException(Note note) {
        this.note = note;
    }

    public Note getNote() {
        return note;
    }
}
