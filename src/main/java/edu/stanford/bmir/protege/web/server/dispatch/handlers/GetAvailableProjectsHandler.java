package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionsManager;
import edu.stanford.bmir.protege.web.shared.project.AvailableProject;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsAction;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static edu.stanford.bmir.protege.web.server.access.Subject.forUser;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.DOWNLOAD_PROJECT;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.MOVE_ANY_PROJECT_TO_TRASH;
import static java.util.stream.Collectors.toList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2013
 */
public class GetAvailableProjectsHandler implements ActionHandler<GetAvailableProjectsAction, GetAvailableProjectsResult> {

    private final ProjectPermissionsManager projectPermissionsManager;

    private final AccessManager accessManager;

    @Inject
    public GetAvailableProjectsHandler(@Nonnull ProjectPermissionsManager projectPermissionsManager,
                                       @Nonnull AccessManager accessManager) {
        this.projectPermissionsManager = projectPermissionsManager;
        this.accessManager = accessManager;
    }

    @Override
    public Class<GetAvailableProjectsAction> getActionClass() {
        return GetAvailableProjectsAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetAvailableProjectsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetAvailableProjectsResult execute(GetAvailableProjectsAction action, ExecutionContext executionContext) {
        UserId userId = executionContext.getUserId();
        List<AvailableProject> availableProjects = projectPermissionsManager.getReadableProjects(userId).stream()
               .sorted()
               .map(details -> {
                   Subject user = forUser(executionContext.getUserId());
                   ProjectResource projectResource = new ProjectResource(details.getProjectId());
                   boolean downloadable = accessManager.hasPermission( user, projectResource, DOWNLOAD_PROJECT);
                   boolean trashable = details.getOwner().equals(executionContext.getUserId())
                           || accessManager.hasPermission(user, projectResource, MOVE_ANY_PROJECT_TO_TRASH);
                   return new AvailableProject(details, downloadable, trashable);
               })
               .collect(toList());

        return new GetAvailableProjectsResult(availableProjects);
    }
}
