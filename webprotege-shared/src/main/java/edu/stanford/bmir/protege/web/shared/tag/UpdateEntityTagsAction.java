package edu.stanford.bmir.protege.web.shared.tag;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Mar 2018
 */
public class UpdateEntityTagsAction implements ProjectAction<UpdateEntityTagsResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private Set<TagId> fromTagIds;

    private Set<TagId> toTagIds;

    public UpdateEntityTagsAction(@Nonnull ProjectId projectId,
                                  OWLEntity entity, @Nonnull Set<TagId> fromTagIds,
                                  @Nonnull Set<TagId> toTagIds) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
        this.fromTagIds = ImmutableSet.copyOf(fromTagIds);
        this.toTagIds = ImmutableSet.copyOf(toTagIds);
    }

    @GwtSerializationConstructor
    private UpdateEntityTagsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public Set<TagId> getFromTagIds() {
        return ImmutableSet.copyOf(fromTagIds);
    }

    public Set<TagId> getToTagIds() {
        return ImmutableSet.copyOf(toTagIds);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entity, fromTagIds, toTagIds);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UpdateEntityTagsAction)) {
            return false;
        }
        UpdateEntityTagsAction other = (UpdateEntityTagsAction) obj;
        return this.projectId.equals(other.projectId)
                && this.fromTagIds.equals(other.fromTagIds)
                && this.toTagIds.equals(other.toTagIds)
                && this.entity.equals(other.entity);
    }


    @Override
    public String toString() {
        return toStringHelper("UpdateEntityTagsAction")
                .addValue(projectId)
                .addValue(entity)
                .add("from", fromTagIds)
                .add("to", toTagIds)
                .toString();
    }
}
