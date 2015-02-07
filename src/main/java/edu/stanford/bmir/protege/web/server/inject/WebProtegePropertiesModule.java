package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.AbstractModule;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class WebProtegePropertiesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(WebProtegeProperties.class).toProvider(WebProtegePropertiesProvider.class).asEagerSingleton();
        bind(String.class).annotatedWith(ApplicationName.class).toInstance(WebProtegeProperties.get().getApplicationName());
        bind(String.class).annotatedWith(ApplicationHost.class).toInstance(WebProtegeProperties.get().getApplicationHostName());
    }
}
