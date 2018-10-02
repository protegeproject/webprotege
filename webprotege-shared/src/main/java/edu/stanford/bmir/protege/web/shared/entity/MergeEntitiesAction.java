package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.bulkop.HasCommitMessage;
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
public class MergeEntitiesAction extends AbstractHasProjectAction<MergeEntitiesResult> implements HasCommitMessage {

    private ImmutableSet<OWLEntity> sourceEntities;

    private OWLEntity targetEntity;

    private MergedEntityTreatment treatment;

    private String commitMessage;

    /**
     * @param projectId    The project id
     * @param sourceEntities The set of entities being merged into another entity
     * @param targetEntity The entity that the source class is being merged into.
     * @param treatment    The treatment.
     * @param commitMessage
     */
    public MergeEntitiesAction(@Nonnull ProjectId projectId,
                               @Nonnull ImmutableSet<OWLEntity> sourceEntities,
                               @Nonnull OWLEntity targetEntity,
                               @Nonnull MergedEntityTreatment treatment,
                               @Nonnull String commitMessage) {
        super(projectId);
        this.sourceEntities = checkNotNull(sourceEntities);
        this.targetEntity = checkNotNull(targetEntity);
        this.treatment = checkNotNull(treatment);
        this.commitMessage = commitMessage;
    }

    @GwtSerializationConstructor
    private MergeEntitiesAction() {
        super();
    }

    /**
     * Creates a {@link MergeEntitiesAction}.  An entity merge is directional – one entity is merged into
     * another entity.
     * @param projectId The project to perform the merge in.
     * @param sourceEntities The entities that will be merged into another entity.
     * @param targetEntity The entity that will have the source entity merged into it.
     * @param treatment The treatment for the merged entity that specifies whether the merged entity
     *                  will be deleted or deprecated.
     */
    public static MergeEntitiesAction mergeEntities(@Nonnull ProjectId projectId,
                                                    @Nonnull ImmutableSet<OWLEntity> sourceEntities,
                                                    @Nonnull OWLEntity targetEntity,
                                                    @Nonnull MergedEntityTreatment treatment,
                                                    @Nonnull String commitMessage) {
        return new MergeEntitiesAction(projectId, sourceEntities, targetEntity, treatment, commitMessage);
    }

    /**
     * Gets the class that is being merged into another class
     */
    @Nonnull
    public ImmutableSet<OWLEntity> getSourceEntity() {
        return sourceEntities;
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
        return Objects.hashCode(getProjectId(), sourceEntities, targetEntity, treatment, commitMessage);
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
                && this.sourceEntities.equals(other.sourceEntities)
                && this.targetEntity.equals(other.targetEntity)
                && this.treatment.equals(other.treatment)
                && this.commitMessage.equals(other.commitMessage);
    }


    @Override
    public String toString() {
        return toStringHelper("MergeEntitiesAction")
                .addValue(getProjectId())
                .add("sourceEntities", sourceEntities)
                .add("target", targetEntity)
                .add("treatment", treatment)
                .add("commitMessage", commitMessage)
                .toString();
    }

    @Nonnull
    @Override
    public String getCommitMessage() {
        return commitMessage;
    }
}
