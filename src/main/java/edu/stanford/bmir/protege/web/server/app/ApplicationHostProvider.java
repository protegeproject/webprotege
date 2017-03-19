package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.server.admin.AdminSettingsManager;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2017
 */
public class ApplicationHostProvider {

    private final AdminSettingsManager adminSettingsManager;

    @Inject
    public ApplicationHostProvider(AdminSettingsManager adminSettingsManager) {
        this.adminSettingsManager = adminSettingsManager;
    }

    public String getApplicationHost() {
        return adminSettingsManager.getAdminSettings().getApplicationLocation().getHost();
    }
}
