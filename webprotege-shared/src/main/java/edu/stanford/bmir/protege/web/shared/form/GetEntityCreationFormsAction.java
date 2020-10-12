package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-28
 */
public class GetEntityCreationFormsAction implements ProjectAction<GetEntityCreationFormsResult> {

    private ProjectId projectId;

    @Nullable
    private OWLEntity parentEntity;

    private EntityType<?> entityType;

    public GetEntityCreationFormsAction(@Nonnull ProjectId projectId,
                                        @Nonnull OWLEntity parentEntity,
                                        @Nonnull EntityType<?> entityType) {
        this.projectId = checkNotNull(projectId);
        this.parentEntity = checkNotNull(parentEntity);
        this.entityType = checkNotNull(entityType);
    }

    @GwtSerializationConstructor
    private GetEntityCreationFormsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public EntityType<?> getEntityType() {
        return entityType;
    }

    @Nonnull
    public OWLEntity getParentEntity() {
        return parentEntity;
    }
}
