package edu.stanford.bmir.protege.web.server.upload;

import edu.stanford.bmir.protege.web.server.app.ApplicationPreferencesStore;

import javax.inject.Inject;
import java.util.function.Supplier;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Apr 2017
 */
public class MaxUploadSizeSupplier implements Supplier<Long> {

    private final ApplicationPreferencesStore applicationPreferencesStore;

    @Inject
    public MaxUploadSizeSupplier(ApplicationPreferencesStore applicationPreferencesStore) {
        this.applicationPreferencesStore = applicationPreferencesStore;
    }

    /**
     * Gets the maximum upload size in bytes.
     * @return The maximum upload size in bytes.
     */
    @Override
    public Long get() {
        return applicationPreferencesStore.getApplicationPreferences().getMaxUploadSize();
    }
}
