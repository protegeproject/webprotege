package edu.stanford.bmir.protege.web.server.owlapi.notes.impl.chao;

import edu.stanford.bmir.protege.web.server.owlapi.notes.api.NoteId;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
public class QuietMalformedNoteHandler implements MalformedNoteHandler {

    public void handleMalformedNote(NoteId noteId, OWLOntology notesOntology, MalformedNoteException e) {
        StringBuilder sb = new StringBuilder();
        sb.append("Malformed note: ");
        sb.append(noteId);
        sb.append(" in ");
        sb.append(notesOntology.getOntologyID());
        sb.append(".  Reason: [");
        sb.append(e.getClass().getSimpleName());
        sb.append("] ");
        sb.append(e.getMessage());
        System.err.println(sb.toString());
    }
}
