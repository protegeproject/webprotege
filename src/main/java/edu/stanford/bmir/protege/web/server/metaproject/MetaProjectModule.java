package edu.stanford.bmir.protege.web.server.metaproject;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.auth.AuthenticationManager;
import edu.stanford.bmir.protege.web.server.auth.AuthenticationManagerImpl;
import edu.stanford.bmir.protege.web.server.permissions.PermissionChecker;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionsManager;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionsManagerImpl;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManagerImpl;
import edu.stanford.bmir.protege.web.server.project.ProjectExistsFilter;
import edu.stanford.bmir.protege.web.server.project.ProjectExistsFilterImpl;
import edu.stanford.bmir.protege.web.server.sharing.ProjectSharingSettingsManager;
import edu.stanford.bmir.protege.web.server.sharing.ProjectSharingSettingsManagerImpl;
import edu.stanford.bmir.protege.web.server.user.HasGetUserIdByUserIdOrEmail;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManagerImpl;

import javax.inject.Singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Module
public class MetaProjectModule {


    @Provides
    @Singleton
    public ProjectExistsFilter provideProjectExistsFilter(ProjectExistsFilterImpl impl) {
        return impl;
    }

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
    public PermissionChecker providePermissionChecker(ProjectPermissionsManager projectPermissionsManager) {
        return projectPermissionsManager;
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

//    @Provides
//    @Singleton
//    public HasUserIds provideHasUserIds() {
//
//    }

}
