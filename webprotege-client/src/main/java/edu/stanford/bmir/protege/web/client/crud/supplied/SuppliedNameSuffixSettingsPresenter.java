package edu.stanford.bmir.protege.web.client.crud.supplied;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public class SuppliedNameSuffixSettingsPresenter {

    @Nonnull
    private final SuppliedNameSuffixSettingsView view;

    @Inject
    public SuppliedNameSuffixSettingsPresenter(@Nonnull SuppliedNameSuffixSettingsView view) {
        this.view = checkNotNull(view);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void setSettings(@Nonnull SuppliedNameSuffixSettings settings) {

    }

    @Nonnull
    public SuppliedNameSuffixSettings getSettings() {
        return new SuppliedNameSuffixSettings();
    }
}
