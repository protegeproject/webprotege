package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.ProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ProjectPermissionValidator;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
import edu.stanford.bmir.protege.web.shared.issues.GetEntityDiscussionThreadsAction;
import edu.stanford.bmir.protege.web.shared.issues.GetEntityDiscussionThreadsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_OBJECT_COMMENT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class GetEntityDiscussionThreadsHandler implements ProjectActionHandler<GetEntityDiscussionThreadsAction, GetEntityDiscussionThreadsResult> {

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetEntityDiscussionThreadsHandler(@Nonnull EntityDiscussionThreadRepository repository,
                                             @Nonnull AccessManager accessManager,
                                             @Nonnull RenderingManager renderingManager) {
        this.repository = repository;
        this.accessManager = accessManager;
        this.renderingManager = renderingManager;
    }

    @Nonnull
    @Override
    public Class<GetEntityDiscussionThreadsAction> getActionClass() {
        return GetEntityDiscussionThreadsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetEntityDiscussionThreadsAction action, @Nonnull RequestContext requestContext) {
        return new ProjectPermissionValidator(accessManager,
                                              action.getProjectId(),
                                              requestContext.getUserId(),
                                              VIEW_OBJECT_COMMENT.getActionId());
    }

    @Nonnull
    @Override
    public GetEntityDiscussionThreadsResult execute(@Nonnull GetEntityDiscussionThreadsAction action, @Nonnull ExecutionContext executionContext) {
        List<EntityDiscussionThread> threads = repository.findThreads(action.getProjectId(), action.getEntity());
        OWLEntityData entityData = renderingManager.getRendering(action.getEntity());
        return new GetEntityDiscussionThreadsResult(entityData,
                                                    ImmutableList.copyOf(threads));
    }
}
