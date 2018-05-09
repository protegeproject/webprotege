package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.shared.issues.DeleteEntityCommentAction;
import edu.stanford.bmir.protege.web.shared.issues.DeleteEntityCommentResult;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Oct 2016
 */
public class DeleteEntityCommentHandler implements ProjectActionHandler<DeleteEntityCommentAction, DeleteEntityCommentResult> {

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Inject
    public DeleteEntityCommentHandler(@Nonnull EntityDiscussionThreadRepository repository) {
        this.repository = repository;
    }

    @Nonnull
    @Override
    public Class<DeleteEntityCommentAction> getActionClass() {
        return DeleteEntityCommentAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull DeleteEntityCommentAction action, @Nonnull RequestContext requestContext) {
        return () -> {
            Optional<EntityDiscussionThread> thread = repository.findThreadByCommentId(action.getCommentId());
            if(!thread.isPresent()) {
                return getInvalidRequest();
            }
            long commentCount = thread.get().getComments().stream()
                    .filter(c -> c.getCreatedBy().equals(requestContext.getUserId()))
                    .filter(c -> c.getId().equals(action.getCommentId()))
                    .count();
            if(commentCount != 1L) {
                return getInvalidRequest();
            }
            return RequestValidationResult.getValid();
        };
    }

    private RequestValidationResult getInvalidRequest() {
        return RequestValidationResult.getInvalid("You do not have permission the delete this comment");
    }

    @Nonnull
    @Override
    public DeleteEntityCommentResult execute(@Nonnull DeleteEntityCommentAction action, @Nonnull ExecutionContext executionContext) {
        boolean deleted = repository.deleteComment(action.getCommentId());
        return new DeleteEntityCommentResult(action.getCommentId(), deleted);
    }
}
