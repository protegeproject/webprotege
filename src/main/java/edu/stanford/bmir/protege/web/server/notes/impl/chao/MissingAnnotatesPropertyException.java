package edu.stanford.bmir.protege.web.server.notes.impl.chao;

import edu.stanford.bmir.protege.web.shared.notes.NoteId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
public class MissingAnnotatesPropertyException extends MalformedNoteException {

    public MissingAnnotatesPropertyException(NoteId noteId) {
        super(noteId);
    }
}
