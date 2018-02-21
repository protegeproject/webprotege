package edu.stanford.bmir.protege.web.shared.app;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class GetApplicationSettingsResult implements Result {

    private ApplicationSettings applicationSettings;

    @GwtSerializationConstructor
    private GetApplicationSettingsResult() {
    }

    public GetApplicationSettingsResult(ApplicationSettings applicationSettings) {
        this.applicationSettings = checkNotNull(applicationSettings);
    }

    public ApplicationSettings getApplicationSettings() {
        return applicationSettings;
    }
}
