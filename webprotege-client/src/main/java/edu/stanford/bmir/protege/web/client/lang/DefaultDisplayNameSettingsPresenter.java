package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public class DefaultDisplayNameSettingsPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final DefaultDisplayNameSettingsView defaultDisplayNameSettingsView;

    @Nonnull
    private final DefaultLanguageTagView defaultLanguageTagView;

    @Nonnull
    private Optional<Place> nextPlace = Optional.empty();

    @Inject
    public DefaultDisplayNameSettingsPresenter(@Nonnull ProjectId projectId,
                                               @Nonnull SettingsPresenter settingsPresenter,
                                               @Nonnull DispatchServiceManager dispatch,
                                               @Nonnull DefaultDisplayNameSettingsView defaultDisplayNameSettingsView, @Nonnull DefaultLanguageTagView defaultLanguageTagView) {
        this.projectId = checkNotNull(projectId);
        this.settingsPresenter = checkNotNull(settingsPresenter);
        this.dispatch = checkNotNull(dispatch);
        this.defaultLanguageTagView = checkNotNull(defaultLanguageTagView);
        this.defaultDisplayNameSettingsView = checkNotNull(defaultDisplayNameSettingsView);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        settingsPresenter.start(container);
        settingsPresenter.setSettingsTitle("User Language Settings");
        settingsPresenter.addSection("Entity Display Name").setWidget(defaultDisplayNameSettingsView);
        settingsPresenter.addSection("New Entities").setWidget(defaultLanguageTagView);
    }

    public void setNextPlace(@Nonnull Optional<Place> place) {
        this.nextPlace = checkNotNull(place);
    }

}
