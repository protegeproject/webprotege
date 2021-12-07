package ch.unifr.digits.webprotege.attestation.shared;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public class OntologyHashAction extends AbstractHasProjectAction<OntologyHashResult> {
    private String iri;

    /**
     * For serialization purposes only
     */
    protected OntologyHashAction() {

    }

    public OntologyHashAction(ProjectId projectId, String iri) {
        super(projectId);
        this.iri = iri;
    }

    public String getIri() {
        return iri;
    }

    public void setIri(String iri) {
        this.iri = iri;
    }
}
