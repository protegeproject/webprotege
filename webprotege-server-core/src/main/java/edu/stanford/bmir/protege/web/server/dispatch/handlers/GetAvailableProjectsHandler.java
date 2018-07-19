package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionsManager;
import edu.stanford.bmir.protege.web.server.user.UserActivityManager;
import edu.stanford.bmir.protege.web.server.user.UserActivityRecord;
import edu.stanford.bmir.protege.web.shared.project.AvailableProject;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsAction;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.server.access.Subject.forUser;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.DOWNLOAD_PROJECT;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.MOVE_ANY_PROJECT_TO_TRASH;
import static java.util.stream.Collectors.toList;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 01/04/2013
 */
public class GetAvailableProjectsHandler implements ApplicationActionHandler<GetAvailableProjectsAction, GetAvailableProjectsResult> {

    private final ProjectPermissionsManager projectPermissionsManager;

    private final AccessManager accessManager;

    private final UserActivityManager userActivityManager;

    @Inject
    public GetAvailableProjectsHandler(@Nonnull ProjectPermissionsManager projectPermissionsManager,
                                       @Nonnull AccessManager accessManager,
                                       @Nonnull UserActivityManager userActivityManager) {
        this.projectPermissionsManager = projectPermissionsManager;
        this.accessManager = accessManager;
        this.userActivityManager = userActivityManager;
    }

    @Nonnull
    @Override
    public Class<GetAvailableProjectsAction> getActionClass() {
        return GetAvailableProjectsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetAvailableProjectsAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetAvailableProjectsResult execute(@Nonnull GetAvailableProjectsAction action, @Nonnull ExecutionContext executionContext) {
        UserId userId = executionContext.getUserId();
        Optional<UserActivityRecord> userActivityRecord = userActivityManager.getUserActivityRecord(executionContext.getUserId());
        Map<ProjectId, Long> lastOpenedMap = new HashMap<>();
        userActivityRecord.ifPresent(record ->
                                             record.getRecentProjects()
                                                   .forEach(recent ->
                                                                    lastOpenedMap.put(recent.getProjectId(), recent.getTimestamp())));
        List<AvailableProject> availableProjects = projectPermissionsManager.getReadableProjects(userId).stream()
                                                                            .map(details -> {
                                                                                Subject user = forUser(userId);
                                                                                ProjectResource projectResource = new ProjectResource(details.getProjectId());
                                                                                boolean downloadable = accessManager.hasPermission(user, projectResource, DOWNLOAD_PROJECT);
                                                                                boolean trashable = details.getOwner().equals(userId)
                                                                                        || accessManager.hasPermission(user, projectResource, MOVE_ANY_PROJECT_TO_TRASH);
                                                                                long lastOpened = lastOpenedMap.getOrDefault(details.getProjectId(), 0L);
                                                                                return AvailableProject.get(details, downloadable, trashable, lastOpened);
                                                                            })
                                                                            .collect(toList());
        return new GetAvailableProjectsResult(availableProjects);
    }
}
