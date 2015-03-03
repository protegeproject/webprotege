package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.shared.permissions.GroupId;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.*;

import javax.inject.Inject;
import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class ProjectPermissionsManagerImpl implements ProjectPermissionsManager {

    private static final String ADMIN_GROUP = "AdminGroup";

    private MetaProject metaProject;

    private ProjectDetailsManager projectDetailsManager;

    private ProjectExistsFilter projectExistsFilter;

    @Inject
    public ProjectPermissionsManagerImpl(MetaProject metaProject, ProjectDetailsManager projectDetailsManager, ProjectExistsFilter projectExistsFilter) {
        this.metaProject = metaProject;
        this.projectDetailsManager = projectDetailsManager;
        this.projectExistsFilter = projectExistsFilter;
    }

    @Override
    public boolean isUserAdmin(UserId userId) {
        if (userId.isGuest()) {
            return false;
        }
        User user = metaProject.getUser(userId.getUserName());
        if (user == null) {
            return false;
        }
        for (Group group : user.getGroups()) {
            if ("Admin".equals(group.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<GroupId> getUserGroups(UserId userId) {
        if(userId.isGuest()) {
            return Collections.emptySet();
        }
        final Set<GroupId> groups = new HashSet<>();
        User user = metaProject.getUser(userId.getUserName());
        for(Group group : user.getGroups()) {
            groups.add(GroupId.get(group.getName()));
        }
        return groups;
    }

    @Override
    public Collection<Operation> getAllowedOperations(String project, String userName) {
        User user = metaProject.getUser(userName);
        if(user == null) {
            return Collections.emptySet();
        }
        ProjectInstance projectInstance = metaProject.getProject(project);
        if (projectInstance == null) {
            return Collections.emptySet();
        }
        Policy policy = metaProject.getPolicy();
        Collection<Operation> allowedOps = new ArrayList<Operation>();
        for (Operation op : policy.getKnownOperations()) {
            if (policy.isOperationAuthorized(user, op, projectInstance)) {
                allowedOps.add(op);
            }
        }
        return allowedOps;
    }

    @Override
    public PermissionsSet getPermissionsSet(ProjectId projectId, UserId userId) {
        ProjectInstance pi = metaProject.getProject(projectId.getId());
        if(pi == null) {
            return PermissionsSet.emptySet();
        }
        User user = metaProject.getUser(userId.getUserName());
        if(user == null) {
            return PermissionsSet.emptySet();
        }
        PermissionsSet.Builder builder = PermissionsSet.builder();
        for(Operation operation : metaProject.getPolicy().getAllowedOperations(user, pi)) {
            builder.addPermission(Permission.getPermission(operation.getName()));
        }
        return builder.build();
    }

    @Override
    public boolean hasValidCredentials(String userName, String password) {
        User user = metaProject.getUser(userName);
        return user != null && user.verifyPassword(password);
    }

    @Override
    public List<ProjectDetails> getListableReadableProjects(UserId userId) {
        User user = metaProject.getUser(userId.getUserName());
        // Get groups is an expensive operation it seems!
        Collection<Group> userGroups = user.getGroups();
        List<ProjectDetails> result = new ArrayList<>();
        for (ProjectInstance projectInstance : metaProject.getProjects()) {
            final String name = projectInstance.getName();
            if (name != null && ProjectId.isWelFormedProjectId(name)) {
                final ProjectId projectId = ProjectId.get(name);
                if (isAuthorisedToReadAndList(user, projectInstance, userGroups)) {
                    if (projectExistsFilter.isProjectPresent(projectId)) {
                        ProjectDetails projectDetails = projectDetailsManager.getProjectDetails(projectId);
                        result.add(projectDetails);
                    }
                }
            }
        }
        return result;
    }


    private boolean isUserOwner(User user, User owner) {
        return user != null && owner != null && owner.equals(user);
    }

    private boolean isAuthorisedToRead(User user, ProjectInstance projectInstance, Collection<Group> userGroups) {
        if (user == null) {
            return isWorldAllowedOperation(projectInstance, MetaProjectConstants.OPERATION_READ);
        } else
            return isAdminUser(user, userGroups) || isOperationAuthorised(projectInstance, userGroups, MetaProjectConstants.OPERATION_READ);
    }

    private boolean isAdminUser(User user, Collection<Group> userGroups) {
        if (user == null) {
            return false;
        }
        for (Group group : userGroups) {
            if (ADMIN_GROUP.equals(group.getName())) {
                return true;
            }
        }
        return false;
    }


    private boolean isAuthorisedToDisplayInList(User user, ProjectInstance projectInstance, Collection<Group> userGroups) {
        Operation operation = MetaProjectConstants.OPERATION_DISPLAY_IN_PROJECT_LIST;
        if (user == null) {
            return isWorldAllowedOperation(projectInstance, operation);
        }
        else {
            return isOperationAuthorised(projectInstance, userGroups, operation);
        }

    }

    private boolean isOperationAuthorised(ProjectInstance projectInstance, Collection<Group> userGroups, Operation operation) {
        for(GroupOperation groupOperation : projectInstance.getAllowedGroupOperations()) {
            if(groupOperation.getAllowedOperations().contains(operation)) {
                if(userGroups.contains(groupOperation.getAllowedGroup())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isWorldAllowedOperation(ProjectInstance projectInstance, Operation operation) {
        for (GroupOperation groupOperation : projectInstance.getAllowedGroupOperations()) {
            Group group = groupOperation.getAllowedGroup();
            String groupName = group.getName();
            if (MetaProjectConstants.USER_WORLD.equals(groupName)) {
                Set<Operation> operations = groupOperation.getAllowedOperations();
                if (operations.contains(operation)) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean isAuthorisedToReadAndList(User user, ProjectInstance projectInstance, Collection<Group> userGroups) {
        User owner = projectInstance.getOwner();
        return isUserOwner(user, owner) || isAuthorisedToDisplayInList(user, projectInstance, userGroups) && isAuthorisedToRead(user, projectInstance, userGroups);
    }
}
