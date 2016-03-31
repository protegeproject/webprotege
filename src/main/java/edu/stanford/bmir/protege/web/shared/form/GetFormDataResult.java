package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class GetFormDataResult implements Result {

    private ProjectId projectId;

    private OWLEntity entity;

    private FormData formData;

    private GetFormDataResult() {
    }

    public GetFormDataResult(ProjectId projectId, OWLEntity entity, FormData formData) {
        this.projectId = projectId;
        this.entity = entity;
        this.formData = formData;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public FormData getFormData() {
        return formData;
    }
}
