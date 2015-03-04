package edu.stanford.bmir.protege.web.server.inject;


import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class AdminEmailProvider implements Provider<Optional<String>> {

    private WebProtegeProperties webProtegeProperties;

    @Inject
    public AdminEmailProvider(WebProtegeProperties webProtegeProperties) {
        this.webProtegeProperties = webProtegeProperties;
    }

    @Override
    public Optional<String> get() {
        return webProtegeProperties.getAdministratorEmail();
    }
}
