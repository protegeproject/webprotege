package edu.stanford.bmir.protege.web.shared.admin;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class GetAdminSettingsResult implements Result {

    private AdminSettings adminSettings;

    @GwtSerializationConstructor
    private GetAdminSettingsResult() {
    }

    public GetAdminSettingsResult(AdminSettings adminSettings) {
        this.adminSettings = checkNotNull(adminSettings);
    }

    public AdminSettings getAdminSettings() {
        return adminSettings;
    }
}
