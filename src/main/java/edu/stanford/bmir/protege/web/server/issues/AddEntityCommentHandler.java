package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.CommentPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.events.HasPostEvents;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.issues.*;
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
public class AddEntityCommentHandler extends AbstractHasProjectActionHandler<AddEntityCommentAction, AddEntityCommentResult> {

    private final ValidatorFactory<CommentPermissionValidator> validator;

    private final EntityDiscussionThreadRepository repository;


    @Inject
    public AddEntityCommentHandler(OWLAPIProjectManager projectManager,
                                   ValidatorFactory<CommentPermissionValidator> validator,
                                   EntityDiscussionThreadRepository repository) {
        super(projectManager);
        this.validator = checkNotNull(validator);
        this.repository = checkNotNull(repository);
    }

    @Override
    public Class<AddEntityCommentAction> getActionClass() {
        return AddEntityCommentAction.class;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(AddEntityCommentAction action,
                                                             RequestContext requestContext) {
        return validator.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected AddEntityCommentResult execute(AddEntityCommentAction action,
                                             OWLAPIProject project,
                                             ExecutionContext executionContext) {
        UserId createdBy = executionContext.getUserId();
        long createdAt = System.currentTimeMillis();
        Comment comment = new Comment(CommentId.create(), createdBy, createdAt, Optional.empty(), action.getComment());
        ThreadId threadId = action.getThreadId();
        repository.addCommentToThread(threadId, comment);
        project.getEventManager().postEvent(new CommentPostedEvent(action.getProjectId(), threadId, comment));
        return new AddEntityCommentResult(action.getProjectId(), threadId, comment);

    }
}
