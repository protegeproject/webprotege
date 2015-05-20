package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import edu.stanford.bmir.protege.web.server.auth.AuthenticationModule;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlersModule;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchModule;
import edu.stanford.bmir.protege.web.server.init.ConfigurationTasksModule;
import edu.stanford.bmir.protege.web.server.mail.MailModule;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectModule;
import edu.stanford.bmir.protege.web.server.project.ProjectManagerModule;

import javax.servlet.ServletContext;

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
                new WebProtegePropertiesModule(),
                new ProjectManagerModule(),
                new FileSystemConfigurationModule(),
                new ConfigurationTasksModule(),
                new MetaProjectModule(),
                new MailModule(),
                new DispatchModule(),
                new ActionHandlersModule(),
                new AuthenticationModule(),
                new LoggingModule(),
                new DbModule(),
                new RepositoryModule());
    }

    public static synchronized WebProtegeInjector get() {
        return instance;
    }

    public <T> T getInstance(Class<T> aClass) {
        return injector.getInstance(aClass);
    }

    public Injector createChildInjector(Module... modules) {
        return injector.createChildInjector(modules);
    }
}
