package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.CommentPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.CompositeRequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserIsDicussionThreadCreatorValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
public class SetDiscussionThreadStatusHandler extends AbstractHasProjectActionHandler<SetDiscussionThreadStatusAction, SetDiscussionThreadStatusResult> {

    @Nonnull
    private final ValidatorFactory<CommentPermissionValidator> validatorFactory;

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Inject
    public SetDiscussionThreadStatusHandler(@Nonnull OWLAPIProjectManager projectManager,
                                            @Nonnull ValidatorFactory<CommentPermissionValidator> validatorFactory,
                                            @Nonnull EntityDiscussionThreadRepository repository) {
        super(projectManager);
        this.validatorFactory = validatorFactory;
        this.repository = repository;
    }

    @Override
    public Class<SetDiscussionThreadStatusAction> getActionClass() {
        return SetDiscussionThreadStatusAction.class;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(SetDiscussionThreadStatusAction action,
                                                             RequestContext requestContext) {
        return CompositeRequestValidator.get(
                Arrays.asList(
                        validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId()),
                        new UserIsDicussionThreadCreatorValidator(repository,
                                                                  action.getThreadId(),
                                                                  requestContext.getUserId())
                )
        );
    }

    @Override
    protected SetDiscussionThreadStatusResult execute(SetDiscussionThreadStatusAction action,
                                                      OWLAPIProject project,
                                                      ExecutionContext executionContext) {
        ThreadId threadId = action.getThreadId();
        Status status = action.getStatus();
        repository.setThreadStatus(threadId, status);
        ProjectId projectId = action.getProjectId();
        project.getEventManager().postEvent(new DiscussionThreadStatusChangedEvent(projectId,
                                                                                   threadId,
                                                                                   status));
        return new SetDiscussionThreadStatusResult(threadId, status);
    }
}
