package edu.stanford.bmir.protege.web.server.notes.impl.chao;

import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
@Deprecated
public interface MalformedNoteHandler {

    void handleMalformedNote(NoteId noteId, OWLOntology notesOntology, MalformedNoteException e);
}
