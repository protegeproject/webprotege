package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.PermissionScreener;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.progress.BusyView;
import edu.stanford.bmir.protege.web.client.projectsettings.ProjectSettingsView;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_PROJECT_SETTINGS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2017
 */
public class ProjectSettingsPresenter {

    private final ProjectId projectId;

    private final ProjectSettingsView view;

    private final PermissionScreener permissionScreener;

    private final DispatchServiceManager dispatchServiceManager;

    private final BusyView busyView;

    private final EventBus eventBus;

    private final PlaceController placeController;

    private Optional<Place> nextPlace = Optional.empty();

    @Inject
    public ProjectSettingsPresenter(@Nonnull ProjectId projectId,
                                    @Nonnull ProjectSettingsView view,
                                    @Nonnull PermissionScreener permissionScreener,
                                    @Nonnull DispatchServiceManager dispatchServiceManager,
                                    @Nonnull BusyView busyView,
                                    @Nonnull EventBus eventBus,
                                    @Nonnull PlaceController placeController) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.placeController = checkNotNull(placeController);
        this.permissionScreener = checkNotNull(permissionScreener);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.busyView = checkNotNull(busyView);
        this.eventBus = checkNotNull(eventBus);
        view.setCancelButtonVisible(false);
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public void start(AcceptsOneWidget container) {
        permissionScreener.checkPermission(EDIT_PROJECT_SETTINGS.getActionId(),
                                           container,
                                           () -> displaySettings(container));
        view.setApplyChangesHandler(this::applySettings);
        view.setCancelChangesHandler(this::cancelChanges);
    }

    public void setNextPlace(Optional<Place> nextPlace) {
        this.nextPlace = nextPlace;
        view.setCancelButtonVisible(nextPlace.isPresent());
    }

    private void displaySettings(AcceptsOneWidget container) {
        busyView.setMessage("Retrieving project settings.");
        container.setWidget(busyView);
        dispatchServiceManager.execute(new GetProjectSettingsAction(projectId),
                                       result -> {
                                           view.setValue(result.getProjectSettings());
                                           container.setWidget(view);
                                       });
    }


    private void applySettings() {
        GWT.log("[ProjectSettingsPresenter] Applying project settings");
        Optional<ProjectSettings> projectSettings = view.getEditorValue();
        projectSettings.ifPresent(data -> {
            dispatchServiceManager.execute(new SetProjectSettingsAction(data), new DispatchServiceCallback<SetProjectSettingsResult>() {
                @Override
                public void handleSuccess(SetProjectSettingsResult setProjectSettingsResult) {
                    eventBus.fireEvent(new ProjectSettingsChangedEvent(data).asGWTEvent());
                    goToNextPlaceIfPresent();
                }
            });
        });
    }

    private void cancelChanges() {
        goToNextPlaceIfPresent();
    }

    private void goToNextPlaceIfPresent() {
        nextPlace.ifPresent(placeController::goTo);
    }
}
