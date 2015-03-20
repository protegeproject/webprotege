package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.AbstractModule;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class DbModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(DbHost.class).toProvider(DbHostProvider.class);
        bind(Integer.class).annotatedWith(DbPort.class).toProvider(DbPortProvider.class);
    }
}
