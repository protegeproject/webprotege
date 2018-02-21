package edu.stanford.bmir.protege.web.server.app;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.logging.DefaultLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.project.ProjectComponentFactory;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jan 2018
 */
@Module
public class ServerModule {


    @ApplicationSingleton
    @Provides
    public WebProtegeLogger provideWebProtegeLogger(DefaultLogger logger) {
        return logger;
    }


    @Provides
    public ProjectComponentFactory provideProjectComponentFactory(ProjectComponentFactoryImpl impl) {
        return impl;
    }
}
