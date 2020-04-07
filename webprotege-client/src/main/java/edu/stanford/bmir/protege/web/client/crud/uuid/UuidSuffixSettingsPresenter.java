package edu.stanford.bmir.protege.web.client.crud.uuid;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidFormat;

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

    public void setSettings(@Nonnull UuidSuffixSettings settings) {
        view.setFormat(settings.getUuidFormat());
    }

    public void clear() {
        view.setFormat(UuidFormat.BASE62);
    }

    public UuidSuffixSettings getSettings() {
        return UuidSuffixSettings.get(view.getFormat());
    }
}
