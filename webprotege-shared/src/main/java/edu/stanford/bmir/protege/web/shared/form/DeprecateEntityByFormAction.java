package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-22
 */
public class DeprecateEntityByFormAction implements ProjectAction<DeprecateEntityByFormResult> {

    private OWLEntity entity;

    @Nullable
    private FormData deprecationFormData;

    private OWLEntity replacementEntity;

    private ProjectId projectId;

    @GwtSerializationConstructor
    private DeprecateEntityByFormAction() {
    }

    public DeprecateEntityByFormAction(OWLEntity entity,
                                       Optional<FormData> deprecationFormData,
                                       Optional<OWLEntity> replacementEntity,
                                       ProjectId projectId) {
        this.entity = checkNotNull(entity);
        this.deprecationFormData = checkNotNull(deprecationFormData).orElse(null);
        this.replacementEntity = checkNotNull(replacementEntity).orElse(null);
        this.projectId = checkNotNull(projectId);
        if(replacementEntity.isPresent()) {
            boolean entityTypesAreTheSame = replacementEntity.get()
                             .getEntityType()
                             .equals(entity.getEntityType());
            if(!entityTypesAreTheSame) {
                throw new IllegalArgumentException("Entity types for the entity being deprecated and the replacement entity must be the same");
            }
        }
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the entity to be deprecated
     */
    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    /**
     * Gets the form data for the deprecated entity
     */
    public Optional<FormData> getDeprecationFormData() {
        return Optional.ofNullable(deprecationFormData);
    }

    /**
     * Gets the optional replacement entity
     */
    public Optional<OWLEntity> getReplacementEntity() {
        return Optional.ofNullable(replacementEntity);
    }

}
