package edu.stanford.bmir.protege.web.server.inject;

import dagger.Module;
import dagger.Provides;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
@Module
public class DbModule {

    @Provides
    @DbHost
    public String provideDbHost(DbHostProvider dbHostProvider) {
        return dbHostProvider.get();
    }

    @Provides
    @DbPort
    public int provideDbPort(DbPortProvider dbPortProvider) {
        return dbPortProvider.get();
    }
}
