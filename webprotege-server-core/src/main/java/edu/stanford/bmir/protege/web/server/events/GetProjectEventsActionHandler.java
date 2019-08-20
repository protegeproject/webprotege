package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.event.EventTag;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.access.ProjectResource.forProject;
import static edu.stanford.bmir.protege.web.server.access.Subject.forUser;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class GetProjectEventsActionHandler implements ApplicationActionHandler<GetProjectEventsAction, GetProjectEventsResult> {

    @Nonnull
    private final ProjectManager projectManager;

    @Nonnull
    private final AccessManager accessManager;

    @Inject
    public GetProjectEventsActionHandler(@Nonnull ProjectManager projectManager,
                                         @Nonnull AccessManager accessManager) {
        this.projectManager = checkNotNull(projectManager);
        this.accessManager = checkNotNull(accessManager);
    }

    @Nonnull
    @Override
    public Class<GetProjectEventsAction> getActionClass() {
        return GetProjectEventsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetProjectEventsAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetProjectEventsResult execute(@Nonnull GetProjectEventsAction action, @Nonnull ExecutionContext executionContext) {
        EventTag sinceTag = action.getSinceTag();
        ProjectId projectId = action.getProjectId();
        UserId userId = executionContext.getUserId();
        if(!accessManager.hasPermission(forUser(userId),
                                        forProject(action.getProjectId()),
                                        VIEW_PROJECT)) {
            return getEmptyResult(projectId, sinceTag);
        }
        ProjectEventList projectEventList = projectManager.getProjectEventsSinceTag(projectId, sinceTag);
        return  new GetProjectEventsResult(projectEventList);
    }

    private static GetProjectEventsResult getEmptyResult(ProjectId projectId, EventTag sinceTag) {
        return new GetProjectEventsResult(ProjectEventList.builder(sinceTag, projectId, sinceTag).build());
    }
}
