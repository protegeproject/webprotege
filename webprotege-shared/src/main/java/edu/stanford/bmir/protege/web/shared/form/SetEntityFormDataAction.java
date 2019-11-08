package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class SetEntityFormDataAction implements ProjectAction<SetEntityFormDataResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private FormDescriptor formDescriptor;

    private FormData formData;

    public SetEntityFormDataAction(ProjectId projectId,
                                   OWLEntity entity,
                                   FormDescriptor formDescriptor,
                                   FormData formData) {
        this.projectId = projectId;
        this.entity = entity;
        this.formDescriptor = formDescriptor;
        this.formData = formData;
    }

    @GwtSerializationConstructor
    private SetEntityFormDataAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public FormDescriptor getFormDescriptor() {
        return formDescriptor;
    }

    public FormData getFormData() {
        return formData;
    }
}
