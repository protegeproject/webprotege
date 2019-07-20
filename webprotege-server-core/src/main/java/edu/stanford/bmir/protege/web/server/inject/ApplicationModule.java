package edu.stanford.bmir.protege.web.server.inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.AccessManagerImpl;
import edu.stanford.bmir.protege.web.server.access.RoleOracle;
import edu.stanford.bmir.protege.web.server.access.RoleOracleImpl;
import edu.stanford.bmir.protege.web.server.api.UserApiKeyStore;
import edu.stanford.bmir.protege.web.server.api.UserApiKeyStoreImpl;
import edu.stanford.bmir.protege.web.server.app.ApplicationDisposablesManager;
import edu.stanford.bmir.protege.web.server.app.ApplicationSettingsManager;
import edu.stanford.bmir.protege.web.server.util.DisposableObjectManager;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.auth.AuthenticationManager;
import edu.stanford.bmir.protege.web.server.auth.AuthenticationManagerImpl;
import edu.stanford.bmir.protege.web.server.collection.CollectionItemDataRepository;
import edu.stanford.bmir.protege.web.server.collection.CollectionItemDataRepositoryImpl;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.server.crud.obo.OBOIdSuffixEntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettingsConverter;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettingsRepository;
import edu.stanford.bmir.protege.web.server.crud.supplied.SuppliedNameSuffixEntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.server.crud.uuid.UUIDEntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchServiceExecutor;
import edu.stanford.bmir.protege.web.server.dispatch.impl.ActionHandlerRegistryImpl;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DispatchServiceExecutorImpl;
import edu.stanford.bmir.protege.web.server.download.DownloadGeneratorExecutor;
import edu.stanford.bmir.protege.web.server.download.FileTransferExecutor;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.server.mail.*;
import edu.stanford.bmir.protege.web.server.mansyntax.render.*;
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
import edu.stanford.bmir.protege.web.server.watches.WatchRecordRepository;
import edu.stanford.bmir.protege.web.server.watches.WatchRecordRepositoryImpl;
import edu.stanford.bmir.protege.web.server.webhook.SlackWebhookRepository;
import edu.stanford.bmir.protege.web.server.webhook.SlackWebhookRepositoryImpl;
import edu.stanford.bmir.protege.web.server.webhook.WebhookRepository;
import edu.stanford.bmir.protege.web.server.webhook.WebhookRepositoryImpl;
import edu.stanford.bmir.protege.web.shared.app.ApplicationSettings;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntityProvider;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
@Module
public class ApplicationModule {

    private static final int MAX_FILE_DOWNLOAD_THREADS = 5;



    @ApplicationSingleton
    @Provides
    public ObjectMapper provideObjectMapper(ObjectMapperProvider provider) {
        return provider.get();
    }

    @Provides
    @ApplicationSingleton
    public AuthenticationManager provideAuthenticationManager(AuthenticationManagerImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public ProjectDetailsManager provideProjectDetailsManager(ProjectDetailsManagerImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public ProjectPermissionsManager provideProjectPermissionsManager(ProjectPermissionsManagerImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public ProjectSharingSettingsManager provideProjectSharingSettingsManager(ProjectSharingSettingsManagerImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public UserDetailsManager provideUserDetailsManager(UserDetailsManagerImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public HasGetUserIdByUserIdOrEmail provideHasGetUserIdByUserIdOrEmail(UserDetailsManager manager) {
        return manager;
    }

    @ApplicationSingleton
    @Provides
    public PerspectiveLayoutStore providesPerspectiveLayoutStore(PerspectiveLayoutStoreImpl impl) {
        return impl;
    }

    @ApplicationSingleton
    @Provides
    public PerspectivesManager providesPerspectivesManager(PerspectivesManagerImpl impl) {
        return impl;
    }

    @Provides
    public HasUserIds providesHasUserIds() {
        return Collections::emptySet;
    }

    @Provides
    @ApplicationSingleton
    public ProjectManager provideOWLAPIProjectManager(ProjectCache projectCache, ProjectAccessManager projectAccessManager) {
        return new ProjectManager(projectCache, projectAccessManager);
    }

    @ApplicationSingleton
    @Provides
    public ProjectAccessManager provideProjectAccessManager(ProjectAccessManagerImpl projectAccessManager) {
        projectAccessManager.ensureIndexes();
        return projectAccessManager;
    }

    @Provides
    @ApplicationSingleton
    public ActionHandlerRegistry provideActionHandlerRegistry(ActionHandlerRegistryImpl impl) {
        return impl;
    }

    @Provides
    public DispatchServiceExecutor provideDispatchServiceExecutor(DispatchServiceExecutorImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    @ApplicationDataFactory
    public OWLDataFactory provideOWLDataFactory() {
        return new OWLDataFactoryImpl();
    }

    @Provides
    @ApplicationDataFactory
    @ApplicationSingleton
    public OWLEntityProvider provideOWLProvider(@ApplicationDataFactory OWLDataFactory dataFactory) {
        return dataFactory;
    }

    @ApplicationSingleton
    public UserActivityManager provideUserActivityManager(UserActivityManagerProvider provider) {
        return provider.get();
    }

    @Provides
    @ApplicationSingleton
    public WebProtegeProperties provideWebProtegeProperties(WebProtegePropertiesProvider povider) {
        return povider.get();
    }

    @Provides
    @MailProperties
    @ApplicationSingleton
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
    @ApplicationSingleton
    public ProjectEntityCrudKitSettingsRepository provideProjectEntityCrudKitSettingsRepository(
            MongoDatabase database, ProjectEntityCrudKitSettingsConverter converter) {
        return new ProjectEntityCrudKitSettingsRepository(database, converter);
    }

    @Provides
    @ApplicationSingleton
    public WatchRecordRepository provideWatchRecordRepository(WatchRecordRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public AccessManager provideAccessManager(AccessManagerImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public RoleOracle provideRoleOracle() {
        return RoleOracleImpl.get();
    }

    @Provides
    @DownloadGeneratorExecutor
    public ExecutorService provideDownloadGeneratorExecutorService() {
        // Might prove to be too much of a bottle neck.  For now, this limits the memory we need
        // to generate downloads
        return Executors.newSingleThreadExecutor(r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setName(thread.getName().replace("thread", "Download-Generator"));
            return thread;
        });
    }

    @Provides
    @FileTransferExecutor
    public ExecutorService provideFileTransferExecutorService() {
        return Executors.newFixedThreadPool(MAX_FILE_DOWNLOAD_THREADS, r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setName(thread.getName().replace("thread", "Download-Streamer"));
            return thread;
        });
    }

    @Provides
    public WebhookRepository providesWebhookRepository(WebhookRepositoryImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public SlackWebhookRepository provideSlackWebhookRepository(SlackWebhookRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public CollectionItemDataRepository provideCollectionElementDataRepository(CollectionItemDataRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    public ApplicationSettings provideApplicationSettings(ApplicationSettingsManager manager) {
        return manager.getApplicationSettings();
    }

    @Provides @IntoSet
    public EntityCrudKitPlugin<?,?,?> provideUUIDPlugin(UUIDEntityCrudKitPlugin plugin) {
        return plugin;
    }

    @Provides @IntoSet
    public EntityCrudKitPlugin<?,?,?> provideOBOIdPlugin(OBOIdSuffixEntityCrudKitPlugin plugin) {
        return plugin;
    }

    @Provides @IntoSet
    public EntityCrudKitPlugin<?,?,?> provideSuppliedNamePlugin(SuppliedNameSuffixEntityCrudKitPlugin plugin) {
        return plugin;
    }

    @Provides
    LiteralStyle provideDefaultLiteralStyle() {
        return LiteralStyle.REGULAR;
    }

    @Provides
    HttpLinkRenderer provideDefaultHttpLinkRenderer(DefaultHttpLinkRenderer renderer) {
        return renderer;
    }

    @Provides
    LiteralRenderer provideLiteralRenderer(MarkdownLiteralRenderer renderer) {
        return renderer;
    }

    @Provides
    ItemStyleProvider provideItemStyleProvider(DefaultItemStyleProvider provider) {
        return provider;
    }

    @Provides
    NestedAnnotationStyle provideNestedAnnotationStyle() {
        return NestedAnnotationStyle.COMPACT;
    }

    @ApplicationSingleton
    @Provides
    UserApiKeyStore provideUserApiKeyStore(UserApiKeyStoreImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @ApplicationSingleton
    @Provides
    ApplicationDisposablesManager provideApplicationDisposableObjectManager(DisposableObjectManager disposableObjectManager) {
        return new ApplicationDisposablesManager(disposableObjectManager);
    }

    @ApplicationSingleton
    @Provides
    BuiltInPrefixDeclarations provideBuiltInPrefixDeclarations(@Nonnull BuiltInPrefixDeclarationsLoader loader) {
        return loader.getBuiltInPrefixDeclarations();
    }

    @Provides
    @DormantProjectTime
    @ApplicationSingleton
    long providesProjectDormantTime(WebProtegeProperties properties) {
        return properties.getProjectDormantTime();
    }
}
