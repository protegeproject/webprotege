package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.AbstractModule;
import edu.stanford.bmir.protege.web.server.logging.DefaultLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
public class LoggingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(WebProtegeLogger.class).to(DefaultLogger.class).asEagerSingleton();
    }
}
