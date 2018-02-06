package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class DbHostProvider implements Provider<String> {

    private WebProtegeProperties webProtegeProperties;

    @Inject
    public DbHostProvider(WebProtegeProperties webProtegeProperties) {
        this.webProtegeProperties = webProtegeProperties;
    }

    @Override
    public String get() {
        return webProtegeProperties.getDBHost().get();
    }
}
