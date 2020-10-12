package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.entity.FreshEntityIri;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-30
 */
public class CreateEntityFromFormDataAction implements ProjectAction<CreateEntityFromFormDataResult> {

    private ProjectId projectId;

    private EntityType<?> entityType;

    private FreshEntityIri freshEntityIri;

    private FormData formData;

    public CreateEntityFromFormDataAction(@Nonnull ProjectId projectId,
                                          @Nonnull EntityType<?> entityType,
                                          @Nonnull FreshEntityIri freshEntityIri,
                                          @Nonnull FormData formData) {
        this.projectId = projectId;
        this.entityType = entityType;
        this.freshEntityIri = freshEntityIri;
        this.formData = formData;
    }

    @GwtSerializationConstructor
    private CreateEntityFromFormDataAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    public FreshEntityIri getFreshEntityIri() {
        return freshEntityIri;
    }

    public FormData getFormData() {
        return formData;
    }
}
