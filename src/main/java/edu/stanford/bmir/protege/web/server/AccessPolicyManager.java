package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.data.AccessPolicyUserData;
import edu.stanford.bmir.protege.web.client.ui.constants.OntologyShareAccessConstants;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.server.metaproject.*;
import edu.stanford.smi.protege.server.metaproject.impl.GroupOperationImpl;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;
import edu.stanford.smi.protege.server.metaproject.impl.WrappedProtegeInstanceImpl;

import java.util.*;

/**
 * The code in this class was written by contractors - it is UNBELIEVABLY BAD and needs replacing.
 */
public class AccessPolicyManager {
    //
    public static final WebProtegeLogger LOGGER = WebProtegeLoggerManager.get(AccessPolicyManager.class);


    private static final AccessPolicyManager instance = new AccessPolicyManager();

    //
    public static AccessPolicyManager get() {
        return instance;
    }

    private AccessPolicyManager() {
    }

    private MetaProject getMetaProject() {
        return MetaProjectManager.getManager().getMetaProject();
    }

    private ProjectInstance getProjectInstance(ProjectId projectId, MetaProject metaproject) {
        return metaproject.getProject(projectId.getId());
    }

    public String getOwner(ProjectId projectId) {
        ProjectInstance projectInstance = getProjectInstance(projectId);
        if (projectInstance == null) {
            return null;
        }

        User owner = projectInstance.getOwner();
        return owner.getName();
    }

    private ProjectInstance getProjectInstance(ProjectId projectId) {
        MetaProject metaproject = getMetaProject();
        return getProjectInstance(projectId, metaproject);
    }


    public boolean canManageProject(ProjectId projectId, UserId userId) {
        if (projectId == null || userId == null) {
            return false;
        }
        try {
            ProjectInstance projectInstance = getProjectInstance(projectId);

            if (projectInstance == null) {
                return false;
            }
            User owner = projectInstance.getOwner();

            if (owner.getName().equalsIgnoreCase(userId.getUserName())) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.severe(e);
        }
        return false;
    }

    public List<String> getUsers() {
        Set<User> users = getMetaProject().getUsers();
        List<String> userNameList = new ArrayList<String>();
        if (users == null) {
            return userNameList;
        }
        for (User user : users) {
            if (user.getName() != null) {
                userNameList.add(user.getName());
            }
        }
        return userNameList;
    }

    public void addReadPermission(ProjectId projectId, List<String> userNameList) {
        if (userNameList == null || projectId == null) {
            return;
        }

        MetaProject metaProject = getMetaProject();
        Operation readOnlyOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_READ_ONLY_ACCESS_OPERATION);
        ProjectInstance projectInstance = getProjectInstance(projectId, metaProject);

        removeDefaultPolicyIfExists(projectInstance, readOnlyOperation);

        Group group = metaProject.getGroup(projectId.getId() + OntologyShareAccessConstants.ONTOLOGY_READERS_GROUP_SUFFIX);
        if (group == null) {
            group = metaProject.createGroup(projectId.getId() + OntologyShareAccessConstants.ONTOLOGY_READERS_GROUP_SUFFIX);
        }
        if (readOnlyOperation == null) {
            readOnlyOperation = metaProject.createOperation(OntologyShareAccessConstants.PROJECT_READ_ONLY_ACCESS_OPERATION);

        }

//        User serverUser = metaProject.getUser(WebProtegeProperties.getProtegeServerUser());
//        //Checking if the server user mentioned in webprotege.user in protege.properties is present in users readers group. If not then adds it.
//        //The server user is required while loading the project and should have read access.
//        if (serverUser != null && !group.getMembers().contains(serverUser)) {
//            Set<User> members = group.getMembers();
//            members.add(serverUser);
//            group.setMembers(members);
//        }

        if (doesProjectContainsGroupAndOperation(projectInstance, group, readOnlyOperation)) {
            addPermission(metaProject, userNameList, projectInstance, group, readOnlyOperation, true);
        } else {
            addPermission(metaProject, userNameList, projectInstance, group, readOnlyOperation, false);
        }

    }

    synchronized boolean doesProjectContainsGroupAndOperation(ProjectInstance projectInstance, Group group, Operation operation) {

        for (GroupOperation groupOperation : projectInstance.getAllowedGroupOperations()) {
            if (groupOperation.getAllowedGroup().equals(group) && groupOperation.getAllowedOperations().contains(operation)) {

                return true;
            }
        }
        return false;

    }

    synchronized void addPermission(MetaProject metaProject, List<String> userNameList, ProjectInstance projectInstance, Group group, Operation operation, boolean doesProjectContainsGroup) {

        if (doesProjectContainsGroup) {
            for (GroupOperation groupOperation : projectInstance.getAllowedGroupOperations()) {
                if (groupOperation.getAllowedGroup().equals(group) && groupOperation.getAllowedOperations().contains(operation)) {
                    for (String userName : userNameList) {
                        User user = getMetaProject().getUser(userName);
                        group.addMember(user);
                    }
                    return;
                }
            }
        } else {
            for (String userName : userNameList) {
                User user = getMetaProject().getUser(userName);
                group.addMember(user);
            }
            GroupOperation groupOperation = metaProject.createGroupOperation();
            groupOperation.setAllowedGroup(group);
            groupOperation.addAllowedOperation(operation);
            projectInstance.addAllowedGroupOperations(groupOperation);

        }
    }

    public void removeReadPermission(ProjectId projectId, List<String> userNameList) {

        if (userNameList == null || projectId == null) {
            return;
        }

        MetaProject metaProject = getMetaProject();
        Operation readOnlyOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_READ_ONLY_ACCESS_OPERATION);
        if (readOnlyOperation == null) {
            return;
        }
        ProjectInstance projectInstance = getProjectInstance(projectId, metaProject);
        Group group = metaProject.getGroup(projectId.getId() + OntologyShareAccessConstants.ONTOLOGY_READERS_GROUP_SUFFIX);
        if (doesProjectContainsGroupAndOperation(projectInstance, group, readOnlyOperation)) {
            removePermission(metaProject, userNameList, projectInstance, group, readOnlyOperation);
        }
    }

    synchronized void removePermission(MetaProject metaProject, List<String> userNameList, ProjectInstance projectInstance, Group group, Operation operation) {
        for (GroupOperation groupOperation : projectInstance.getAllowedGroupOperations()) {
            if (groupOperation.getAllowedGroup().equals(group) && groupOperation.getAllowedOperations().contains(operation)) {

                Set<User> members = group.getMembers();
                for (String userName : userNameList) {
                    User user = getMetaProject().getUser(userName);
                    members.remove(user);

                }
                group.setMembers(members);
                break;
            }
        }
    }

    public void addWritePermission(ProjectId projectId, List<String> userNameList) {
        if (userNameList == null || projectId == null) {
            return;
        }

        MetaProject metaProject = getMetaProject();
        Operation writeOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_WRITE_ACCESS_OPERATION);
        ProjectInstance projectInstance = getProjectInstance(projectId, metaProject);

        removeDefaultPolicyIfExists(projectInstance, writeOperation);

        Group group = metaProject.getGroup(projectId.getId() + OntologyShareAccessConstants.ONTOLOGY_WRITERS_GROUP_SUFFIX);
        if (group == null) {
            group = metaProject.createGroup(projectId.getId() + OntologyShareAccessConstants.ONTOLOGY_WRITERS_GROUP_SUFFIX);
        }
        if (writeOperation == null) {
            writeOperation = metaProject.createOperation(OntologyShareAccessConstants.PROJECT_WRITE_ACCESS_OPERATION);

        }
        if (doesProjectContainsGroupAndOperation(projectInstance, group, writeOperation)) {
            addPermission(metaProject, userNameList, projectInstance, group, writeOperation, true);
        } else {
            addPermission(metaProject, userNameList, projectInstance, group, writeOperation, false);
        }
    }

    public void removeWritePermission(ProjectId projectId, List<String> userNameList) {
        if (userNameList == null || projectId == null) {
            return;
        }

        MetaProject metaProject = getMetaProject();
        Operation writeOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_WRITE_ACCESS_OPERATION);
        if (writeOperation == null) {
            return;
        }
        ProjectInstance projectInstance = getProjectInstance(projectId, metaProject);
        Group group = metaProject.getGroup(projectId.getId() + OntologyShareAccessConstants.ONTOLOGY_WRITERS_GROUP_SUFFIX);
        if (doesProjectContainsGroupAndOperation(projectInstance, group, writeOperation)) {
            removePermission(metaProject, userNameList, projectInstance, group, writeOperation);
        }

    }

    public Collection<AccessPolicyUserData> getUsersWithReadOnlyAccess(ProjectId projectId) {
        return getUsersWithReadOnlyAccess(projectId, false);
    }

    // TODO !!!! WHY ARE THERE EMPTY CATCH BLOCKS HERE?
    public Collection<AccessPolicyUserData> getUsersWithReadOnlyAccess(ProjectId projectId, boolean includeTemporaryAccount) {
        try {
            MetaProject metaProject = getMetaProject();

            Collection<AccessPolicyUserData> userList = new TreeSet<AccessPolicyUserData>(new AccessPolicyUserData());
            ProjectInstance projectInstance = getProjectInstance(projectId, metaProject);
            Operation readOnlyOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_READ_ONLY_ACCESS_OPERATION);
            Set<GroupOperation> groupOperations = projectInstance.getAllowedGroupOperations();
            Set<User> allPoliciesUsers = new HashSet<User>();

            if (isUsersFromDefaultPolicy(groupOperations, readOnlyOperation)) {
                AccessPolicyUserData userEveryBodyUserData = new AccessPolicyUserData();
                userEveryBodyUserData.setName(OntologyShareAccessConstants.USER_EVERYBODY_NAME);
                userList.add(userEveryBodyUserData);
                return userList;
            }

            if (groupOperations != null) {
                for (GroupOperation groupOperation1 : groupOperations) {
                    try {
                        GroupOperation groupOperation = groupOperation1;
                        Group group = groupOperation.getAllowedGroup();
                        if (group.getMembers() != null) {
                            allPoliciesUsers.addAll(group.getMembers());
                        }
                    } catch (Exception e) {
                    }
                }
            }
            Group group = metaProject.getGroup(projectId.getId() + OntologyShareAccessConstants.ONTOLOGY_READERS_GROUP_SUFFIX);// add
            return userList;
        } catch (Exception e) {
        }
        return null;
    }

    public Collection<AccessPolicyUserData> getUsersWithWriteAccess(ProjectId projectId) {
        return getUsersWithWriteAccess(projectId, false);
    }

    /**
     * Retrieves uses having write access for the particular project
     *
     * @param projectId
     * @param includeTemporaryAccount
     * @return
     */
    public Collection<AccessPolicyUserData> getUsersWithWriteAccess(ProjectId projectId, boolean includeTemporaryAccount) {

        try {
            MetaProject metaProject = getMetaProject();

            Collection<AccessPolicyUserData> userList = new TreeSet<AccessPolicyUserData>(new AccessPolicyUserData());
            ProjectInstance projectInstance = getProjectInstance(projectId, metaProject);
            Policy policy = metaProject.getPolicy();
            Operation writeOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_WRITE_ACCESS_OPERATION);
            Set<GroupOperation> groupOperations = projectInstance.getAllowedGroupOperations();
            Set<User> allPoliciesUsers = new HashSet<User>();

            if (isUsersFromDefaultPolicy(groupOperations, writeOperation)) {
                AccessPolicyUserData userEveryBodyUserData = new AccessPolicyUserData();
                userEveryBodyUserData.setName(OntologyShareAccessConstants.USER_EVERYBODY_NAME);
                userList.add(userEveryBodyUserData);
                return userList;
            }

            if (groupOperations != null) {
                for (GroupOperation groupOperation1 : groupOperations) {
                    try {
                        GroupOperation groupOperation = groupOperation1;
                        Group group = groupOperation.getAllowedGroup();
                        if (group.getMembers() != null) {
                            allPoliciesUsers.addAll(group.getMembers());
                        }
                    } catch (Exception e) {
                    }

                }
            }
            Group group = metaProject.getGroup(projectId.getId() + OntologyShareAccessConstants.ONTOLOGY_WRITERS_GROUP_SUFFIX);
            List<String> groupUserList = getUserListForGroupOperation(projectInstance, group, writeOperation);
            for (User user : allPoliciesUsers) {
                if (user.getName() != null && policy.isOperationAuthorized(user, writeOperation, projectInstance)) {
                    AccessPolicyUserData userData = new AccessPolicyUserData();
                    userData.setName(user.getName());
                    if (groupUserList.contains(user.getName())) {
                        userData.setPartofWriters(true);
                    }
                    try {
                        userList.add(userData);
                    } catch (Exception e) {
                    }
                }
            }
            return userList;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Retrieves users list from group operations as list of string.
     *
     * @param projectInstance
     * @param group
     * @param operation
     * @return
     */
    synchronized List<String> getUserListForGroupOperation(ProjectInstance projectInstance, Group group, Operation operation) {
        List<String> usersNameList = new ArrayList<String>();
        Set<GroupOperation> groupOperations = projectInstance.getAllowedGroupOperations();
        if (group != null && groupOperations != null) {
            for (GroupOperation groupOperation : groupOperations) {
                if (groupOperation.getAllowedGroup().equals(group) && groupOperation.getAllowedOperations().contains(operation)) {
                    Set<User> users = group.getMembers();
                    for (User user : users) {
                        usersNameList.add(user.getName());
                    }
                    break;
                }
            }
        }
        return usersNameList;

    }

    /**
     * Checks if the users is from default policy
     *
     * @param groupOperations
     * @param operation
     * @return
     */
    synchronized boolean isUsersFromDefaultPolicy(Set<GroupOperation> groupOperations, Operation operation) {

        for (GroupOperation groupOperation : groupOperations) {
            Group group = groupOperation.getAllowedGroup();
            if (group.getName().equalsIgnoreCase(OntologyShareAccessConstants.WORLD_GROUP_NAME) && groupOperation.getAllowedOperations().contains(operation)) {
                return true;
            }
        }

        return false;
    }

    synchronized void removeDefaultPolicyIfExists(ProjectInstance projectInstance, Operation operation) {
        Set<GroupOperation> groupOperations = projectInstance.getAllowedGroupOperations();
        for (GroupOperation groupOperation : groupOperations) {
            Group group = groupOperation.getAllowedGroup();
            if (group.getName().equalsIgnoreCase(OntologyShareAccessConstants.WORLD_GROUP_NAME) && groupOperation.getAllowedOperations().contains(operation)) {
                groupOperations.remove(groupOperation);
                projectInstance.setAllowedGroupOperations(groupOperations);

                Instance groupOpInst1 = (((WrappedProtegeInstanceImpl) groupOperation).getProtegeInstance());
                Instance cloneGroupOpInst = (Instance) groupOpInst1.shallowCopy(null, null);

                GroupOperation cloneGroupOp = new GroupOperationImpl((MetaProjectImpl) projectInstance.getMetaProject(), cloneGroupOpInst);
                Set<Operation> cloneGroupAllowedOp = cloneGroupOp.getAllowedOperations();
                for (Operation op : cloneGroupAllowedOp) {
                    if (op.equals(operation)) {
                        cloneGroupAllowedOp.remove(op);
                        break;
                    }
                }
                if (cloneGroupAllowedOp.size() > 0) {
                    cloneGroupOp.setAllowedOperations(cloneGroupAllowedOp);
                    projectInstance.addAllowedGroupOperations(cloneGroupOp);

                }
                updateProjectOwnerPolicy(projectInstance);

                return;
            }
        }
    }

    synchronized void updateProjectOwnerPolicy(ProjectInstance projectInstance) {
        User owner = projectInstance.getOwner();
        MetaProject metaProject = projectInstance.getMetaProject();
        if (owner == null || owner.getName() == null) {
            return;
        }
        Group group = metaProject.getGroup(OntologyShareAccessConstants.OWNER_GROUP_PREFIX + owner.getName());
        if (group == null) {
            group = metaProject.createGroup(OntologyShareAccessConstants.OWNER_GROUP_PREFIX + owner.getName());
        }
        if (!group.getMembers().contains(owner)) {
            Set<User> mem = group.getMembers();
            mem.clear();
            mem.add(owner);
            group.setMembers(mem);
        }

        updateProjectOwnerPolicyIfExists(projectInstance, group, metaProject);
    }

    synchronized void updateProjectOwnerPolicyIfExists(ProjectInstance projectInstance, Group group, MetaProject metaProject) {
        Operation readOnlyOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_READ_ONLY_ACCESS_OPERATION);
        Operation writeOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_WRITE_ACCESS_OPERATION);
        Operation displayInProjectListOperation = metaProject.getOperation(MetaProjectConstants.OPERATION_DISPLAY_IN_PROJECT_LIST.getName());

        Set<GroupOperation> allowedGpOp = projectInstance.getAllowedGroupOperations();
        for (GroupOperation groupOperation : allowedGpOp) {
            Group gp = groupOperation.getAllowedGroup();
            Set<Operation> go = groupOperation.getAllowedOperations();
            if (gp.equals(group) && go.contains(readOnlyOperation) && go.contains(writeOperation) && go.contains(displayInProjectListOperation)) {
                return;
            }
        }
        GroupOperation groupOp = metaProject.createGroupOperation();
        groupOp.setAllowedGroup(group);
        groupOp.addAllowedOperation(readOnlyOperation);
        groupOp.addAllowedOperation(writeOperation);
        groupOp.addAllowedOperation(displayInProjectListOperation);
        projectInstance.addAllowedGroupOperations(groupOp);

    }

}