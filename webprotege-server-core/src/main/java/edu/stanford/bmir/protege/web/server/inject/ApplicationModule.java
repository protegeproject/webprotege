package edu.stanford.bmir.protege.web.server.inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Ticker;
import com.google.common.collect.ImmutableList;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.AccessManagerImpl;
import edu.stanford.bmir.protege.web.server.access.RoleOracle;
import edu.stanford.bmir.protege.web.server.access.RoleOracleImpl;
import edu.stanford.bmir.protege.web.server.api.UserApiKeyStore;
import edu.stanford.bmir.protege.web.server.api.UserApiKeyStoreImpl;
import edu.stanford.bmir.protege.web.server.app.ApplicationDisposablesManager;
import edu.stanford.bmir.protege.web.server.app.ApplicationSettingsManager;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.auth.AuthenticationManager;
import edu.stanford.bmir.protege.web.server.auth.AuthenticationManagerImpl;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeRecordTranslator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeRecordTranslatorImpl;
import edu.stanford.bmir.protege.web.server.collection.CollectionItemDataRepository;
import edu.stanford.bmir.protege.web.server.collection.CollectionItemDataRepositoryImpl;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchServiceExecutor;
import edu.stanford.bmir.protege.web.server.dispatch.impl.ActionHandlerRegistryImpl;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DispatchServiceExecutorImpl;
import edu.stanford.bmir.protege.web.server.download.DownloadGeneratorExecutor;
import edu.stanford.bmir.protege.web.server.download.FileTransferExecutor;
import edu.stanford.bmir.protege.web.server.form.EntityFormRepository;
import edu.stanford.bmir.protege.web.server.form.EntityFormRepositoryImpl;
import edu.stanford.bmir.protege.web.server.form.EntityFormSelectorRepository;
import edu.stanford.bmir.protege.web.server.form.EntityFormSelectorRepositoryImpl;
import edu.stanford.bmir.protege.web.server.index.IndexUpdatingService;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.server.mail.*;
import edu.stanford.bmir.protege.web.server.mansyntax.render.*;
import edu.stanford.bmir.protege.web.server.owlapi.NonCachingDataFactory;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionsManager;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionsManagerImpl;
import edu.stanford.bmir.protege.web.server.perspective.*;
import edu.stanford.bmir.protege.web.server.project.*;
import edu.stanford.bmir.protege.web.server.search.EntitySearchFilterRepository;
import edu.stanford.bmir.protege.web.server.search.EntitySearchFilterRepositoryImpl;
import edu.stanford.bmir.protege.web.server.sharing.ProjectSharingSettingsManager;
import edu.stanford.bmir.protege.web.server.sharing.ProjectSharingSettingsManagerImpl;
import edu.stanford.bmir.protege.web.server.upload.*;
import edu.stanford.bmir.protege.web.server.user.*;
import edu.stanford.bmir.protege.web.server.util.DisposableObjectManager;
import edu.stanford.bmir.protege.web.server.viz.EntityGraphEdgeLimit;
import edu.stanford.bmir.protege.web.server.viz.EntityGraphSettingsRepository;
import edu.stanford.bmir.protege.web.server.viz.EntityGraphSettingsRepositoryImpl;
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
import java.util.concurrent.ScheduledExecutorService;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
@Module
public class ApplicationModule {

    private static final int MAX_FILE_DOWNLOAD_THREADS = 5;

    private static final int INDEX_UPDATING_THREADS = 10;


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
        return new NonCachingDataFactory(new OWLDataFactoryImpl());
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
    @ApplicationSingleton
    public ExecutorService provideDownloadGeneratorExecutorService(ApplicationExecutorsRegistry executorsRegistry) {
        // Might prove to be too much of a bottle neck.  For now, this limits the memory we need
        // to generate downloads
        var executor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setName(thread.getName().replace("thread", "Download-Generator"));
            return thread;
        });
        executorsRegistry.registerService(executor, "Download-Generator-Service");
        return executor;
    }

    @Provides
    @FileTransferExecutor
    @ApplicationSingleton
    public ExecutorService provideFileTransferExecutorService(ApplicationExecutorsRegistry executorsRegistry) {
        var executor = Executors.newFixedThreadPool(MAX_FILE_DOWNLOAD_THREADS, r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setName(thread.getName().replace("thread", "Download-Streamer"));
            return thread;
        });
        executorsRegistry.registerService(executor, "Download-Streaming-Service");
        return executor;
    }

    @Provides
    @IndexUpdatingService
    @ApplicationSingleton
    public ExecutorService provideIndexUpdatingExecutorService(ApplicationExecutorsRegistry executorsRegistry) {
        var executor = Executors.newFixedThreadPool(INDEX_UPDATING_THREADS, r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setName(thread.getName().replace("thread", "Index-Updater"));
            return thread;
        });
        executorsRegistry.registerService(executor, "Index-Updater");
        return executor;
    }

    @Provides
    @UploadedOntologiesCacheService
    @ApplicationSingleton
    public ScheduledExecutorService provideUploadedOntologiesCacheService(ApplicationExecutorsRegistry executorsRegistry) {
        var executor = Executors.newSingleThreadScheduledExecutor();
        executorsRegistry.registerService(executor, "Uploaded-Ontologies-Cache-Service");
        return executor;
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

    @Provides
    Ticker provideTicker() {
        return Ticker.systemTicker();
    }

    @Provides
    @ApplicationSingleton
    UploadedOntologiesCache provideUploadedOntologiesCache(UploadedOntologiesProcessor processor,
                                                           @UploadedOntologiesCacheService ScheduledExecutorService cacheService,
                                                           Ticker ticker,
                                                           ApplicationDisposablesManager disposableObjectManager) {
        var cache = new UploadedOntologiesCache(processor, ticker, cacheService);
        cache.start();
        disposableObjectManager.register(cache);
        return cache;
    }

    @Provides
    DocumentResolver provideDocumentResolver(DocumentResolverImpl impl) {
        return impl;
    }


    @Provides
    OntologyChangeRecordTranslator provideOntologyChangeRecordTranslator(OntologyChangeRecordTranslatorImpl impl) {
        return impl;
    }

    @Provides
    EntityFormRepository provideEntityFormRepository(EntityFormRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    @ApplicationSingleton
    EntityGraphSettingsRepository provideProjectEntityGraphSettingsRepository(
            EntityGraphSettingsRepositoryImpl impl) {
        return impl;
    }

    @Provides
    EntityFormSelectorRepository provideFormSelectorRepository(EntityFormSelectorRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    @EntityGraphEdgeLimit
    int provideEntityGraphEdgeLimit(WebProtegeProperties properties) {
        return properties.getEntityGraphEdgeLimit().orElse(3000);
    }

    @Provides
    @ApplicationSingleton
    EntitySearchFilterRepository provideEntitySearchFilterRepository(EntitySearchFilterRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    @ApplicationSingleton
    ImmutableList<BuiltInPerspective> provideBuiltInProjectPerspectives(BuiltInPerspectivesProvider builtInPerspectivesProvider) {
        return builtInPerspectivesProvider.getBuiltInPerspectives();
    }

    @Provides
    @ApplicationSingleton
    PerspectiveDescriptorRepository providePerspectiveDescriptorsRepository(PerspectiveDescriptorRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    @ApplicationSingleton
    PerspectiveLayoutRepository providePerspectiveLayoutsRepository(PerspectiveLayoutRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }
}
