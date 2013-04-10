package edu.stanford.bmir.protege.web.server.notes.impl.chao;

import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import org.semanticweb.owlapi.model.OWLDataProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
public class FoundZeroButExpectedOneDataPropertyValueException extends MalformedNoteException {

    private OWLDataProperty dataProperty;

    public FoundZeroButExpectedOneDataPropertyValueException(NoteId noteId, OWLDataProperty dataProperty) {
        super(noteId);
        this.dataProperty = dataProperty;
    }

    public OWLDataProperty getDataProperty() {
        return dataProperty;
    }
}
