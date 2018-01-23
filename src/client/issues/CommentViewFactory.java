package edu.stanford.bmir.protege.web.client.issues;

import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.issues.Comment;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Oct 2016
 */
public class CommentViewFactory {

    private final LoggedInUserProvider loggedInUserProvider;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Inject
    public CommentViewFactory(@Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
                              @Nonnull LoggedInUserProvider loggedInUserProvider) {
        this.permissionChecker = checkNotNull(permissionChecker);
        this.loggedInUserProvider = loggedInUserProvider;
    }

    /**
     * Creates and initializes a view for the specified comment.  The various
     * buttons that allow actions on the comment to be performed will be enabled/disabled
     * in accordance with the logged in user identity.
     *
     * @param comment       The comment.
     * @param replyHandler  A handler for replying to the comment.
     * @param editHandler   A handler for editing the comment.
     * @param deleteHandler A handler for deleting the comment.
     * @return A view for the comment
     */
    public CommentView createAndInitView(@Nonnull Comment comment,
                                         @Nonnull ReplyToCommentHandler replyHandler,
                                         @Nonnull EditCommentHandler editHandler,
                                         @Nonnull DeleteCommentHandler deleteHandler) {
        final CommentView commentView = new CommentViewImpl();
        commentView.setCreatedBy(comment.getCreatedBy());
        commentView.setCreatedAt(comment.getCreatedAt());
        commentView.setUpdatedAt(comment.getUpdatedAt());
        commentView.setBody(comment.getRenderedBody());
        commentView.setReplyToCommentHandler(replyHandler);
        commentView.setEditCommentHandler(editHandler);
        commentView.setDeleteCommentHandler(deleteHandler);
        commentView.setReplyButtonVisible(false);
        return commentView;
    }

}
