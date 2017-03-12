package edu.stanford.bmir.protege.web.server.access;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Feb 2017
 */
@Module
public class AccessModule {

    @Provides
    @Singleton
    public AccessManager provideAccessManager(AccessManagerMongoDbImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public RoleOracle provideRoleOracle() {
        return RoleOracleImpl.get();
    }


}
