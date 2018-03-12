package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2018
 */
public class MergeEntitiesAction extends AbstractHasProjectAction<MergeEntitiesResult> {

    private OWLEntity sourceEntity;

    private OWLEntity targetEntity;

    private MergedEntityTreatment treatment;

    /**
     * @param projectId    The project id
     * @param sourceEntity The entity being merged into another class
     * @param targetEntity The entity that the source class is being merged into.
     * @param treatment    The treatment.
     */
    public MergeEntitiesAction(@Nonnull ProjectId projectId,
                               @Nonnull OWLEntity sourceEntity,
                               @Nonnull OWLEntity targetEntity,
                               @Nonnull MergedEntityTreatment treatment) {
        super(projectId);
        this.sourceEntity = checkNotNull(sourceEntity);
        this.targetEntity = checkNotNull(targetEntity);
        this.treatment = checkNotNull(treatment);
    }

    @GwtSerializationConstructor
    private MergeEntitiesAction() {
        super();
    }

    /**
     * Creates a {@link MergeEntitiesAction}.  An entity merge is directional – one entity is merged into
     * another entity.
     * @param projectId The project to perform the merge in.
     * @param sourceEntity The entity that will be merged into another entity.
     * @param targetEntity The entity that will have the source entity merged into it.
     * @param treatment The treatment for the merged entity that specifies whether the merged entity
     *                  will be deleted or deprecated.
     */
    public static MergeEntitiesAction mergeEntities(@Nonnull ProjectId projectId,
                                                    @Nonnull OWLEntity sourceEntity,
                                                    @Nonnull OWLEntity targetEntity,
                                                    @Nonnull MergedEntityTreatment treatment) {
        return new MergeEntitiesAction(projectId, sourceEntity, targetEntity, treatment);
    }

    /**
     * Gets the class that is being merged into another class
     */
    @Nonnull
    public OWLEntity getSourceEntity() {
        return sourceEntity;
    }

    /**
     * Gets the class that the other class is being merged into.
     */
    @Nonnull
    public OWLEntity getTargetEntity() {
        return targetEntity;
    }

    /**
     * Get the treatment for the entity that is being merged into another entity.
     */
    @Nonnull
    public MergedEntityTreatment getTreatment() {
        return treatment;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getProjectId(), sourceEntity, targetEntity, treatment);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MergeEntitiesAction)) {
            return false;
        }
        MergeEntitiesAction other = (MergeEntitiesAction) obj;
        return this.getProjectId().equals(other.getProjectId())
                && this.sourceEntity.equals(other.sourceEntity)
                && this.targetEntity.equals(other.targetEntity)
                && this.treatment.equals(other.treatment);
    }


    @Override
    public String toString() {
        return toStringHelper("MergeEntitiesAction")
                .addValue(getProjectId())
                .add("source", sourceEntity)
                .add("target", targetEntity)
                .add("treatment", treatment)
                .toString();
    }
}
