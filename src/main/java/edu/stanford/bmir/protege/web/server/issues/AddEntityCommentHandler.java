package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.CommentPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.shared.issues.AddEntityCommentAction;
import edu.stanford.bmir.protege.web.shared.issues.AddEntityCommentResult;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.ThreadId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
public class AddEntityCommentHandler implements ActionHandler<AddEntityCommentAction, AddEntityCommentResult> {

    private final ValidatorFactory<CommentPermissionValidator> validator;

    private final EntityDiscussionThreadRepository repository;

    @Inject
    public AddEntityCommentHandler(ValidatorFactory<CommentPermissionValidator> validator,
                                   EntityDiscussionThreadRepository repository) {
        this.validator = checkNotNull(validator);
        this.repository = checkNotNull(repository);
    }

    @Override
    public Class<AddEntityCommentAction> getActionClass() {
        return AddEntityCommentAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(AddEntityCommentAction action, RequestContext requestContext) {
        return validator.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    public AddEntityCommentResult execute(AddEntityCommentAction action, ExecutionContext executionContext) {
        UserId createdBy = executionContext.getUserId();
        long createdAt = System.currentTimeMillis();
        Comment comment = new Comment(createdBy, createdAt, Optional.empty(), action.getComment());
        ThreadId threadId = action.getThreadId();
        repository.addCommentToThread(threadId, comment);
        return new AddEntityCommentResult(action.getProjectId(), threadId, comment);
    }
}
