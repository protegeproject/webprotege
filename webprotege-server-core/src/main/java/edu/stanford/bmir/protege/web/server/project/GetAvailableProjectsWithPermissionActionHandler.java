package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.Resource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsWithPermissionAction;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsWithPermissionResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
public class GetAvailableProjectsWithPermissionActionHandler implements ApplicationActionHandler<GetAvailableProjectsWithPermissionAction, GetAvailableProjectsWithPermissionResult> {

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public GetAvailableProjectsWithPermissionActionHandler(@Nonnull AccessManager accessManager,
                                                           @Nonnull ProjectDetailsManager projectDetailsManager) {
        this.accessManager = accessManager;
        this.projectDetailsManager = projectDetailsManager;
    }

    @Nonnull
    @Override
    public Class<GetAvailableProjectsWithPermissionAction> getActionClass() {
        return GetAvailableProjectsWithPermissionAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetAvailableProjectsWithPermissionAction action,
                                                @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetAvailableProjectsWithPermissionResult execute(@Nonnull GetAvailableProjectsWithPermissionAction action,
                                                            @Nonnull ExecutionContext executionContext) {
        var userId = Subject.forUser(executionContext.getUserId());
        var permission = action.getPermission();
        var projectDetails = accessManager.getResourcesAccessibleToSubject(userId, permission)
                     .stream()
                     .map(Resource::getProjectId)
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .map(projectDetailsManager::getProjectDetails)
                     .collect(toImmutableList());
        return new GetAvailableProjectsWithPermissionResult(projectDetails);
    }
}
