package edu.stanford.bmir.protege.web.server.inject;

import dagger.Component;
import edu.stanford.bmir.protege.web.server.app.WebProtegeConfigurationChecker;
import edu.stanford.bmir.protege.web.server.app.WebProtegeSessionListener;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.AccessModule;
import edu.stanford.bmir.protege.web.server.app.ApplicationNameSupplier;
import edu.stanford.bmir.protege.web.server.auth.AuthenticationModule;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlersModule;
import edu.stanford.bmir.protege.web.server.init.ConfigurationTasksModule;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectComponent;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectModule;
import edu.stanford.bmir.protege.web.server.mail.MailModule;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.inject.SharedApplicationModule;

import javax.inject.Singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Component(modules = {
        ApplicationModule.class,
        SharedApplicationModule.class,
        FileSystemConfigurationModule.class,
        ConfigurationTasksModule.class,
        MailModule.class,
        ActionHandlersModule.class,
        AuthenticationModule.class,
        AccessModule.class,
        DbModule.class,
        RepositoryModule.class,
})
@Singleton
@ApplicationSingleton
public interface ApplicationComponent {

    WebProtegeConfigurationChecker getWebProtegeConfigurationChecker();

    WebProtegeSessionListener getSessionListener();

    UserDetailsManager getUserDetailsManager();

    ProjectComponent getProjectComponent(ProjectModule module);

    ServletComponent getServletComponent();

    AccessManager getAccessManager();

    ApplicationNameSupplier getApplicationNameProvider();
}
