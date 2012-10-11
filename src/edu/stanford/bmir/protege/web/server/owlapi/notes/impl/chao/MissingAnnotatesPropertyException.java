package edu.stanford.bmir.protege.web.server.owlapi.notes.impl.chao;

import edu.stanford.bmir.protege.web.server.owlapi.notes.api.NoteId;

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
