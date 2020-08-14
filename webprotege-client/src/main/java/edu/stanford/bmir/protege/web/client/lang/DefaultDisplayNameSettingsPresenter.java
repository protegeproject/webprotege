package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;

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
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final DefaultDisplayNameSettingsView defaultDisplayNameSettingsView;

    @Nonnull
    private final DefaultLanguageTagView defaultLanguageTagView;

    @Inject
    public DefaultDisplayNameSettingsPresenter(@Nonnull SettingsPresenter settingsPresenter,
                                               @Nonnull DefaultDisplayNameSettingsView defaultDisplayNameSettingsView,
                                               @Nonnull DefaultLanguageTagView defaultLanguageTagView) {
        this.settingsPresenter = checkNotNull(settingsPresenter);
        this.defaultLanguageTagView = checkNotNull(defaultLanguageTagView);
        this.defaultDisplayNameSettingsView = checkNotNull(defaultDisplayNameSettingsView);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        settingsPresenter.start(container);
        settingsPresenter.setSettingsTitle("User Language Settings");
        settingsPresenter.addSection("Entity Display Name").setWidget(defaultDisplayNameSettingsView);
        settingsPresenter.addSection("New Entities").setWidget(defaultLanguageTagView);
    }
}
