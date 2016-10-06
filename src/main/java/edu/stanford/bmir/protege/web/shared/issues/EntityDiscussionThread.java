package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;


import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 *
 * A thread of comments that are attached to an entity
 */
public class EntityDiscussionThread implements IsSerializable {

    private ThreadId id;

    private ProjectId projectId;

    private OWLEntity targetEntity;

    private ImmutableList<Comment> comments;

    private Status status;

    @Inject
    public EntityDiscussionThread(@Nonnull ThreadId id,
                                  @Nonnull ProjectId projectId,
                                  @Nonnull OWLEntity targetEntity,
                                  @Nonnull ImmutableList<Comment> comments,
                                  @Nonnull Status status) {
        this.id = checkNotNull(id);
        this.projectId = checkNotNull(projectId);
        this.targetEntity = checkNotNull(targetEntity);
        this.comments = checkNotNull(comments);
        this.status = checkNotNull(status);
    }

    @GwtSerializationConstructor
    private EntityDiscussionThread() {
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public ThreadId getId() {
        return id;
    }

    @Nonnull
    public OWLEntity getTargetEntity() {
        return targetEntity;
    }

    @Nonnull
    public Status getStatus() {
        return status;
    }

    @Nonnull
    public ImmutableList<Comment> getComments() {
        return comments;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, targetEntity, comments, status);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof EntityDiscussionThread)) {
            return false;
        }
        EntityDiscussionThread other = (EntityDiscussionThread) obj;
        return this.id.equals(other.id)
                && this.targetEntity.equals(other.targetEntity)
                && this.comments.equals(other.comments)
                && this.status.equals(other.status);
    }


    @Override
    public String toString() {
        return toStringHelper("EntityCommentsThread")
                .addValue(id)
                .add("targetEntity", targetEntity)
                .add("status", status)
                .add("comments", comments)
                .toString();
    }
}
