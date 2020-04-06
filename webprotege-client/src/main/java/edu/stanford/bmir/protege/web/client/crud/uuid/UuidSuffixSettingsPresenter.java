package edu.stanford.bmir.protege.web.client.crud.uuid;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public class UuidSuffixSettingsPresenter {

    @Nonnull
    private final UuidSuffixSettingsView view;

    @Inject
    public UuidSuffixSettingsPresenter(@Nonnull UuidSuffixSettingsView view) {
        this.view = view;
    }

    public void start(@Nonnull AcceptsOneWidget widget) {
        widget.setWidget(view);
    }

    public void setSettings(@Nonnull UUIDSuffixSettings settings) {

    }

    public UUIDSuffixSettings getSettings() {
        return new UUIDSuffixSettings();
    }
}
