package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class GetFormResult implements Result {

    private ProjectId projectId;

    private OWLEntity entity;

    private FormDescriptor formDescriptor;

    private GetFormResult() {
    }

    public GetFormResult(ProjectId projectId, OWLEntity entity, FormDescriptor formDescriptor) {
        this.projectId = projectId;
        this.entity = entity;
        this.formDescriptor = this.formDescriptor;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public FormDescriptor getFormData() {
        return formDescriptor;
    }
}
