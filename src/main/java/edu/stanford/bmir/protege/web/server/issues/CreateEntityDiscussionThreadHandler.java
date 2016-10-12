package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
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
public class CreateEntityDiscussionThreadHandler extends AbstractHasProjectActionHandler<CreateEntityDiscussionThreadAction, CreateEntityDiscussionThreadResult> {

    @Nonnull
    private final ValidatorFactory<ReadPermissionValidator> validator;

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Inject
    public CreateEntityDiscussionThreadHandler(@Nonnull OWLAPIProjectManager projectManager,
                                               @Nonnull ValidatorFactory<ReadPermissionValidator> validator,
                                               @Nonnull EntityDiscussionThreadRepository repository) {
        super(projectManager);
        this.validator = validator;
        this.repository = repository;
    }

    @Override
    public Class<CreateEntityDiscussionThreadAction> getActionClass() {
        return CreateEntityDiscussionThreadAction.class;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(CreateEntityDiscussionThreadAction action,
                                                             RequestContext requestContext) {
        return validator.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected CreateEntityDiscussionThreadResult execute(CreateEntityDiscussionThreadAction action,
                                                         OWLAPIProject project,
                                                         ExecutionContext executionContext) {
        Comment comment = new Comment(
                CommentId.create(),
                executionContext.getUserId(),
                System.currentTimeMillis(),
                Optional.empty(),
                action.getComment());
        EntityDiscussionThread thread = new EntityDiscussionThread(ThreadId.create(),
                                                                   action.getProjectId(),
                                                                   action.getEntity(),
                                                                   Status.OPEN,
                                                                   ImmutableList.of(comment));
        repository.saveThread(thread);
        project.getEventManager().postEvent(new DiscussionThreadCreatedEvent(thread));
        List<EntityDiscussionThread> threads = repository.findThreads(action.getProjectId(), action.getEntity());
        return new CreateEntityDiscussionThreadResult(ImmutableList.copyOf(threads));
    }
}
