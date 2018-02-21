package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Oct 2016
 */
public class DeleteEntityCommentAction implements ProjectAction<DeleteEntityCommentResult> {

    private ProjectId projectId;

    private CommentId commentId;

    public static DeleteEntityCommentAction deleteComment(@Nonnull ProjectId projectId,
                                                          @Nonnull CommentId commentId) {
        return new DeleteEntityCommentAction(projectId, commentId);
    }

    public DeleteEntityCommentAction(@Nonnull ProjectId projectId,
                                     @Nonnull CommentId commentId) {
        this.commentId = checkNotNull(commentId);
        this.projectId = checkNotNull(projectId);
    }

    @GwtSerializationConstructor
    private DeleteEntityCommentAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public CommentId getCommentId() {
        return commentId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(commentId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DeleteEntityCommentAction)) {
            return false;
        }
        DeleteEntityCommentAction other = (DeleteEntityCommentAction) obj;
        return this.commentId.equals(other.commentId);
    }
}
