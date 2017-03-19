package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.server.admin.ApplicationSettingsManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2017
 */
public class ApplicationPathProvider {

    private final ApplicationSettingsManager manager;

    @Inject
    public ApplicationPathProvider(@Nonnull ApplicationSettingsManager manager) {
        this.manager = checkNotNull(manager);
    }

    @Nonnull
    public String getApplicationPath() {
        return manager.getApplicationSettings().getApplicationLocation().getPath();
    }
}
