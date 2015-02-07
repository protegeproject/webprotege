package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.stanford.bmir.protege.web.server.init.ConfigurationTasksModule;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectModule;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class WebProtegeInjector {

    private static final WebProtegeInjector instance = new WebProtegeInjector();

    private final Injector injector;

    private WebProtegeInjector() {
        injector = Guice.createInjector(
                new FileSystemConfigurationModule(),
                new ConfigurationTasksModule(),
                new MetaProjectModule());
    }

    public static WebProtegeInjector get() {
        return instance;
    }

    public <T> T getInstance(Class<T> aClass) {
        return injector.getInstance(aClass);
    }
}
