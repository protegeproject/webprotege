package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class DbHostProvider implements Provider<Optional<String>> {

    private WebProtegeProperties webProtegeProperties;

    @Inject
    public DbHostProvider(WebProtegeProperties webProtegeProperties) {
        this.webProtegeProperties = webProtegeProperties;
    }

    @Override
    public Optional<String> get() {
        return webProtegeProperties.getDBHost();
    }
}
