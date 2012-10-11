package edu.stanford.bmir.protege.web.server.owlapi.notes.impl.chao;

import edu.stanford.bmir.protege.web.server.owlapi.notes.api.NoteId;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
public class FoundAnonymousButExpectedNamedIndividualException extends MalformedNoteException {

    private OWLObjectProperty property;

    private OWLAnonymousIndividual individual;

    public FoundAnonymousButExpectedNamedIndividualException(NoteId noteId, OWLObjectProperty property, OWLAnonymousIndividual individual) {
        super(noteId);
        this.property = property;
        this.individual = individual;
    }

    public OWLObjectProperty getProperty() {
        return property;
    }

    public OWLAnonymousIndividual getIndividual() {
        return individual;
    }
}
