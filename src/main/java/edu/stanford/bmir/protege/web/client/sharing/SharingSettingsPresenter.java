package edu.stanford.bmir.protege.web.client.sharing;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.app.PermissionScreener;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.PermissionManager;
import edu.stanford.bmir.protege.web.client.progress.BusyView;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.GetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

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
    private final PlaceController placeController;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final ActiveProjectManager activeProjectManager;

    @Nonnull
    private final PermissionManager permissionManager;

    @Nonnull
    private final PermissionScreener permissionScreener;

    @Nonnull
    private Optional<Place> nextPlace = Optional.empty();

    @Inject
    public SharingSettingsPresenter(@Nonnull ProjectId projectId,
                                    @Nonnull SharingSettingsView view,
                                    @Nonnull BusyView busyView,
                                    @Nonnull PlaceController placeController,
                                    @Nonnull DispatchServiceManager dispatchServiceManager,
                                    @Nonnull ActiveProjectManager activeProjectManager,
                                    @Nonnull PermissionManager permissionManager,
                                    @Nonnull PermissionScreener permissionScreener) {
        this.projectId = projectId;
        this.view = view;
        this.busyView = busyView;
        this.placeController = placeController;
        this.dispatchServiceManager = dispatchServiceManager;
        this.activeProjectManager = activeProjectManager;
        this.permissionManager = permissionManager;
        this.permissionScreener = permissionScreener;
    }

    public Optional<Place> getNextPlace() {
        return nextPlace;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container,
                      @Nonnull EventBus eventBus) {
        busyView.setMessage("Loading sharing settings");
        container.setWidget(busyView);
        permissionScreener.checkPermission(
                EDIT_SHARING_SETTINGS.getActionId(),
                container, () -> displaySharingSettings(container));
    }



    private void displaySharingSettings(AcceptsOneWidget container) {
        dispatchServiceManager.execute(new GetProjectSharingSettingsAction(projectId), result -> {
            ProjectSharingSettings settings = result.getProjectSharingSettings();
            view.setApplyChangesHandler(this::applyChangesAndGoToNextPlace);
            view.setCancelHandler(this::cancelChangesAndGoToNextPlace);
            view.setLinkSharingPermission(settings.getLinkSharingPermission());
            view.setSharingSettings(settings.getSharingSettings());
            container.setWidget(view);
        });
        activeProjectManager.getActiveProjectDetails(projectDetails -> {
            String displayName = projectDetails.map(ProjectDetails::getDisplayName).orElse("");
            view.setProjectTitle(displayName);
        });
    }


    private void applyChangesAndGoToNextPlace() {
        nextPlace.ifPresent(placeController::goTo);
        ProjectSharingSettings settings = new ProjectSharingSettings(projectId, view.getLinkSharingPermission(), view.getSharingSettings());
        dispatchServiceManager.execute(new SetProjectSharingSettingsAction(settings), new DispatchServiceCallbackWithProgressDisplay<SetProjectSharingSettingsResult>() {
            @Override
            public void handleSuccess(SetProjectSharingSettingsResult result) {
                permissionManager.firePermissionsChanged();
            }

            @Override
            public String getProgressDisplayTitle() {
                return "Updating sharing settings";
            }

            @Override
            public String getProgressDisplayMessage() {
                return "Please wait";
            }
        });
    }

    private void cancelChangesAndGoToNextPlace() {
        nextPlace.ifPresent(placeController::goTo);
    }

    public void setNextPlace(Optional<Place> nextPlace) {
        this.nextPlace = nextPlace;
    }
}
