package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.AbstractModule;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.logging.DefaultLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;

import javax.inject.Singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
@Module
public class LoggingModule {

    @Singleton
    @Provides
    public WebProtegeLogger provideWebProtegeLogger(DefaultLogger logger) {
        return logger;
    }
}
