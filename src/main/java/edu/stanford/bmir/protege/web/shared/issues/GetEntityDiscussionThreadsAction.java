package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class GetEntityDiscussionThreadsAction implements Action<GetEntityDiscussionThreadsResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    public GetEntityDiscussionThreadsAction(@Nonnull ProjectId projectId, @Nonnull OWLEntity entity) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
    }

    public static GetEntityDiscussionThreadsAction getDiscussionThreads(@Nonnull ProjectId projectId,
                                                                        @Nonnull OWLEntity entity) {
        return new GetEntityDiscussionThreadsAction(projectId, entity);
    }

    @GwtSerializationConstructor
    private GetEntityDiscussionThreadsAction() {
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entity);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetEntityDiscussionThreadsAction)) {
            return false;
        }
        GetEntityDiscussionThreadsAction other = (GetEntityDiscussionThreadsAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entity.equals(other.entity);
    }


    @Override
    public String toString() {
        return toStringHelper("GetCommentThreadsAction")
                .addValue(projectId)
                .addValue(entity)
                .toString();
    }
}
