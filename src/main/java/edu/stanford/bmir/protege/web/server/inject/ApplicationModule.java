package edu.stanford.bmir.protege.web.server.inject;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchServiceExecutor;
import edu.stanford.bmir.protege.web.server.dispatch.impl.ActionHandlerRegistryImpl;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DispatchServiceExecutorImpl;
import edu.stanford.bmir.protege.web.server.dispatch.validators.*;
import edu.stanford.bmir.protege.web.server.logging.DefaultLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectCache;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.permissions.PermissionChecker;
import edu.stanford.bmir.protege.web.server.perspective.PerspectiveLayoutStore;
import edu.stanford.bmir.protege.web.server.perspective.PerspectiveLayoutStoreImpl;
import edu.stanford.bmir.protege.web.server.perspective.PerspectivesManager;
import edu.stanford.bmir.protege.web.server.perspective.PerspectivesManagerImpl;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.user.HasUserIds;

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
    ValidatorFactory<ReadPermissionValidator> providesReadPermissionValidatorFactory(PermissionChecker permissionChecker) {
        return (projectId1, userId) -> new ReadPermissionValidator(projectId1, userId, permissionChecker);
    }

    @Provides
    ValidatorFactory<CommentPermissionValidator> providesCommentPermissionValidatorFactory(PermissionChecker permissionChecker) {
        return (projectId1, userId) -> new CommentPermissionValidator(projectId1, userId, permissionChecker);
    }

    @Provides
    ValidatorFactory<WritePermissionValidator> providesWritePermissionValidatorFactory(PermissionChecker permissionChecker) {
        return (projectId1, userId) -> new WritePermissionValidator(projectId1, userId, permissionChecker);
    }

    @Provides
    ValidatorFactory<AdminPermissionValidator> providesAdminPermissionValidatorFactory(PermissionChecker permissionChecker,
                                                                                       ProjectDetailsManager projectDetailsManager) {
        return (projectId1, userId) -> new AdminPermissionValidator(projectId1,
                                                                    userId,
                                                                    permissionChecker,
                                                                    projectDetailsManager);
    }

    @Provides
    ValidatorFactory<UserIsProjectOwnerValidator> providesUserIsProjectOwnerValidatorFactory(ProjectDetailsManager projectDetailsManager) {
        return (projectId1, userId) -> new UserIsProjectOwnerValidator(projectId1, userId, projectDetailsManager);
    }


    @Provides
    @Singleton
    public OWLAPIProjectManager provideOWLAPIProjectManager(OWLAPIProjectCache projectCache) {
        return new OWLAPIProjectManager(projectCache);
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
}
