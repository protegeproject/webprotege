package edu.stanford.bmir.protege.web.server.app;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.project.ProjectComponentFactory;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jan 2018
 */
@Module
public class ServerModule {

    @Provides
    public ProjectComponentFactory provideProjectComponentFactory(ProjectComponentFactoryImpl impl) {
        return impl;
    }
}
