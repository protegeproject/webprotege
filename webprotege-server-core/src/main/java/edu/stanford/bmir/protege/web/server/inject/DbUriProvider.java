package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-26
 */
public class DbUriProvider implements Provider<Optional<String>> {

    private WebProtegeProperties webProtegeProperties;

    @Inject
    public DbUriProvider(WebProtegeProperties webProtegeProperties) {
        this.webProtegeProperties = checkNotNull(webProtegeProperties);
    }

    @Override
    public Optional<String> get() {
        return this.webProtegeProperties.getDbUri();
    }
}
