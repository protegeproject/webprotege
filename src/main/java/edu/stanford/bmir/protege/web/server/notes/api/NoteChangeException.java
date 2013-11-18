package edu.stanford.bmir.protege.web.server.notes.api;

import edu.stanford.bmir.protege.web.shared.notes.Note;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class NoteChangeException extends NoteStoreException {

    public NoteChangeException(Throwable cause, Note note) {
        super(cause, note);
    }

    public NoteChangeException(Note note) {
        super(note);
    }
}
