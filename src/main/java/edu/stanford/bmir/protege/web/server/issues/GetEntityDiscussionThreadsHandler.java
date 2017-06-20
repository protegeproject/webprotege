package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ProjectPermissionValidator;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
import edu.stanford.bmir.protege.web.shared.issues.GetEntityDiscussionThreadsAction;
import edu.stanford.bmir.protege.web.shared.issues.GetEntityDiscussionThreadsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
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
    private final ProjectManager projectManager;

    @Inject
    public GetEntityDiscussionThreadsHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull EntityDiscussionThreadRepository repository,
                                             @Nonnull ProjectManager projectManager) {
        this.repository = checkNotNull(repository);
        this.accessManager = checkNotNull(accessManager);
        this.projectManager = checkNotNull(projectManager);
    }

    @Override
    public Class<GetEntityDiscussionThreadsAction> getActionClass() {
        return GetEntityDiscussionThreadsAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetEntityDiscussionThreadsAction action, RequestContext requestContext) {
        return new ProjectPermissionValidator(accessManager,
                                              action.getProjectId(),
                                              requestContext.getUserId(),
                                              VIEW_OBJECT_COMMENT.getActionId());
    }

    @Override
    public GetEntityDiscussionThreadsResult execute(GetEntityDiscussionThreadsAction action, ExecutionContext executionContext) {
        List<EntityDiscussionThread> threads = repository.findThreads(action.getProjectId(), action.getEntity());
        Project project = projectManager.getProject(action.getProjectId(), executionContext.getUserId());
        OWLEntityData entityData = project.getRenderingManager().getRendering(action.getEntity());
        return new GetEntityDiscussionThreadsResult(entityData,
                                                    ImmutableList.copyOf(threads));
    }
}
