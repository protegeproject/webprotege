package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Oct 2016
 */
public class DeleteEntityCommentResult implements Result {

    private CommentId commentId;

    private boolean deleted;

    public DeleteEntityCommentResult(@Nonnull CommentId commentId, boolean deleted) {
        this.commentId = checkNotNull(commentId);
        this.deleted = deleted;
    }

    @GwtSerializationConstructor
    private DeleteEntityCommentResult() {
    }

    @Nonnull
    public CommentId getCommentId() {
        return commentId;
    }

    public boolean wasDeleted() {
        return deleted;
    }
}
