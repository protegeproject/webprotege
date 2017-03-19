package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.server.admin.ApplicationSettingsManager;
import edu.stanford.bmir.protege.web.shared.app.ApplicationScheme;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2017
 */
public class ApplicationSchemeProvider {

    @Nonnull
    private final ApplicationSettingsManager manager;

    @Inject
    public ApplicationSchemeProvider(@Nonnull ApplicationSettingsManager manager) {
        this.manager = checkNotNull(manager);
    }

    public ApplicationScheme getApplicationScheme() {
        return ApplicationScheme.valueOf(manager.getApplicationSettings().getApplicationLocation().getScheme().toUpperCase());
    }
}
