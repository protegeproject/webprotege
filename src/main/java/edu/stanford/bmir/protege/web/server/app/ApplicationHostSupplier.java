package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.server.admin.AdminSettingsManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Supplier;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2017
 */
public class ApplicationHostSupplier implements Supplier<String> {

    private final AdminSettingsManager adminSettingsManager;

    @Inject
    public ApplicationHostSupplier(@Nonnull AdminSettingsManager adminSettingsManager) {
        this.adminSettingsManager = adminSettingsManager;
    }

    @Nonnull
    public String get() {
        return adminSettingsManager.getAdminSettings().getApplicationLocation().getHost();
    }
}
