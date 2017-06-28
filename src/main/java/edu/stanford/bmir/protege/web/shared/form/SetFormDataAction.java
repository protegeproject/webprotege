package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2017
 */
public class SetFormDataAction implements ProjectAction<SetFormDataResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private FormData formData;

    public SetFormDataAction(ProjectId projectId,
                             OWLEntity entity,
                             FormData formData) {
        this.projectId = projectId;
        this.entity = entity;
        this.formData = formData;
    }

    @GwtSerializationConstructor
    private SetFormDataAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public FormData getFormData() {
        return formData;
    }


    @Override
    public String toString() {
        return toStringHelper("SetFormDataAction")
                .addValue(projectId)
                .addValue(entity)
                .addValue(formData)
                .toString();
    }
}
