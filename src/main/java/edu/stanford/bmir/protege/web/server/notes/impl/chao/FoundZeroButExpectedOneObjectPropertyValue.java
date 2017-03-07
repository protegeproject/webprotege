package edu.stanford.bmir.protege.web.server.notes.impl.chao;

import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
@Deprecated
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
