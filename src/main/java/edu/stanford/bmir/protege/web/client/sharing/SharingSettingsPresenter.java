package edu.stanford.bmir.protege.web.client.sharing;

import com.google.common.base.Optional;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.assistedinject.Assisted;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.PermissionManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.*;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 01/03/16
 */
public class SharingSettingsPresenter {

    private final ProjectId projectId;

    private final SharingSettingsView view;

    private final PlaceController placeController;

    private final DispatchServiceManager dispatchServiceManager;

    private final PermissionManager permissionManager;

    private Optional<Place> nextPlace = Optional.absent();

    @Inject
    public SharingSettingsPresenter(@Assisted ProjectId projectId, SharingSettingsView view, PlaceController placeController, DispatchServiceManager dispatchServiceManager, PermissionManager permissionManager) {
        this.projectId = projectId;
        this.view = view;
        this.placeController = placeController;
        this.dispatchServiceManager = dispatchServiceManager;
        this.permissionManager = permissionManager;
    }

    public Optional<Place> getNextPlace() {
        return nextPlace;
    }

    public void start(AcceptsOneWidget container) {
        view.setApplyChangesHandler(new SharingSettingsView.ApplyChangesHandler() {
            @Override
            public void handleApplyChanges() {
                applyChangesAndGoToNextPlace();
            }
        });
        view.setCancelHandler(new SharingSettingsView.CancelHandler() {
            @Override
            public void handleCancel() {
                cancelChangesAndGoToNextPlace();
            }
        });
        dispatchServiceManager.execute(new GetProjectSharingSettingsAction(projectId), new DispatchServiceCallback<GetProjectSharingSettingsResult>() {
            @Override
            public void handleSuccess(GetProjectSharingSettingsResult result) {
                ProjectSharingSettings settings = result.getProjectSharingSettings();
                view.setLinkSharingPermission(settings.getLinkSharingPermission());
                view.setSharingSettings(settings.getSharingSettings());
            }
        });
        container.setWidget(view);
    }


    private void applyChangesAndGoToNextPlace() {
        if (nextPlace.isPresent()) {
            placeController.goTo(nextPlace.get());
        }
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
        if(nextPlace.isPresent()) {
            placeController.goTo(nextPlace.get());
        }
    }

    public void setNextPlace(Optional<Place> nextPlace) {
        this.nextPlace = nextPlace;
    }
}
