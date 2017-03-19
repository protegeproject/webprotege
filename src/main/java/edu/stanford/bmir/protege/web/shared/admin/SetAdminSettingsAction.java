package edu.stanford.bmir.protege.web.shared.admin;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2017
 */
public class SetAdminSettingsAction implements Action<SetAdminSettingsResult> {

    private AdminSettings adminSettings;

    @GwtSerializationConstructor
    private SetAdminSettingsAction() {
    }

    public SetAdminSettingsAction(@Nonnull AdminSettings adminSettings) {
        this.adminSettings = checkNotNull(adminSettings);
    }

    public AdminSettings getAdminSettings() {
        return adminSettings;
    }
}
