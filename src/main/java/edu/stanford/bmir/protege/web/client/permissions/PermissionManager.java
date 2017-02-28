package edu.stanford.bmir.protege.web.client.permissions;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserIdProjectIdKey;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * Manages the permissions for projects and users.
 * @author Matthew Horridge
 */
@Singleton
public class PermissionManager implements HasDispose {

    private final EventBus eventBus;

    private final DispatchServiceManager dispatchServiceManager;

    private final HandlerRegistration loggedInHandler;

    private final HandlerRegistration loggedOutHandler;

    private final ActiveProjectManager activeProjectManager;

    private final LoggedInUserProvider loggedInUserProvider;

    private final Multimap<UserIdProjectIdKey, ActionId> actionCache = HashMultimap.create();

    @Inject
    public PermissionManager(EventBus eventBus, DispatchServiceManager dispatchServiceManager, ActiveProjectManager activeProjectManager, LoggedInUserProvider loggedInUserProvider) {
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
        this.activeProjectManager = activeProjectManager;
        this.loggedInUserProvider = loggedInUserProvider;
        loggedInHandler = eventBus.addHandler(UserLoggedInEvent.TYPE, event -> firePermissionsChanged());
        loggedOutHandler = eventBus.addHandler(UserLoggedOutEvent.TYPE, event -> firePermissionsChanged());
    }

    /**
     * Fires a {@link PermissionsChangedEvent} for the
     * current project on the event bus.
     */
    public void firePermissionsChanged() {
        actionCache.clear();
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        final Optional<ProjectId> projectId = activeProjectManager.getActiveProjectId();
        if (!projectId.isPresent()) {
            return;
        }
        dispatchServiceManager.execute(new GetPermissionsAction(projectId.get(), userId), new DispatchServiceCallback<GetPermissionsResult>() {
            @Override
            public void handleSuccess(GetPermissionsResult result) {
                UserIdProjectIdKey key = new UserIdProjectIdKey(userId, projectId.get());
                actionCache.putAll(key, result.getAllowedActions());
                GWT.log("[PermissionManager] Firing permissions changed for project: " + projectId);
                eventBus.fireEventFromSource(new PermissionsChangedEvent(projectId.get()).asGWTEvent(), projectId.get());
            }
        });

    }

    public void hasPermissionForProject(UserId userId,
                                        ActionId actionId,
                                        ProjectId projectId,
                                        DispatchServiceCallback<Boolean> callback) {
        final UserIdProjectIdKey key = new UserIdProjectIdKey(userId, projectId);
        if(actionCache.containsKey(key)) {
            callback.onSuccess(actionCache.get(key).contains(actionId));
            return;
        }
        dispatchServiceManager.execute(new GetPermissionsAction(projectId, userId),
                                       new DispatchServiceCallback<GetPermissionsResult>() {
                                           @Override
                                           public void handleSuccess(GetPermissionsResult result) {
                                               actionCache.putAll(key, result.getAllowedActions());
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
