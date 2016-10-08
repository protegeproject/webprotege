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
public class GetEntityDiscussionThreadsHandler implements ActionHandler<GetEntityDiscussionThreadsAction, GetEntityDiscussionThreadsResult> {

    @Nonnull
    private final ValidatorFactory<ReadPermissionValidator> validatorFactory;

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Inject
    public GetEntityDiscussionThreadsHandler(@Nonnull ValidatorFactory<ReadPermissionValidator> validatorFactory,
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
        System.out.println("Getting threads");
        List<EntityDiscussionThread> threads = repository.findThreads(action.getProjectId(), action.getEntity());
        return new GetEntityDiscussionThreadsResult(ImmutableList.copyOf(threads));
    }
}
