package edu.stanford.bmir.protege.web.client.ui.ontology.id;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import org.semanticweb.owlapi.model.OWLOntologyID;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
public class GetOntologyIdResult implements Result {

    private OWLOntologyID ontologyID;

    private GetOntologyIdResult() {
    }

    public GetOntologyIdResult(OWLOntologyID ontologyID) {
        this.ontologyID = ontologyID;
    }

    public OWLOntologyID getOntologyID() {
        return ontologyID;
    }
}
