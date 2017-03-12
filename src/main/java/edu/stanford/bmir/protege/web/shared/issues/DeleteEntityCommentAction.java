package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Oct 2016
 */
public class DeleteEntityCommentAction implements Action<DeleteEntityCommentResult> {

    private CommentId commentId;

    public static DeleteEntityCommentAction deleteComment(@Nonnull CommentId commentId) {
        return new DeleteEntityCommentAction(commentId);
    }

    public DeleteEntityCommentAction(@Nonnull CommentId commentId) {
        this.commentId = checkNotNull(commentId);
    }

    @GwtSerializationConstructor
    private DeleteEntityCommentAction() {
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
