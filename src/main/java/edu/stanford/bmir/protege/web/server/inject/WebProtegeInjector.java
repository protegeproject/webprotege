package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlersModule;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchModule;
import edu.stanford.bmir.protege.web.server.init.ConfigurationTasksModule;
import edu.stanford.bmir.protege.web.server.mail.MailModule;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectModule;

import javax.servlet.ServletContext;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class WebProtegeInjector {

    private static WebProtegeInjector instance;

    private final Injector injector;

    public static synchronized void init(ServletContext servletContext) {
        if(instance != null) {
            throw new RuntimeException("Already initialized!");
        }
        instance = new WebProtegeInjector(servletContext);
    }

    private WebProtegeInjector(ServletContext servletContext) {
        injector = Guice.createInjector(
                new ServletContextModule(servletContext),
                new WebProtegePropertiesModule(),
                new FileSystemConfigurationModule(),
                new ConfigurationTasksModule(),
                new MetaProjectModule(),
                new MailModule(),
                new DispatchModule(),
                new ActionHandlersModule());
    }

    public static synchronized WebProtegeInjector get() {
        if(instance == null) {
            throw new RuntimeException("Not initialized");
        }
        return instance;
    }

    public <T> T getInstance(Class<T> aClass) {
        return injector.getInstance(aClass);
    }
}
