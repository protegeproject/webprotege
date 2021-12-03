package ch.unifr.digits.webprotege.attestation.shared;

import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

public class VerifyAction implements ProjectAction<VerifyResult> {
    private ProjectId projectId;
    private String iri;
    private String versionIri;
    private String hash;
    private OWLEntity entity;
    private Mode mode;

    /**
     * For serialization purposes only
     */
    public VerifyAction() {
    }

    public VerifyAction(ProjectId projectId, String iri, String versionIri, String hash, OWLEntity entity, Mode mode) {
        this.projectId = projectId;
        this.iri = iri;
        this.versionIri = versionIri;
        this.hash = hash;
        this.entity = entity;
        this.mode = mode;
    }

    public String getIri() {
        return iri;
    }

    public void setIri(String iri) {
        this.iri = iri;
    }

    public String getVersionIri() {
        return versionIri;
    }

    public void setVersionIri(String versionIri) {
        this.versionIri = versionIri;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public void setEntity(OWLEntity entity) {
        this.entity = entity;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public enum Mode {
        ONTOLOGY,
        ENTITY
    }
}
