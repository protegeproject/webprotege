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
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class GetEntityDiscussionThreadsActionHandler implements ActionHandler<GetEntityDiscussionThreadsAction, GetEntityDiscussionThreadsResult> {

    @Nonnull
    private final ValidatorFactory<ReadPermissionValidator> validatorFactory;

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Inject
    public GetEntityDiscussionThreadsActionHandler(@Nonnull ValidatorFactory<ReadPermissionValidator> validatorFactory,
                                                   @Nonnull EntityDiscussionThreadRepository repository) {
        this.validatorFactory = checkNotNull(validatorFactory);
        this.repository = checkNotNull(repository);
    }

    @Override
    public Class<GetEntityDiscussionThreadsAction> getActionClass() {
        return GetEntityDiscussionThreadsAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetEntityDiscussionThreadsAction action, RequestContext requestContext) {
        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    public GetEntityDiscussionThreadsResult execute(GetEntityDiscussionThreadsAction action, ExecutionContext executionContext) {
        ImmutableList<Comment> comments = ImmutableList.of(
                new Comment(executionContext.getUserId(), System.currentTimeMillis(), Optional.empty(), "Hello world"),
                new Comment(executionContext.getUserId(), System.currentTimeMillis(), Optional.empty(), "Yes hello"),
                new Comment(executionContext.getUserId(), System.currentTimeMillis(), Optional.empty(), "Hello world")
                );
        EntityDiscussionThread t = new EntityDiscussionThread(new ThreadId(UUID.randomUUID().toString()), action.getProjectId(), action.getEntity(),
                                                              Status.OPEN,
                                                              comments
        );
        repository.saveThread(t);
        System.out.println("Getting threads");
        List<EntityDiscussionThread> threads = repository.findThreads(action.getProjectId(), action.getEntity());
        return new GetEntityDiscussionThreadsResult(ImmutableList.copyOf(threads));
    }
}
