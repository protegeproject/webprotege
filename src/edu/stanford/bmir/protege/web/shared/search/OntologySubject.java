package edu.stanford.bmir.protege.web.shared.search;

import org.semanticweb.owlapi.model.OWLOntologyID;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public class OntologySubject extends SearchSubject {

    private OWLOntologyID ontologyID;

    private OntologySubject() {
    }

    public OntologySubject(OWLOntologyID ontologyID) {
        this.ontologyID = ontologyID;
    }

    @Override
    public OWLOntologyID getSubject() {
        return ontologyID;
    }
}
