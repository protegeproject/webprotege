package edu.stanford.bmir.protege.web.shared.app;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2017
 */
public class SetApplicationSettingsAction implements Action<SetApplicationSettingsResult> {

    private ApplicationSettings applicationSettings;

    @GwtSerializationConstructor
    private SetApplicationSettingsAction() {
    }

    public SetApplicationSettingsAction(@Nonnull ApplicationSettings applicationSettings) {
        this.applicationSettings = checkNotNull(applicationSettings);
    }

    public ApplicationSettings getApplicationSettings() {
        return applicationSettings;
    }
}
