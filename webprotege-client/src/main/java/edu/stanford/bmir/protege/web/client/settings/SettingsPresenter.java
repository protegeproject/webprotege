package edu.stanford.bmir.protege.web.client.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.progress.BusyView;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Jul 2018
 */
public class SettingsPresenter implements HasBusy {

    @Nonnull
    private final SettingsView view;

    @Nonnull
    private final Provider<SettingsSectionViewContainer> viewContainerProvider;

    @Nonnull
    private final ActiveProjectManager activeProjectManager;

    @Nonnull
    private ApplySettingsHandler applySettingsHandler = () -> {};

    @Nonnull
    private CancelSettingsHandler cancelSettingsHandler = () -> {};

    @Nonnull
    private final PlaceController placeController;

    @Nonnull
    private Optional<Place> nextPlace = Optional.empty();

    @Inject
    public SettingsPresenter(@Nonnull SettingsView settingsView,
                             @Nonnull Provider<SettingsSectionViewContainer> viewContainerProvider,
                             @Nonnull ActiveProjectManager activeProjectManager,
                             @Nonnull BusyView busyView, PlaceController placeController) {
        this.view = checkNotNull(settingsView);
        this.viewContainerProvider = checkNotNull(viewContainerProvider);
        this.activeProjectManager = checkNotNull(activeProjectManager);
        this.placeController = checkNotNull(placeController);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        view.clearView();
        view.setApplySettingsHandler(this::handleApply);
        view.setCancelSettingsHandler(this::handleCancel);
        container.setWidget(view);
        activeProjectManager.getActiveProjectDetails(projectDetails -> {
            projectDetails.ifPresent(details -> view.setProjectTitle(details.getDisplayName()));
        });
    }

    public void setApplySettingsHandler(@Nonnull ApplySettingsHandler applySettingsHandler) {
        this.applySettingsHandler = checkNotNull(applySettingsHandler);
    }

    public void setCancelSettingsHandler(@Nonnull CancelSettingsHandler cancelSettingsHandler) {
        this.cancelSettingsHandler = checkNotNull(cancelSettingsHandler);
    }

    public void setCancelButtonVisible(boolean visible) {
        view.setCancelButtonVisible(visible);
    }

    public void setApplyButtonText(@Nonnull String text) {
        view.setApplyButtonText(checkNotNull(text));
    }

    public void setCancelButtonText(@Nonnull String text) {
        view.setCancelButtonText(text);
    }

    @Nonnull
    public AcceptsOneWidget addSection(@Nonnull String sectionName) {
        SettingsSectionViewContainer viewContainer = viewContainerProvider.get();
        viewContainer.setSectionName(checkNotNull(sectionName));
        view.addSectionViewContainer(viewContainer);
        return viewContainer;
    }

    private void handleApply() {
        GWT.log("[SettingsPresenter] Propagating ApplySettings");
        applySettingsHandler.handleApplySettings();

    }

    public void displayErrorMessage(@Nonnull String msgTitle,
                                    @Nonnull String msgBody) {
        view.showErrorMessage(msgTitle, msgBody);
    }

    private void handleCancel() {
        GWT.log("[SettingsPresenter] Handling Cancel");
        cancelSettingsHandler.handleCancelSettings();
        goToNextPlace();
    }

    public void setBusy(boolean busy) {
        view.setBusy(busy);
    }

    public void setNextPlace(@Nonnull Optional<Place> nextPlace) {
        GWT.log("[SettingsPresenter] setNextPlace: " + nextPlace);
        this.nextPlace = checkNotNull(nextPlace);
    }

    public void goToNextPlace() {
        nextPlace.ifPresent(placeController::goTo);
    }

    public void setSettingsTitle(@Nonnull String settingsTitle) {
        view.setSettingsTitle(settingsTitle);
    }
}
