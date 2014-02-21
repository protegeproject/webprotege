package edu.stanford.bmir.protege.web.server.notes.impl.chao;

import edu.stanford.bmir.protege.web.shared.notes.NoteId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
public abstract class MalformedNoteException extends RuntimeException {

    private NoteId noteId;

    public MalformedNoteException(NoteId noteId) {
        this.noteId = noteId;
    }
}
