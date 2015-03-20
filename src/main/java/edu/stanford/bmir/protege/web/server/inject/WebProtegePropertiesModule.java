package edu.stanford.bmir.protege.web.server.inject;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;

import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class WebProtegePropertiesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(WebProtegeProperties.class).toProvider(WebProtegePropertiesProvider.class).asEagerSingleton();
        bind(String.class).annotatedWith(ApplicationName.class).toProvider(ApplicationNameProvider.class).asEagerSingleton();
        bind(String.class).annotatedWith(ApplicationHost.class).toProvider(ApplicationHostProvider.class).asEagerSingleton();
        bind(new TypeLiteral<Optional<String>>(){}).annotatedWith(AdminEmail.class).toProvider(AdminEmailProvider.class).asEagerSingleton();
    }
}
