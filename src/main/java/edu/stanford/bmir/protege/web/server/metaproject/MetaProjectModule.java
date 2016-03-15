package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import edu.stanford.bmir.protege.web.client.permissions.PermissionManager;
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
import edu.stanford.bmir.protege.web.server.user.HasUserIds;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManagerImpl;
import edu.stanford.smi.protege.server.metaproject.MetaProject;

import java.io.File;
import java.net.URI;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class MetaProjectModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProjectExistsFilter.class).to(ProjectExistsFilterImpl.class);
        bind(File.class).annotatedWith(MetaProjectFile.class).toProvider(MetaProjectFileProvider.class);
        bind(URI.class).annotatedWith(MetaProjectURI.class).toProvider(MetaProjectURIProvider.class);
        bind(MetaProject.class).toProvider(MetaProjectProvider.class).asEagerSingleton();
        bind(AuthenticationManager.class).to(AuthenticationManagerImpl.class).asEagerSingleton();
        bind(ProjectDetailsManager.class).to(ProjectDetailsManagerImpl.class).asEagerSingleton();
        bind(ProjectPermissionsManager.class).to(ProjectPermissionsManagerImpl.class).asEagerSingleton();
        bind(PermissionChecker.class).to(ProjectPermissionsManager.class);
        bind(ProjectSharingSettingsManager.class).to(ProjectSharingSettingsManagerImpl.class).asEagerSingleton();
        bind(UserDetailsManager.class).to(UserDetailsManagerImpl.class).asEagerSingleton();
        bind(HasGetUserIdByUserIdOrEmail.class).to(new TypeLiteral<UserDetailsManager>() {
        });
        bind(HasUserIds.class).to(UserDetailsManagerImpl.class).asEagerSingleton();
        bind(MetaProjectStore.class).asEagerSingleton();
    }
}
