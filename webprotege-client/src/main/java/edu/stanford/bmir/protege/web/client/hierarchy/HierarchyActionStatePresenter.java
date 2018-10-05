package edu.stanford.bmir.protege.web.client.hierarchy;

import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent.ON_USER_LOGGED_IN;
import static edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent.ON_USER_LOGGED_OUT;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 6 Dec 2017
 */
public class HierarchyActionStatePresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private final Map<ActionId, UIAction> actionMap = new HashMap<>();

    private boolean selectionPresent = false;

    @Inject
    public HierarchyActionStatePresenter(@Nonnull ProjectId projectId,
                                         @Nonnull LoggedInUserProjectPermissionChecker permissionChecker) {
        this.projectId = projectId;
        this.permissionChecker = permissionChecker;
    }

    public void registerAction(@Nonnull BuiltInAction actionId,
                               @Nonnull UIAction uiAction) {
        registerAction(actionId.getActionId(), uiAction);
    }

    public void registerAction(@Nonnull ActionId actionId,
                               @Nonnull UIAction action) {
        actionMap.put(checkNotNull(actionId),
                      checkNotNull(action));
    }

    public void start(@Nonnull WebProtegeEventBus eventBus) {
        eventBus.addProjectEventHandler(projectId,
                                        ON_PERMISSIONS_CHANGED,
                                        event -> updateActionStates());
        eventBus.addApplicationEventHandler(ON_USER_LOGGED_OUT,
                                            event -> updateActionStates());
        eventBus.addApplicationEventHandler(ON_USER_LOGGED_IN,
                                            event -> updateActionStates());
        updateActionStates();
    }

    public void setSelectionPresent(boolean selectionPresent) {
        if (selectionPresent != this.selectionPresent) {
            this.selectionPresent = selectionPresent;
            updateActionStates();
        }

    }

    private void updateActionStates() {
        actionMap.forEach(this::updateState);
    }

    private void updateState(@Nonnull ActionId actionId,
                             @Nonnull UIAction action) {
        action.setEnabled(false);
        permissionChecker.hasPermission(actionId,
                                        perm -> {
                                            action.setEnabled(perm && (!action.requiresSelection() || selectionPresent));
                                        });

    }
}
