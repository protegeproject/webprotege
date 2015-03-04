package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class ApplicationNameProvider implements Provider<String> {

    private WebProtegeProperties webProtegeProperties;

    @Inject
    public ApplicationNameProvider(WebProtegeProperties webProtegeProperties) {
        this.webProtegeProperties = webProtegeProperties;
    }

    @Override
    public String get() {
        return webProtegeProperties.getApplicationName();
    }
}
