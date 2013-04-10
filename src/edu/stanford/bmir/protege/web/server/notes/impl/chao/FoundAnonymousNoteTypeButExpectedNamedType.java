package edu.stanford.bmir.protege.web.server.notes.impl.chao;

import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import org.semanticweb.owlapi.model.OWLClassExpression;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
public class FoundAnonymousNoteTypeButExpectedNamedType extends MalformedNoteException {

    private OWLClassExpression type;

    public FoundAnonymousNoteTypeButExpectedNamedType(NoteId noteId, OWLClassExpression type) {
        super(noteId);
        this.type = type;
    }

    public OWLClassExpression getType() {
        return type;
    }
}
