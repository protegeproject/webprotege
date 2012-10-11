package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/08/2012
 */
public class CannotRemoveNoteWithRepliesException extends RuntimeException {

    private NoteId note;

    public CannotRemoveNoteWithRepliesException(NoteId note) {
        this.note = note;
    }

    public NoteId getNote() {
        return note;
    }
}
