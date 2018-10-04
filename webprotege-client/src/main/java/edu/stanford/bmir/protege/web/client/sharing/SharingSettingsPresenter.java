package edu.stanford.bmir.protege.web.client.sharing;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.app.PermissionScreener;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.client.permissions.PermissionManager;
import edu.stanford.bmir.protege.web.client.progress.BusyView;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.GetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_SHARING_SETTINGS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 01/03/16
 */
public class SharingSettingsPresenter implements Presenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final SharingSettingsView view;

    @Nonnull
    private final BusyView busyView;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final PermissionManager permissionManager;

    @Nonnull
    private final PermissionScreener permissionScreener;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Nonnull
    private final ProgressDisplay progressDisplay;

    @Inject
    public SharingSettingsPresenter(@Nonnull ProjectId projectId,
                                    @Nonnull SharingSettingsView view,
                                    @Nonnull BusyView busyView,
                                    @Nonnull DispatchServiceManager dispatchServiceManager,
                                    @Nonnull PermissionManager permissionManager,
                                    @Nonnull PermissionScreener permissionScreener,
                                    @Nonnull SettingsPresenter settingsPresenter,
                                    @Nonnull Messages messages, @Nonnull DispatchErrorMessageDisplay errorDisplay, @Nonnull ProgressDisplay progressDisplay) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.busyView = checkNotNull(busyView);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.permissionManager = checkNotNull(permissionManager);
        this.permissionScreener = checkNotNull(permissionScreener);
        this.settingsPresenter = checkNotNull(settingsPresenter);
        this.messages = checkNotNull(messages);
        this.errorDisplay = checkNotNull(errorDisplay);
        this.progressDisplay = checkNotNull(progressDisplay);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container,
                      @Nonnull EventBus eventBus) {
        busyView.setMessage(messages.sharing_settings_loading());
        container.setWidget(busyView);
        permissionScreener.checkPermission(
                EDIT_SHARING_SETTINGS.getActionId(),
                container, () -> displaySharingSettings(container));
    }



    private void displaySharingSettings(AcceptsOneWidget container) {
        dispatchServiceManager.execute(new GetProjectSharingSettingsAction(projectId), result -> {
            ProjectSharingSettings settings = result.getProjectSharingSettings();
            settingsPresenter.start(container);
            settingsPresenter.setSettingsTitle(messages.share());
            settingsPresenter.addSection(messages.sharing_settings_title()).setWidget(view);
            settingsPresenter.setApplySettingsHandler(this::applyChangesAndGoToNextPlace);
            settingsPresenter.setCancelSettingsHandler(this::cancelChangesAndGoToNextPlace);
            view.setLinkSharingPermission(settings.getLinkSharingPermission());
            view.setSharingSettings(settings.getSharingSettings());
        });
    }


    private void applyChangesAndGoToNextPlace() {
        settingsPresenter.goToNextPlace();
        ProjectSharingSettings settings = new ProjectSharingSettings(projectId, view.getLinkSharingPermission(), view.getSharingSettings());
        dispatchServiceManager.execute(new SetProjectSharingSettingsAction(settings), new DispatchServiceCallbackWithProgressDisplay<SetProjectSharingSettingsResult>(errorDisplay, progressDisplay) {
            @Override
            public void handleSuccess(SetProjectSharingSettingsResult result) {
                permissionManager.firePermissionsChanged();
            }

            @Override
            public String getProgressDisplayTitle() {
                return messages.sharing_settings_updating();
            }

            @Override
            public String getProgressDisplayMessage() {
                return "Please wait";
            }
        });
    }

    private void cancelChangesAndGoToNextPlace() {
        settingsPresenter.goToNextPlace();
    }

    public void setNextPlace(Optional<Place> nextPlace) {
        settingsPresenter.setNextPlace(nextPlace);
    }
}
