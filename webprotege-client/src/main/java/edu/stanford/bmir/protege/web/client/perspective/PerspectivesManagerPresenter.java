package edu.stanford.bmir.protege.web.client.perspective;

import com.google.common.collect.ImmutableList;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.SimpleEventBus;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDescriptor;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDetails;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-02
 */
public class PerspectivesManagerPresenter {

    @Nonnull
    private final PerspectivesManagerView view;

    @Nonnull
    private final PerspectiveDetailsListPresenter detailsListPresenter;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final PerspectivesManagerService perspectivesManagerService;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Inject
    public PerspectivesManagerPresenter(@Nonnull PerspectivesManagerView view,
                                        @Nonnull PerspectiveDetailsListPresenter detailsListPresenter,
                                        @Nonnull SettingsPresenter settingsPresenter,
                                        @Nonnull PerspectivesManagerService perspectivesManagerService,
                                        @Nonnull Messages messages,
                                        @Nonnull LoggedInUserProjectPermissionChecker permissionChecker) {
        this.view = checkNotNull(view);
        this.detailsListPresenter = detailsListPresenter;
        this.settingsPresenter = checkNotNull(settingsPresenter);
        this.perspectivesManagerService = checkNotNull(perspectivesManagerService);
        this.messages = checkNotNull(messages);
        this.permissionChecker = checkNotNull(permissionChecker);
    }

    public void setNextPlace(@Nonnull Place nextPlace) {
        settingsPresenter.setNextPlace(Optional.of(nextPlace));
    }


    public void start(@Nonnull AcceptsOneWidget container) {
        settingsPresenter.start(container);
        settingsPresenter.setApplySettingsHandler(this::handleApplySettings);
        settingsPresenter.setCancelSettingsHandler(this::handleCancelSettings);
        AcceptsOneWidget perspectiveDetailsContainer = settingsPresenter.addSection(messages.perspective_tabs());
        perspectiveDetailsContainer.setWidget(view);
        detailsListPresenter.start(view.getPerspectivesListContainer(), new SimpleEventBus());
        settingsPresenter.setBusy(true);
        refill();
    }

    private void handleApplySettings() {
        settingsPresenter.setBusy(true);
        ImmutableList<PerspectiveDescriptor> descriptors = detailsListPresenter.getValues()
                .stream()
                .map(PerspectiveDetails::toPerspectiveDescriptor)
                .collect(toImmutableList());
        perspectivesManagerService.savePerspectives(descriptors, () -> {
            settingsPresenter.setBusy(false);
            settingsPresenter.goToNextPlace();
        });
    }

    private void handleCancelSettings() {
        settingsPresenter.goToNextPlace();
    }

    private void refill() {
        perspectivesManagerService.getPerspectiveDetails(details -> {
            settingsPresenter.setBusy(false);
            detailsListPresenter.setValues(details);
        });
    }

}
