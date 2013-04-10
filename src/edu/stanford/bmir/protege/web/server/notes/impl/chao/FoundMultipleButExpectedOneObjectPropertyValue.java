package edu.stanford.bmir.protege.web.server.notes.impl.chao;

import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
public class FoundMultipleButExpectedOneObjectPropertyValue extends MalformedNoteException {

    private OWLObjectProperty property;

    public FoundMultipleButExpectedOneObjectPropertyValue(NoteId noteId, OWLObjectProperty objectProperty) {
        super(noteId);
        this.property = objectProperty;
    }

    public OWLObjectProperty getProperty() {
        return property;
    }
}
