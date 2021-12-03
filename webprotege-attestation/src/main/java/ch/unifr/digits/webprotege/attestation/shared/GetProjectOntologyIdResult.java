package ch.unifr.digits.webprotege.attestation.shared;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Set;

public class GetProjectOntologyIdResult implements Result {
    private Set<OWLOntologyID> ontologyIDs;

    protected GetProjectOntologyIdResult() {
    }

    public GetProjectOntologyIdResult(Set<OWLOntologyID> ontologyIDs) {
        this.ontologyIDs = ontologyIDs;
    }

    public Set<OWLOntologyID> getOntologyIDs() {
        return ontologyIDs;
    }
}
