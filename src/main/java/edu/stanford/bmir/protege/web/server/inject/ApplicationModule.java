package edu.stanford.bmir.protege.web.server.inject;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchServiceExecutor;
import edu.stanford.bmir.protege.web.server.dispatch.impl.ActionHandlerRegistryImpl;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DispatchServiceExecutorImpl;
import edu.stanford.bmir.protege.web.server.logging.DefaultLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.perspective.PerspectiveLayoutStore;
import edu.stanford.bmir.protege.web.server.perspective.PerspectiveLayoutStoreImpl;
import edu.stanford.bmir.protege.web.server.perspective.PerspectivesManager;
import edu.stanford.bmir.protege.web.server.perspective.PerspectivesManagerImpl;
import edu.stanford.bmir.protege.web.server.project.ProjectAccessManager;
import edu.stanford.bmir.protege.web.server.project.ProjectAccessManagerImpl;
import edu.stanford.bmir.protege.web.server.project.ProjectCache;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.user.HasUserIds;
import edu.stanford.bmir.protege.web.server.user.UserActivityManager;
import edu.stanford.bmir.protege.web.server.user.UserActivityManagerProvider;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntityProvider;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryInternalsImplNoCache;

import javax.inject.Singleton;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
@Module
public class ApplicationModule {


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

}
