package edu.stanford.bmir.protege.web.client.permissions;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectPermissionsResult;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserIdProjectIdKey;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Manages the permissions for projects and users.
 * @author Matthew Horridge
 */
@ApplicationSingleton
public class PermissionManager implements HasDispose {

    private final EventBus eventBus;

    private final DispatchServiceManager dispatchServiceManager;

    private final HandlerRegistration loggedInHandler;

    private final HandlerRegistration loggedOutHandler;

    private final ActiveProjectManager activeProjectManager;

    private final LoggedInUserProvider loggedInUserProvider;

    private final Multimap<UserIdProjectIdKey, ActionId> permittedActionCache = HashMultimap.create();

    private final DispatchErrorMessageDisplay errorDisplay;

    @Inject
    public PermissionManager(EventBus eventBus, DispatchServiceManager dispatchServiceManager, ActiveProjectManager activeProjectManager, LoggedInUserProvider loggedInUserProvider, DispatchErrorMessageDisplay errorDisplay) {
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
        this.activeProjectManager = activeProjectManager;
        this.loggedInUserProvider = loggedInUserProvider;
        loggedInHandler = eventBus.addHandler(UserLoggedInEvent.ON_USER_LOGGED_IN, event -> firePermissionsChanged());
        loggedOutHandler = eventBus.addHandler(UserLoggedOutEvent.ON_USER_LOGGED_OUT, event -> firePermissionsChanged());
        this.errorDisplay = errorDisplay;
    }

    /**
     * Fires a {@link PermissionsChangedEvent} for the
     * current project on the event bus.
     */
    public void firePermissionsChanged() {
        GWT.log("[PermissionManager] Firing permissions changed");
        permittedActionCache.clear();
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        final Optional<ProjectId> projectId = activeProjectManager.getActiveProjectId();
        if (!projectId.isPresent()) {
            return;
        }
        final ProjectId theProjectId = projectId.get();
        dispatchServiceManager.execute(new GetProjectPermissionsAction(projectId.get(), userId), result -> {
            UserIdProjectIdKey key = new UserIdProjectIdKey(userId, theProjectId);
            permittedActionCache.putAll(key, result.getAllowedActions());
            GWT.log("[PermissionManager] Firing permissions changed for project: " + projectId);
            eventBus.fireEventFromSource(new PermissionsChangedEvent(theProjectId).asGWTEvent(), theProjectId);
        });

    }

    /**
     * Determines if the specified user has permission to execute the specified action on the
     * specified project.
     * @param userId The {@link UserId} to test for.
     * @param actionId The {@link ActionId} to test for.
     * @param projectId The {@link ProjectId} to test for.
     * @param callback A callback for receiving the result.
     */
    public void hasPermissionForProject(@Nonnull UserId userId,
                                        @Nonnull ActionId actionId,
                                        @Nonnull ProjectId projectId,
                                        @Nonnull DispatchServiceCallback<Boolean> callback) {
        final UserIdProjectIdKey key = new UserIdProjectIdKey(userId, projectId);
        if(permittedActionCache.containsKey(key)) {
            callback.onSuccess(permittedActionCache.get(key).contains(actionId));
            return;
        }
        dispatchServiceManager.execute(new GetProjectPermissionsAction(projectId, userId),
                                       new DispatchServiceCallback<GetProjectPermissionsResult>(errorDisplay) {
                                           @Override
                                           public void handleSuccess(GetProjectPermissionsResult result) {
                                               permittedActionCache.putAll(key, result.getAllowedActions());
                                               callback.onSuccess(result.getAllowedActions().contains(actionId));
                                           }

                                           @Override
                                           public void handleErrorFinally(Throwable throwable) {
                                               callback.handleErrorFinally(throwable);
                                           }
                                       });
    }

    public void dispose() {
        loggedInHandler.removeHandler();
        loggedOutHandler.removeHandler();
    }

}
