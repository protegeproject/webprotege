package edu.stanford.bmir.protege.web.server.owlapi.notes.impl.chao;

import edu.stanford.bmir.protege.web.server.owlapi.notes.api.NoteId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
public class FoundZeroButExpectedOneObjectPropertyValue extends MalformedNoteException {

    private OWLObjectProperty property;

    public FoundZeroButExpectedOneObjectPropertyValue(NoteId noteId, OWLObjectProperty property) {
        super(noteId);
        this.property = property;
    }

    public OWLObjectProperty getProperty() {
        return property;
    }
}
