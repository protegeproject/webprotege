package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.server.admin.AdminSettingsManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2017
 */
public class ApplicationHostSupplier {

    private final AdminSettingsManager adminSettingsManager;

    @Inject
    public ApplicationHostSupplier(@Nonnull AdminSettingsManager adminSettingsManager) {
        this.adminSettingsManager = adminSettingsManager;
    }

    public String getApplicationHost() {
        return adminSettingsManager.getAdminSettings().getApplicationLocation().getHost();
    }
}
