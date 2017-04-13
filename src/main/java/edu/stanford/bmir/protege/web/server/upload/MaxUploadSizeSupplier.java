package edu.stanford.bmir.protege.web.server.upload;

import edu.stanford.bmir.protege.web.server.admin.ApplicationSettingsManager;

import javax.inject.Inject;
import java.util.function.Supplier;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Apr 2017
 */
public class MaxUploadSizeSupplier implements Supplier<Long> {

    private final ApplicationSettingsManager applicationSettingsManager;

    @Inject
    public MaxUploadSizeSupplier(ApplicationSettingsManager applicationSettingsManager) {
        this.applicationSettingsManager = applicationSettingsManager;
    }

    /**
     * Gets the maximum upload size in bytes.
     * @return The maximum upload size in bytes.
     */
    @Override
    public Long get() {
        return applicationSettingsManager.getApplicationSettings().getMaxUploadSize();
    }
}
