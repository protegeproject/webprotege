package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.shared.issues.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class CreateEntityDiscussionThreadHandler implements ActionHandler<CreateEntityDiscussionThreadAction, CreateEntityDiscussionThreadResult> {

    @Nonnull
    private final ValidatorFactory<ReadPermissionValidator> validator;

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Inject
    public CreateEntityDiscussionThreadHandler(@Nonnull ValidatorFactory<ReadPermissionValidator> validator,
                                               @Nonnull EntityDiscussionThreadRepository repository) {
        this.validator = validator;
        this.repository = repository;
    }

    @Override
    public Class<CreateEntityDiscussionThreadAction> getActionClass() {
        return CreateEntityDiscussionThreadAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(CreateEntityDiscussionThreadAction action,
                                                RequestContext requestContext) {
        return validator.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    public CreateEntityDiscussionThreadResult execute(CreateEntityDiscussionThreadAction action,
                                                      ExecutionContext executionContext) {
        // TODO: CHECK COMMENT IS NOT MALICIOUS
        Comment comment = new Comment(executionContext.getUserId(),
                                      System.currentTimeMillis(),
                                      Optional.empty(),
                                      action.getComment());
        EntityDiscussionThread thread = new EntityDiscussionThread(ThreadId.create(),
                                                                   action.getProjectId(),
                                                                   action.getEntity(),
                                                                   Status.OPEN,
                                                                   ImmutableList.of(comment));
        repository.saveThread(thread);
        List<EntityDiscussionThread> threads = repository.findThreads(action.getProjectId(), action.getEntity());
        return new CreateEntityDiscussionThreadResult(ImmutableList.copyOf(threads));
    }
}
