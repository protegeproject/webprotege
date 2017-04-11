package edu.stanford.bmir.protege.web.server.inject;

import com.mongodb.client.MongoDatabase;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.AccessManagerImpl;
import edu.stanford.bmir.protege.web.server.access.RoleOracle;
import edu.stanford.bmir.protege.web.server.access.RoleOracleImpl;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.auth.AuthenticationManager;
import edu.stanford.bmir.protege.web.server.auth.AuthenticationManagerImpl;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettingsConverter;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettingsRepository;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchServiceExecutor;
import edu.stanford.bmir.protege.web.server.dispatch.impl.ActionHandlerRegistryImpl;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DispatchServiceExecutorImpl;
import edu.stanford.bmir.protege.web.server.logging.DefaultLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.mail.*;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionsManager;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionsManagerImpl;
import edu.stanford.bmir.protege.web.server.perspective.PerspectiveLayoutStore;
import edu.stanford.bmir.protege.web.server.perspective.PerspectiveLayoutStoreImpl;
import edu.stanford.bmir.protege.web.server.perspective.PerspectivesManager;
import edu.stanford.bmir.protege.web.server.perspective.PerspectivesManagerImpl;
import edu.stanford.bmir.protege.web.server.project.*;
import edu.stanford.bmir.protege.web.server.sharing.ProjectSharingSettingsManager;
import edu.stanford.bmir.protege.web.server.sharing.ProjectSharingSettingsManagerImpl;
import edu.stanford.bmir.protege.web.server.user.*;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntityProvider;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryInternalsImplNoCache;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.Properties;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
@Module
public class ApplicationModule {

    @Provides
    @Singleton
    public AuthenticationManager provideAuthenticationManager(AuthenticationManagerImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public ProjectDetailsManager provideProjectDetailsManager(ProjectDetailsManagerImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public ProjectPermissionsManager provideProjectPermissionsManager(ProjectPermissionsManagerImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public ProjectSharingSettingsManager provideProjectSharingSettingsManager(ProjectSharingSettingsManagerImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public UserDetailsManager provideUserDetailsManager(UserDetailsManagerImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public HasGetUserIdByUserIdOrEmail provideHasGetUserIdByUserIdOrEmail(UserDetailsManager manager) {
        return manager;
    }

    @Singleton
    @Provides
    public PerspectiveLayoutStore providesPerspectiveLayoutStore(PerspectiveLayoutStoreImpl impl) {
        return impl;
    }

    @Singleton
    @Provides
    public PerspectivesManager providesPerspectivesManager(PerspectivesManagerImpl impl) {
        return impl;
    }

    @Provides
    public HasUserIds providesHasUserIds() {
        return () -> Collections.emptySet();
    }

    @Provides
    @Singleton
    public ProjectManager provideOWLAPIProjectManager(ProjectCache projectCache, ProjectAccessManager projectAccessManager) {
        return new ProjectManager(projectCache, projectAccessManager);
    }

    @Singleton
    @Provides
    public ProjectAccessManager provideProjectAccessManager(ProjectAccessManagerImpl projectAccessManager) {
        return projectAccessManager;
    }

    @Provides
    @Singleton
    public ActionHandlerRegistry provideActionHandlerRegistry(ActionHandlerRegistryImpl impl) {
        return impl;
    }

    @Provides
    public DispatchServiceExecutor provideDispatchServiceExecutor(DispatchServiceExecutorImpl impl) {
        return impl;
    }

    @Singleton
    @Provides
    public WebProtegeLogger provideWebProtegeLogger(DefaultLogger logger) {
        return logger;
    }

    @Provides
    @Singleton
    @ApplicationSingleton
    public OWLDataFactory provideOWLDataFactory() {
        return new OWLDataFactoryImpl(new OWLDataFactoryInternalsImplNoCache(false));
    }

    @Provides
    @Singleton
    @ApplicationSingleton
    public OWLEntityProvider provideOWLProvider(@ApplicationSingleton OWLDataFactory dataFactory) {
        return dataFactory;
    }

    @Singleton
    public UserActivityManager provideUserActivityManager(UserActivityManagerProvider provider) {
        return provider.get();
    }

    @Provides
    @Singleton
    public WebProtegeProperties provideWebProtegeProperties(WebProtegePropertiesProvider povider) {
        return povider.get();
    }

    @Provides
    @MailProperties
    @Singleton
    public Properties provideMailProperties(MailPropertiesProvider provider) {
        return provider.get();
    }

    @Provides
    public SendMail provideSendMail(SendMailImpl manager) {
        return manager;
    }

    @Provides
    public MessagingExceptionHandler provideMessagingExceptionHandler(MessagingExceptionHandlerImpl handler) {
        return handler;
    }

    @Provides
    @Singleton
    public ProjectEntityCrudKitSettingsRepository provideProjectEntityCrudKitSettingsRepository(
            MongoDatabase database, ProjectEntityCrudKitSettingsConverter converter) {
        return new ProjectEntityCrudKitSettingsRepository(database, converter);
    }

    @Provides
    @Singleton
    public AccessManager provideAccessManager(AccessManagerImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public RoleOracle provideRoleOracle() {
        return RoleOracleImpl.get();
    }
}
