package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.CommentPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.EditCommentAction;
import edu.stanford.bmir.protege.web.shared.issues.EditCommentResult;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Oct 2016
 */
public class EditCommentActionHandler implements ActionHandler<EditCommentAction, EditCommentResult> {

    private final ValidatorFactory<CommentPermissionValidator> validator;

    private final EntityDiscussionThreadRepository repository;

    @Inject
    public EditCommentActionHandler(ValidatorFactory<CommentPermissionValidator> validator,
                                    EntityDiscussionThreadRepository repository) {
        this.validator = validator;
        this.repository = repository;
    }

    @Override
    public Class<EditCommentAction> getActionClass() {
        return EditCommentAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(EditCommentAction action, RequestContext requestContext) {
        return validator.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    public EditCommentResult execute(EditCommentAction action, ExecutionContext executionContext) {
        Optional<EntityDiscussionThread> thread = repository.getThread(action.getThreadId());
        if (!thread.isPresent()) {
            throw new RuntimeException("Invalid comment thread");
        }
        EntityDiscussionThread t = thread.get();
        Optional<Comment> updatedComment = t.getComments().stream()
                                            .filter(c -> c.getId().equals(action.getCommentId()))
                                            .limit(1)
                                            .map(c -> new Comment(c.getId(),
                                                                  c.getCreatedBy(),
                                                                  c.getCreatedAt(),
                                                                  Optional.of(System.currentTimeMillis()),
                                                                  action.getBody()))
                                            .peek(c -> repository.updateComment(t.getId(), c))
                                            .findFirst();
        return new EditCommentResult(updatedComment.get());
    }
}
