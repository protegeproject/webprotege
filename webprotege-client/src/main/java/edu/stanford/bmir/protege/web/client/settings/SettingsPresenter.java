package edu.stanford.bmir.protege.web.client.settings;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Jul 2018
 */
public class SettingsPresenter {

    @Nonnull
    private final SettingsView view;

    @Nonnull
    private final Provider<SettingsSectionViewContainer> viewContainerProvider;

    @Nonnull
    private final List<SettingsSectionPresenter> sectionPresenters = new ArrayList<>();

    private final Set<ApplySettingsCallback> pendingApplySettingsCallbacks = new HashSet<>();

    private final Set<ApplySettingsCallback> failedApplySettingsCallbacks = new HashSet<>();

    @Inject
    public SettingsPresenter(@Nonnull SettingsView settingsView,
                             @Nonnull Provider<SettingsSectionViewContainer> viewContainerProvider) {
        this.view = checkNotNull(settingsView);
        this.viewContainerProvider = checkNotNull(viewContainerProvider);
    }

    public void start(@Nonnull AcceptsOneWidget container,
                      @Nonnull List<SettingsSectionPresenter> sectionPresenters) {
        this.sectionPresenters.clear();
        view.setApplyButtonHandler(this::handleApply);
        view.setApplyButtonHandler(this::handleCancel);
        // TODO: Set Project title
        view.clearView();
        sectionPresenters.clear();
        sectionPresenters.forEach(sectionPresenter -> {
            SettingsSectionViewContainer viewContainer = viewContainerProvider.get();
            viewContainer.setSectionName(sectionPresenter.getSectionName());
            view.addSectionViewContainer(viewContainer);
            sectionPresenter.start(viewContainer);
            sectionPresenters.add(sectionPresenter);
        });
        container.setWidget(view);
    }


    private void handleApply() {
        pendingApplySettingsCallbacks.clear();
        failedApplySettingsCallbacks.clear();
        view.setApplySettingsStarted();
        sectionPresenters.forEach(presenter -> {
            ApplySettingsCallback callback = new ApplySettingsCallback() {
                @Override
                public void settingsApplied() {
                    handleApplySettingsCallback(this);
                }

                @Override
                public void settingsNotApplied(@Nonnull String msgTitle, @Nonnull String msgBody) {
                    handleApplySettingsCallbackError(this, msgTitle, msgBody);
                }
            };
            pendingApplySettingsCallbacks.add(callback);
            presenter.handleApplySettings(callback);
        });
    }

    private void handleApplySettingsCallback(@Nonnull ApplySettingsCallback callback) {
        pendingApplySettingsCallbacks.remove(callback);
        if(pendingApplySettingsCallbacks.isEmpty()) {
            // All callbacks have been completed, whether successfully, or unsuccessfully
            view.setApplySettingsFinished();
            if(failedApplySettingsCallbacks.isEmpty()) {
                // If we don't have any failures then we can go to the next page.
                // If we do have failures then we don't do anything but stay on the
                // current page.
                goToNextPage();
            }
        }
    }

    private void handleApplySettingsCallbackError(@Nonnull ApplySettingsCallback callback,
                                                  @Nonnull String msgTitle,
                                                  @Nonnull String msgBody) {
        failedApplySettingsCallbacks.add(callback);
        pendingApplySettingsCallbacks.remove(callback);
        view.showErrorMessage(msgTitle, msgBody);
    }

    private void handleCancel() {
        goToNextPage();
    }

    private void goToNextPage() {

    }
}
