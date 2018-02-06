package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class DbPortProvider implements Provider<Integer> {

    private WebProtegeProperties webProtegeProperties;

    @Inject
    public DbPortProvider(WebProtegeProperties webProtegeProperties) {
        this.webProtegeProperties = checkNotNull(webProtegeProperties);
    }

    @Override
    public Integer get() {
        Optional<String> dbPort = webProtegeProperties.getDBPort();
        return Integer.parseInt(dbPort.get());
    }
}
