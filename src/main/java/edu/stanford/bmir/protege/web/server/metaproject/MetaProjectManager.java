package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectType;
import edu.stanford.bmir.protege.web.server.user.HasUserIds;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.permissions.GroupId;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.UnknownProjectException;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserRegistrationException;
import edu.stanford.smi.protege.server.metaproject.*;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * This interface assumes a particular implementation i.e. a Protege 3 implementation with Project object.
 * i.e. the openProject method.  Is this a problem?
 */
public final class MetaProjectManager implements HasUserIds, UserDetailsManager, ProjectPermissionsManager, AuthenticationManager, ProjectDetailsManager, ServerSettingsManager, ProjectSharingSettingsManager {



    private static MetaProjectManager instance;

    protected MetaProjectManager() {

    }

    public static synchronized MetaProjectManager getManager() {
        if (instance == null) {
            instance = WebProtegeInjector.get().getInstance(MetaProjectManager.class);
        }
        return instance;
    }

    private AuthenticationManager authenticationManager;

    private ProjectDetailsManager projectDetailsManager;

    private ProjectPermissionsManager projectPermissionsManager;

    private ProjectSharingSettingsManager projectSharingSettingsManager;

    private ServerSettingsManager serverSettingsManager;

    private UserDetailsManager userDetailsManager;

    private MetaProject metaProject;

    @Inject
    public MetaProjectManager(MetaProject metaProject,
                              AuthenticationManager authenticationManager,
                              ProjectDetailsManager projectDetailsManager,
                              ProjectPermissionsManager projectPermissionsManager,
                              ProjectSharingSettingsManager projectSharingSettingsManager,
                              ServerSettingsManager serverSettingsManager,
                              UserDetailsManager userDetailsManager) {
        this.metaProject = metaProject;
        this.authenticationManager = authenticationManager;
        this.projectDetailsManager = projectDetailsManager;
        this.projectPermissionsManager = projectPermissionsManager;
        this.projectSharingSettingsManager = projectSharingSettingsManager;
        this.serverSettingsManager = serverSettingsManager;
        this.userDetailsManager = userDetailsManager;
    }

    public MetaProject getMetaProject() {
        return metaProject;
    }

    public final void dispose() {
        metaProject.dispose();
        synchronized (this) {
            notifyAll();
        }

    }

    @Override
    public Set<GroupId> getUserGroups(UserId userId) {
        return projectPermissionsManager.getUserGroups(userId);
    }

    @Override
    public boolean isUserAdmin(UserId userId) {
        return projectPermissionsManager.isUserAdmin(userId);
    }

    @Override
    public boolean isProjectOwner(UserId userId, ProjectId projectId) {
        return projectDetailsManager.isProjectOwner(userId, projectId);
    }

    @Override
    public boolean hasValidCredentials(String userName, String password) {
        return projectPermissionsManager.hasValidCredentials(userName, password);
    }

    @Override
    public void changePassword(String userName, String password) {
        authenticationManager.changePassword(userName, password);
    }

    @Override
    public Collection<Operation> getAllowedOperations(String projectName, String userName) {
        return projectPermissionsManager.getAllowedOperations(projectName, userName);
    }

    @Override
    public PermissionsSet getPermissionsSet(ProjectId projectId, UserId userId) {
        return projectPermissionsManager.getPermissionsSet(projectId, userId);
    }

    @Override
    public Collection<Operation> getAllowedServerOperations(String userName) {
        return serverSettingsManager.getAllowedServerOperations(userName);
    }

    @Override
    public User getUser(String userNameOrEmail) {
        return userDetailsManager.getUser(userNameOrEmail);
    }

    @Override
    public void registerProject(ProjectId projectId, NewProjectSettings newProjectSettings) {
        projectDetailsManager.registerProject(projectId, newProjectSettings);
    }

    @Override
    public UserData registerUser(UserId userId, EmailAddress email, SaltedPasswordDigest password, Salt salt) throws UserRegistrationException {
        return authenticationManager.registerUser(userId, email, password, salt);
    }

    @Override
    public List<ProjectDetails> getListableReadableProjects(UserId userId) {
        return projectPermissionsManager.getListableReadableProjects(userId);
    }

    @Override
    public boolean allowsCreateUser() {
        return serverSettingsManager.allowsCreateUser();
    }

    @Override
    public ProjectSharingSettings getProjectSharingSettings(ProjectId projectId) {
        return projectSharingSettingsManager.getProjectSharingSettings(projectId);
    }

    @Override
    public void setProjectSharingSettings(ProjectSharingSettings projectSharingSettings) {
        projectSharingSettingsManager.setProjectSharingSettings(projectSharingSettings);
    }

    @Override
    public void applyDefaultSharingSettings(ProjectId projectId, UserId forUser) {
        projectSharingSettingsManager.applyDefaultSharingSettings(projectId, forUser);
    }

    public void setDigestedPassword(UserId userId, SaltedPasswordDigest saltedPasswordDigest, Salt salt) {
        authenticationManager.setDigestedPassword(userId, saltedPasswordDigest, salt);
    }

    public int getProjectCount() {
        return projectDetailsManager.getProjectCount();
    }

    public boolean isExistingProject(ProjectId projectId) {
        return projectDetailsManager.isExistingProject(projectId);
    }

    @Override
    public ProjectDetails getProjectDetails(ProjectId projectId) throws UnknownProjectException {
        return projectDetailsManager.getProjectDetails(projectId);
    }

    @Override
    public Collection<UserId> getUserIds() {
        return userDetailsManager.getUserIds();
    }

    public void setEmail(UserId userId, String email) {
        userDetailsManager.setEmail(userId, email);
    }

    @Override
    public UserDetails getUserDetails(UserId userId) {
        return userDetailsManager.getUserDetails(userId);
    }

    @Override
    public Optional<String> getEmail(UserId userId) {
        return userDetailsManager.getEmail(userId);
    }

    @Override
    public boolean isInTrash(ProjectId projectId) {
        return projectDetailsManager.isInTrash(projectId);
    }

    @Override
    public void setInTrash(ProjectId projectId, boolean b) {
        projectDetailsManager.setInTrash(projectId, b);
    }

    @Override
    public OWLAPIProjectType getType(ProjectId projectId) {
        return projectDetailsManager.getType(projectId);
    }

    @Override
    public void setType(ProjectId projectId, OWLAPIProjectType projectType) {
        projectDetailsManager.setType(projectId, projectType);
    }

    @Override
    public ProjectSettings getProjectSettings(ProjectId projectId) {
        return projectDetailsManager.getProjectSettings(projectId);
    }

    @Override
    public void setProjectSettings(ProjectSettings projectSettings) {
        projectDetailsManager.setProjectSettings(projectSettings);
    }

    @Override
    public Optional<Salt> getSalt(UserId userId) {
        return authenticationManager.getSalt(userId);
    }

    @Override
    public Optional<SaltedPasswordDigest> getSaltedPasswordDigest(UserId userId) {
        return authenticationManager.getSaltedPasswordDigest(userId);
    }
}