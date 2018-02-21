package edu.stanford.bmir.protege.web.server.app;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Supplier;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2017
 */
public class ApplicationHostSupplier implements Supplier<String> {

    private final ApplicationSettingsManager applicationSettingsManager;

    @Inject
    public ApplicationHostSupplier(@Nonnull ApplicationSettingsManager applicationSettingsManager) {
        this.applicationSettingsManager = applicationSettingsManager;
    }

    @Nonnull
    public String get() {
        return applicationSettingsManager.getApplicationSettings().getApplicationLocation().getHost();
    }
}
