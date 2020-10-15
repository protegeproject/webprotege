package edu.stanford.bmir.protege.web.shared.merge_add;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.List;

public class GetAllOntologiesResult implements Result {
    private List<OWLOntologyID> ontologies;

    private GetAllOntologiesResult() {
    }

    public GetAllOntologiesResult(List<OWLOntologyID> ontologies) {
        this.ontologies = ontologies;
    }

    public List<OWLOntologyID> getOntologies() {
        return ontologies;
    }
}
