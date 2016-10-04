package edu.stanford.bmir.protege.web.server.inject;

import dagger.Component;
import edu.stanford.bmir.protege.web.server.BioPortalAPIServiceImpl;
import edu.stanford.bmir.protege.web.server.OBOTextEditorServiceImpl;
import edu.stanford.bmir.protege.web.server.WebProtegeConfigurationChecker;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.auth.AuthenticationModule;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlersModule;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DispatchServiceImpl;
import edu.stanford.bmir.protege.web.server.filedownload.FileDownloadServlet;
import edu.stanford.bmir.protege.web.server.filesubmission.FileUploadServlet;
import edu.stanford.bmir.protege.web.server.init.ConfigurationTasksModule;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectComponent;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectModule;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.mail.MailModule;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectModule;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.owlapi.OntologyServiceOWLAPIImpl;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;

import javax.inject.Singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Component(modules = {
        ApplicationModule.class,
        WebProtegePropertiesModule.class,
        FileSystemConfigurationModule.class,
        ConfigurationTasksModule.class,
        MetaProjectModule.class,
        MailModule.class,
        ActionHandlersModule.class,
        AuthenticationModule.class,
        DbModule.class,
        RepositoryModule.class
})
@Singleton
public interface ApplicationComponent {

    WebProtegeProperties getWebProtegeProperties();

    WebProtegeConfigurationChecker getWebProtegeConfigurationChecker();

    UserDetailsManager getUserDetailsManager();

    ProjectComponent getProjectComponent(ProjectModule module);

    ServletComponent getServletComponent();
}
