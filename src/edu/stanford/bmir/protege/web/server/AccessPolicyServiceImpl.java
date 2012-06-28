package edu.stanford.bmir.protege.web.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.stanford.bmir.protege.web.client.rpc.AccessPolicyService;
import edu.stanford.bmir.protege.web.client.rpc.data.AccessPolicyUserData;
import edu.stanford.bmir.protege.web.client.ui.constants.OntologyShareAccessConstants;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.InvitationConstants;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.domain.Invitation;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.server.metaproject.*;
import edu.stanford.smi.protege.server.metaproject.impl.GroupOperationImpl;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;
import edu.stanford.smi.protege.server.metaproject.impl.WrappedProtegeInstanceImpl;
import edu.stanford.smi.protege.util.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service for Access Privilege's Management
 * @author z.khan
 */
public class AccessPolicyServiceImpl extends WebProtegeRemoteServiceServlet implements AccessPolicyService {

    private static final long serialVersionUID = -2752914519157198904L;

    public String getOwner(String projectName) {
        MetaProject metaproject = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject();
        ProjectInstance projectInstance = metaproject.getProject(projectName);
        if (projectInstance == null) {
            return null;
        }

        User owner = projectInstance.getOwner();
        return owner.getName();
    }

    public boolean canManageProject(String projectName, String userName) {
        if (projectName == null || userName == null) {
            return false;
        }
        try {
            MetaProject metaproject = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject();
            ProjectInstance projectInstance = metaproject.getProject(projectName);

            if (projectInstance == null) {
                return false;
            }
            User owner = projectInstance.getOwner();

            if (owner.getName().equalsIgnoreCase(userName)) {
                return true;
            }
        }
        catch (Exception e) {
            Log.getLogger().severe("Error in function canManageProject :" + e);
        }
        return false;
    }

    public List<String> getUsers() {
        Set<User> users = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject().getUsers();
        List<String> userNameList = new ArrayList<String>();
        if (users == null) {
            return userNameList;
        }
        for (Iterator iterator = users.iterator(); iterator.hasNext(); ) {
            User user = (User) iterator.next();
            if (user.getName() != null) {
                if (user.getPropertyValue(InvitationConstants.USER_PROPERTY_IS_TEMPORARY_ACCOUNT) == null || !user.getPropertyValue(InvitationConstants.USER_PROPERTY_IS_TEMPORARY_ACCOUNT).trim().equals("true")) { // Proceed if account is not temporary.
                    userNameList.add(user.getName());
                }

            }
        }
        return userNameList;
    }

    public void addReadPermission(String projectName, List<String> userNameList) {
        if (userNameList == null || projectName == null) {
            return;
        }

        MetaProject metaProject = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject();
        Operation readOnlyOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_READ_ONLY_ACCESS_OPERATION);
        ProjectInstance projectInstance = metaProject.getProject(projectName);

        removeDefaultPolicyIfExists(projectInstance, readOnlyOperation);

        Group group = metaProject.getGroup(projectName + OntologyShareAccessConstants.ONTOLOGY_READERS_GROUP_SUFFIX);
        if (group == null) {
            group = metaProject.createGroup(projectName + OntologyShareAccessConstants.ONTOLOGY_READERS_GROUP_SUFFIX);
        }
        if (readOnlyOperation == null) {
            readOnlyOperation = metaProject.createOperation(OntologyShareAccessConstants.PROJECT_READ_ONLY_ACCESS_OPERATION);

        }

        User serverUser = metaProject.getUser(ApplicationProperties.getProtegeServerUser());
        //Checking if the server user mentioned in webprotege.user in protege.properties is present in users readers group. If not then adds it.
        //The server user is required while loading the project and should have read access.
        if (serverUser != null && !group.getMembers().contains(serverUser)) {
            Set<User> members = group.getMembers();
            members.add(serverUser);
            group.setMembers(members);
        }

        if (doesProjectContainsGroupAndOperation(projectInstance, group, readOnlyOperation)) {
            addPermission(metaProject, userNameList, projectInstance, group, readOnlyOperation, true);
        }
        else {
            addPermission(metaProject, userNameList, projectInstance, group, readOnlyOperation, false);
        }

    }

    private synchronized boolean doesProjectContainsGroupAndOperation(ProjectInstance projectInstance, Group group, Operation operation) {

        for (GroupOperation groupOperation : projectInstance.getAllowedGroupOperations()) {
            if (groupOperation.getAllowedGroup().equals(group) && groupOperation.getAllowedOperations().contains(operation)) {

                return true;
            }
        }
        return false;

    }

    private synchronized void addPermission(MetaProject metaProject, List<String> userNameList, ProjectInstance projectInstance, Group group, Operation operation, boolean doesProjectContainsGroup) {

        if (doesProjectContainsGroup) {
            for (GroupOperation groupOperation : projectInstance.getAllowedGroupOperations()) {
                if (groupOperation.getAllowedGroup().equals(group) && groupOperation.getAllowedOperations().contains(operation)) {
                    for (Iterator iterator = userNameList.iterator(); iterator.hasNext(); ) {
                        String userName = (String) iterator.next();
                        User user = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject().getUser(userName);
                        group.addMember(user);
                    }
                    return;
                }
            }
        }
        else {
            for (Iterator iterator = userNameList.iterator(); iterator.hasNext(); ) {
                String userName = (String) iterator.next();
                User user = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject().getUser(userName);
                group.addMember(user);
            }
            GroupOperation groupOperation = metaProject.createGroupOperation();
            groupOperation.setAllowedGroup(group);
            groupOperation.addAllowedOperation(operation);
            projectInstance.addAllowedGroupOperations(groupOperation);

        }
    }

    public void removeReadPermission(String projectName, List<String> userNameList) {

        if (userNameList == null || projectName == null) {
            return;
        }

        MetaProject metaProject = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject();
        Operation readOnlyOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_READ_ONLY_ACCESS_OPERATION);
        if (readOnlyOperation == null) {
            return;
        }
        ProjectInstance projectInstance = metaProject.getProject(projectName);
        Group group = metaProject.getGroup(projectName + OntologyShareAccessConstants.ONTOLOGY_READERS_GROUP_SUFFIX);
        if (doesProjectContainsGroupAndOperation(projectInstance, group, readOnlyOperation)) {
            removePermission(metaProject, userNameList, projectInstance, group, readOnlyOperation);
        }
    }

    private synchronized void removePermission(MetaProject metaProject, List<String> userNameList, ProjectInstance projectInstance, Group group, Operation operation) {
        for (GroupOperation groupOperation : projectInstance.getAllowedGroupOperations()) {
            if (groupOperation.getAllowedGroup().equals(group) && groupOperation.getAllowedOperations().contains(operation)) {

                Set<User> members = group.getMembers();
                for (Iterator iterator = userNameList.iterator(); iterator.hasNext(); ) {
                    String userName = (String) iterator.next();
                    User user = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject().getUser(userName);
                    members.remove(user);

                }
                group.setMembers(members);
                break;
            }
        }
    }

    public void addWritePermission(String projectName, List<String> userNameList) {
        if (userNameList == null || projectName == null) {
            return;
        }

        MetaProject metaProject = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject();
        Operation writeOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_WRITE_ACCESS_OPERATION);
        ProjectInstance projectInstance = metaProject.getProject(projectName);

        removeDefaultPolicyIfExists(projectInstance, writeOperation);

        Group group = metaProject.getGroup(projectName + OntologyShareAccessConstants.ONTOLOGY_WRITERS_GROUP_SUFFIX);
        if (group == null) {
            group = metaProject.createGroup(projectName + OntologyShareAccessConstants.ONTOLOGY_WRITERS_GROUP_SUFFIX);
        }
        if (writeOperation == null) {
            writeOperation = metaProject.createOperation(OntologyShareAccessConstants.PROJECT_WRITE_ACCESS_OPERATION);

        }
        if (doesProjectContainsGroupAndOperation(projectInstance, group, writeOperation)) {
            addPermission(metaProject, userNameList, projectInstance, group, writeOperation, true);
        }
        else {
            addPermission(metaProject, userNameList, projectInstance, group, writeOperation, false);
        }
    }

    public void removeWritePermission(String projectName, List<String> userNameList) {
        if (userNameList == null || projectName == null) {
            return;
        }

        MetaProject metaProject = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject();
        Operation writeOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_WRITE_ACCESS_OPERATION);
        if (writeOperation == null) {
            return;
        }
        ProjectInstance projectInstance = metaProject.getProject(projectName);
        Group group = metaProject.getGroup(projectName + OntologyShareAccessConstants.ONTOLOGY_WRITERS_GROUP_SUFFIX);
        if (doesProjectContainsGroupAndOperation(projectInstance, group, writeOperation)) {
            removePermission(metaProject, userNameList, projectInstance, group, writeOperation);
        }

    }

    public Collection<AccessPolicyUserData> getUsersWithReadOnlyAccess(String projectName) {
        return getUsersWithReadOnlyAccess(projectName, false);
    }

    public Collection<AccessPolicyUserData> getUsersWithReadOnlyAccess(String projectName, boolean includeTemporaryAccount) {
        try {
            MetaProject metaProject = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject();

            Collection<AccessPolicyUserData> userList = new TreeSet<AccessPolicyUserData>(new AccessPolicyUserData());
            ProjectInstance projectInstance = metaProject.getProject(projectName);
            Policy policy = metaProject.getPolicy();
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
                for (Iterator iterator = groupOperations.iterator(); iterator.hasNext(); ) {
                    try {
                        GroupOperation groupOperation = (GroupOperation) iterator.next();
                        Group group = groupOperation.getAllowedGroup();
                        if (group.getMembers() != null) {
                            allPoliciesUsers.addAll(group.getMembers());
                        }
                    }
                    catch (Exception e) {
                    }

                }
            }
            Group group = metaProject.getGroup(projectName + OntologyShareAccessConstants.ONTOLOGY_READERS_GROUP_SUFFIX);// add

            List<String> groupUserList = getUserListForGroupOperation(projectInstance, group, readOnlyOperation);
            User serverUser = metaProject.getUser(ApplicationProperties.getProtegeServerUser());
            for (Iterator iterator = allPoliciesUsers.iterator(); iterator.hasNext(); ) {
                User user = (User) iterator.next();
                if (user.getName() != null && policy.isOperationAuthorized(user, readOnlyOperation, projectInstance)) {
                    if (includeTemporaryAccount || user.getPropertyValue(InvitationConstants.USER_PROPERTY_IS_TEMPORARY_ACCOUNT) == null || !user.getPropertyValue(InvitationConstants.USER_PROPERTY_IS_TEMPORARY_ACCOUNT).trim().equals("true")) { // Proceed if account is not temporary.
                        AccessPolicyUserData userData = new AccessPolicyUserData();
                        userData.setName(user.getName());

                        if (groupUserList.contains(user.getName()) && !user.equals(serverUser)) {
                            userData.setPartofReaders(true);
                        }
                        try {
                            if (!user.equals(serverUser)) {
                                userList.add(userData);
                            }
                        }
                        catch (Exception e) {
                        }
                    }
                }
            }

            return userList;
        }
        catch (Exception e) {
        }
        return null;
    }

    public Collection<AccessPolicyUserData> getUsersWithAccess(String projectName) {
//        MetaProject metaProject = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject();
//
//        Collection<AccessPolicyUserData> userList = new TreeSet<AccessPolicyUserData>(new AccessPolicyUserData());
//        ProjectInstance projectInstance = metaProject.getProject(projectName);
//
//
//
//        Policy policy = metaProject.getPolicy();
//
//        Set<User> users = new HashSet<User>();
//        Set<GroupOperation> groupOperations = projectInstance.getAllowedGroupOperations();
//        for(GroupOperation groupOperation : groupOperations) {
//            for(User user : groupOperation.getAllowedGroup().getMembers()) {
//                if(!users.contains(user))
//            }
//
//        }
//
//
//
//        Operation readOnlyOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_READ_ONLY_ACCESS_OPERATION);
//        Set<GroupOperation> groupOperations = projectInstance.getAllowedGroupOperations();
//        Set<User> allPoliciesUsers = new HashSet<User>();
//
//        if (isUsersFromDefaultPolicy(groupOperations, readOnlyOperation)) {
//            AccessPolicyUserData userEveryBodyUserData = new AccessPolicyUserData();
//            userEveryBodyUserData.setName(OntologyShareAccessConstants.USER_EVERYBODY_NAME);
//            userList.add(userEveryBodyUserData);
//            return userList;
//        }
//
//        if (groupOperations != null) {
//            for (Iterator iterator = groupOperations.iterator(); iterator.hasNext(); ) {
//                try {
//                    GroupOperation groupOperation = (GroupOperation) iterator.next();
//                    Group group = groupOperation.getAllowedGroup();
//                    if (group.getMembers() != null) {
//                        allPoliciesUsers.addAll(group.getMembers());
//                    }
//                }
//                catch (Exception e) {
//                }
//
//            }
//        }
//        Group group = metaProject.getGroup(projectName + OntologyShareAccessConstants.ONTOLOGY_READERS_GROUP_SUFFIX);// add
//
//        List<String> groupUserList = getUserListForGroupOperation(projectInstance, group, readOnlyOperation);
//        User serverUser = metaProject.getUser(ApplicationProperties.getProtegeServerUser());
//        for (Iterator iterator = allPoliciesUsers.iterator(); iterator.hasNext(); ) {
//            User user = (User) iterator.next();
//            if (user.getName() != null && policy.isOperationAuthorized(user, readOnlyOperation, projectInstance)) {
//                if (includeTemporaryAccount || user.getPropertyValue(InvitationConstants.USER_PROPERTY_IS_TEMPORARY_ACCOUNT) == null || !user.getPropertyValue(InvitationConstants.USER_PROPERTY_IS_TEMPORARY_ACCOUNT).trim().equals("true")) { // Proceed if account is not temporary.
//                    AccessPolicyUserData userData = new AccessPolicyUserData();
//                    userData.setName(user.getName());
//
//                    if (groupUserList.contains(user.getName()) && !user.equals(serverUser)) {
//                        userData.setPartofReaders(true);
//                    }
//                    try {
//                        if (!user.equals(serverUser)) {
//                            userList.add(userData);
//                        }
//                    }
//                    catch (Exception e) {
//                    }
//                }
//            }
//        }
//
//        return userList;
        return null;
    }

    public Collection<AccessPolicyUserData> getUsersWithWriteAccess(String projectName) {
        return getUsersWithWriteAccess(projectName, false);
    }

    /**
     * Retrieves uses having write access for the particular project
     * @param projectName
     * @param includeTemporaryAccount
     * @return
     */
    public Collection<AccessPolicyUserData> getUsersWithWriteAccess(String projectName, boolean includeTemporaryAccount) {

        try {
            MetaProject metaProject = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject();

            Collection<AccessPolicyUserData> userList = new TreeSet<AccessPolicyUserData>(new AccessPolicyUserData());
            ProjectInstance projectInstance = metaProject.getProject(projectName);
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
                for (Iterator iterator = groupOperations.iterator(); iterator.hasNext(); ) {
                    try {
                        GroupOperation groupOperation = (GroupOperation) iterator.next();
                        Group group = groupOperation.getAllowedGroup();
                        if (group.getMembers() != null) {
                            allPoliciesUsers.addAll(group.getMembers());
                        }
                    }
                    catch (Exception e) {
                    }

                }
            }
            Group group = metaProject.getGroup(projectName + OntologyShareAccessConstants.ONTOLOGY_WRITERS_GROUP_SUFFIX);
            List<String> groupUserList = getUserListForGroupOperation(projectInstance, group, writeOperation);
            for (Iterator iterator = allPoliciesUsers.iterator(); iterator.hasNext(); ) {
                User user = (User) iterator.next();
                if (user.getName() != null && policy.isOperationAuthorized(user, writeOperation, projectInstance)) {
                    if (includeTemporaryAccount || user.getPropertyValue(InvitationConstants.USER_PROPERTY_IS_TEMPORARY_ACCOUNT) == null || !user.getPropertyValue(InvitationConstants.USER_PROPERTY_IS_TEMPORARY_ACCOUNT).trim().equals("true")) { // Proceed if account is not temporary.
                        AccessPolicyUserData userData = new AccessPolicyUserData();
                        userData.setName(user.getName());
                        if (groupUserList.contains(user.getName())) {
                            userData.setPartofWriters(true);
                        }
                        try {
                            userList.add(userData);
                        }
                        catch (Exception e) {
                        }
                    }
                }
            }
            return userList;
        }
        catch (Exception e) {
        }
        return null;
    }

    /**
     * Retrieves users list from group operations as list of string.
     * @param projectInstance
     * @param group
     * @param operation
     * @return
     */
    private synchronized List<String> getUserListForGroupOperation(ProjectInstance projectInstance, Group group, Operation operation) {
        List<String> usersNameList = new ArrayList<String>();
        Set<GroupOperation> groupOperations = projectInstance.getAllowedGroupOperations();
        if (group != null && groupOperations != null) {
            for (GroupOperation groupOperation : groupOperations) {
                if (groupOperation.getAllowedGroup().equals(group) && groupOperation.getAllowedOperations().contains(operation)) {
                    Set<User> users = group.getMembers();
                    for (Iterator iterator = users.iterator(); iterator.hasNext(); ) {
                        User user = (User) iterator.next();
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
     * @param groupOperations
     * @param operation
     * @return
     */
    private synchronized boolean isUsersFromDefaultPolicy(Set<GroupOperation> groupOperations, Operation operation) {

        for (Iterator iterator = groupOperations.iterator(); iterator.hasNext(); ) {
            GroupOperation groupOperation = (GroupOperation) iterator.next();
            Group group = groupOperation.getAllowedGroup();
            if (group.getName().equalsIgnoreCase(OntologyShareAccessConstants.WORLD_GROUP_NAME) && groupOperation.getAllowedOperations().contains(operation)) {
                return true;
            }
        }

        return false;
    }

    private synchronized void removeDefaultPolicyIfExists(ProjectInstance projectInstance, Operation operation) {
        Set<GroupOperation> groupOperations = projectInstance.getAllowedGroupOperations();
        for (Iterator iterator = groupOperations.iterator(); iterator.hasNext(); ) {
            GroupOperation groupOperation = (GroupOperation) iterator.next();
            Group group = groupOperation.getAllowedGroup();
            if (group.getName().equalsIgnoreCase(OntologyShareAccessConstants.WORLD_GROUP_NAME) && groupOperation.getAllowedOperations().contains(operation)) {
                groupOperations.remove(groupOperation);
                projectInstance.setAllowedGroupOperations(groupOperations);

                Instance groupOpInst1 = (((WrappedProtegeInstanceImpl) groupOperation).getProtegeInstance());
                Instance cloneGroupOpInst = (Instance) groupOpInst1.shallowCopy(null, null);

                GroupOperation cloneGroupOp = new GroupOperationImpl((MetaProjectImpl) projectInstance.getMetaProject(), cloneGroupOpInst);
                Set<Operation> cloneGroupAllowedOp = cloneGroupOp.getAllowedOperations();
                for (Iterator iterator2 = cloneGroupAllowedOp.iterator(); iterator2.hasNext(); ) {
                    Operation op = (Operation) iterator2.next();
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

    private synchronized void updateProjectOwnerPolicy(ProjectInstance projectInstance) {
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

    private synchronized void updateProjectOwnerPolicyIfExists(ProjectInstance projectInstance, Group group, MetaProject metaProject) {
        Operation readOnlyOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_READ_ONLY_ACCESS_OPERATION);
        Operation writeOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_WRITE_ACCESS_OPERATION);
        Operation displayInProjectListOperation = metaProject.getOperation(MetaProjectConstants.OPERATION_DISPLAY_IN_PROJECT_LIST.getName());

        Set<GroupOperation> allowedGpOp = projectInstance.getAllowedGroupOperations();
        for (Iterator iterator = allowedGpOp.iterator(); iterator.hasNext(); ) {
            GroupOperation groupOperation = (GroupOperation) iterator.next();
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

    public synchronized boolean checkIfAdditionalPolicyPresent(String projectName) {
        try {

            MetaProject metaProject = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject();
            if (metaProject == null) {
                return true;
            }
            ProjectInstance projectInstance = metaProject.getProject(projectName);
            if (projectInstance == null) {
                return true;
            }
            String owner = projectInstance.getOwner().getName();
            if (owner == null) {
                return true;
            }

            Operation readOnlyOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_READ_ONLY_ACCESS_OPERATION);
            Operation writeOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_WRITE_ACCESS_OPERATION);
            Operation reviewOperation = metaProject.getOperation(OntologyShareAccessConstants.PROJECT_REVIEW_OPERATION);
            Operation displayInProjectListOperation = metaProject.getOperation(MetaProjectConstants.OPERATION_DISPLAY_IN_PROJECT_LIST.getName());

            Set<GroupOperation> groupOperations = projectInstance.getAllowedGroupOperations();
            for (Iterator iterator = groupOperations.iterator(); iterator.hasNext(); ) {
                GroupOperation groupOperation = (GroupOperation) iterator.next();
                Group group = groupOperation.getAllowedGroup();
                Set<Operation> allowedOp = groupOperation.getAllowedOperations();
                // writing seperate if statements instead of combining, for
                // better understanding.
                if (!(group.getName().equalsIgnoreCase(MetaProjectConstants.USER_WORLD) // group is not world with read / write access
                        && (allowedOp.contains(readOnlyOperation) || allowedOp.contains(writeOperation)))) {

                    if (!(group.getName().equalsIgnoreCase(projectName + OntologyShareAccessConstants.ONTOLOGY_READERS_GROUP_SUFFIX) && allowedOp.contains(readOnlyOperation))) {// group is not projects readers group with read access

                        if (!(group.getName().equalsIgnoreCase(projectName + OntologyShareAccessConstants.ONTOLOGY_WRITERS_GROUP_SUFFIX) && allowedOp.contains(writeOperation))) {// group is not projects writer group with write access
                            if (!(group.getName().equalsIgnoreCase(OntologyShareAccessConstants.OWNER_GROUP_PREFIX + owner) && allowedOp.contains(readOnlyOperation) && allowedOp.contains(writeOperation) && allowedOp.contains(displayInProjectListOperation))) {//group is not owner group with read, write and 'display in project list' access
                                if (!(group.getName().equalsIgnoreCase(MetaProjectConstants.USER_WORLD) // group is not world with display in project access
                                        && allowedOp.contains(displayInProjectListOperation))) {
                                    if (!(group.getName().equalsIgnoreCase(OntologyShareAccessConstants.REVIEWERS_GROUP_NAME) // group is not Reviewers with Review access
                                            && allowedOp.contains(reviewOperation))) {
                                        return true;
                                    }

                                }

                            }

                        }
                    }
                }
            }
        }
        catch (Exception e) {
            return true;// For safety: In case exception occurs, assumes contains additional policies.
        }

        return false;
    }

    /**
     * Creates temporary account for the invitees.
     * @param projectName the project name for which to create temporary account
     * @param invitationBaseURL
     * @param invitationList
     * @return
     */
    public void createTemporaryAccountForInvitation(String projectName, String invitationBaseURL, List<Invitation> invitationList) {
        try {
            if (invitationList == null)
                return;
            MetaProjectManager metaProjectManager = Protege3ProjectManager.getProjectManager().getMetaProjectManager();
            ArrayList<String> tempReaderAccountNameList = new ArrayList<String>();
            ArrayList<String> tempWriterAccountNameList = new ArrayList<String>();

            Collection<AccessPolicyUserData> readOnlyAccessUsers = getUsersWithReadOnlyAccess(projectName, true);
            Collection<AccessPolicyUserData> writeAccessUsers = getUsersWithWriteAccess(projectName, true);
            ArrayList<String> projectsReadersList = getNameListFromUserDataCollection(readOnlyAccessUsers);
            ArrayList<String> projectsWritersList = getNameListFromUserDataCollection(writeAccessUsers);

            for (Invitation invitation : invitationList) {
                try {
                    //checking if the temporary account already exists, if not then create a new one ,else update its invitation
                    User tempUser = metaProjectManager.getMetaProject().getUser(invitation.getEmailId());
                    if (tempUser != null) { //Account already exists
                        if (!isAccountTemporary(invitation.getEmailId(), tempUser.getPropertyValue(InvitationConstants.USER_PROPERTY_TEMPORARY_ACCOUNT_RANDOM_NO))) {// The account is not a temporary account , hence reject the invitation
                            continue;

                        }

                    }
                    else { //Account does not already exist
                        metaProjectManager.registerUser(invitation.getEmailId(), "");
                        metaProjectManager.setUserEmail(invitation.getEmailId(), invitation.getEmailId());
                        tempUser = metaProjectManager.getMetaProject().getUser(invitation.getEmailId());
                        Random random = new Random();
                        String randomNo = random.nextInt(10000) + "";
                        tempUser.addPropertyValue(InvitationConstants.USER_PROPERTY_IS_TEMPORARY_ACCOUNT, "true");
                        tempUser.addPropertyValue(InvitationConstants.USER_PROPERTY_TEMPORARY_ACCOUNT_RANDOM_NO, randomNo);
                    }

                    Date currentDate = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                    if (tempUser.getPropertyValue(InvitationConstants.USER_PROPERTY_ACCOUNT_INVITATION_DATE) != null) {
                        tempUser.removePropertyValue(InvitationConstants.USER_PROPERTY_ACCOUNT_INVITATION_DATE, tempUser.getPropertyValue(InvitationConstants.USER_PROPERTY_ACCOUNT_INVITATION_DATE));
                    }

                    tempUser.addPropertyValue(InvitationConstants.USER_PROPERTY_ACCOUNT_INVITATION_DATE, formatter.format(currentDate));

                    if (invitation.isWriter()) {//Invitation is for Writer
                        if (!projectsWritersList.contains(invitation.getEmailId())) {//Add only if temporary account not already in writers group
                            tempWriterAccountNameList.add(invitation.getEmailId());
                        }
                    }
                    else { //Invitation is for Reader
                        if (!projectsReadersList.contains(invitation.getEmailId())) {//Add only if temporary account not already in Readers group
                            tempReaderAccountNameList.add(invitation.getEmailId());
                        }
                    }

                    String invitationEmailBody = getInvitationEmailBody(invitation.isWriter(), projectName, getOwner(projectName), getInvitationURL(invitationBaseURL, invitation.getEmailId(), tempUser.getPropertyValue(InvitationConstants.USER_PROPERTY_TEMPORARY_ACCOUNT_RANDOM_NO)));
                    EmailUtil.sendEmail(invitation.getEmailId(), EmailConstants.INVITATION_SUBJECT, invitationEmailBody, ApplicationProperties.getEmailAccount());

                }
                catch (Exception e) {
                    Log.getLogger().severe("Error creating temporary Account " + invitation.getEmailId() + " for invitation : " + e);
                }

            }
            if (tempReaderAccountNameList != null && tempReaderAccountNameList.size() > 0) {

                addReadPermission(projectName, tempReaderAccountNameList);
            }
            if (tempWriterAccountNameList != null && tempWriterAccountNameList.size() > 0) {
                addWritePermission(projectName, tempWriterAccountNameList);
            }

        }
        catch (Exception e) {
            Log.getLogger().severe("Error in getting MetaProjectManager for temporary Account for invitation :" + e);
        }
    }

    /**
     * Extracts the name from collection of UserData and returns Arraylist of
     * names.
     * @param collection
     * @return
     */
    private ArrayList<String> getNameListFromUserDataCollection(Collection<AccessPolicyUserData> collection) {
        ArrayList<String> nameList = new ArrayList<String>();
        if (collection == null) {
            return nameList;
        }

        for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
            AccessPolicyUserData userData = (AccessPolicyUserData) iterator.next();
            nameList.add(userData.getName());
        }
        return nameList;
    }

    /**
     * Creates the email invitation body according to parameters provided.
     * @param isWriter
     * @param projectName
     * @param ownerName
     * @param invitationLink
     * @return
     */
    private String getInvitationEmailBody(boolean isWriter, String projectName, String ownerName, String invitationLink) {
        String template = EmailConstants.INVITATION_BODY;

        if (isWriter) {
            template = template.replace("<AccessType>", "Writer");
        }
        else {
            template = template.replace("<AccessType>", "Reader");
        }

        template = template.replace("<ProjectName>", projectName);
        template = template.replace("<AuthorName>", ownerName);
        template = template.replace("<InvitationLink>", invitationLink);

        return template;

    }

    /**
     * Creates invitation url. URL contains parameter which distinguish it as an
     * invitation URL and URL also contains invitation Id.
     * @param invitationId
     * @param string
     * @return
     */
    private String getInvitationURL(String invitationBaseURL, String invitationId, String randomNo) {

        String invitationURL = invitationBaseURL + InvitationConstants.INVITATION_URL_PARAMETER_IS_INVITATION + "=true";

        invitationURL = invitationURL + "&" + InvitationConstants.INVITATION_URL_PARAMETER_INVITATION_ID + "=" + invitationId;
        invitationURL = invitationURL + "&" + InvitationConstants.INVITATION_URL_PARAMETER_RANDOM_NO + "=" + randomNo;
        return invitationURL;
    }

    /**
     * Checks if the account identified by given invitation id is present or
     * not.
     * @param invitationId
     * @return
     */
    public boolean isInvitedAccountPresent(String invitationId) {
        MetaProjectManager metaProjectManager = Protege3ProjectManager.getProjectManager().getMetaProjectManager();
        User tempUser = metaProjectManager.getMetaProject().getUser(invitationId);
        if (tempUser == null) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the account identified by given invitation id is temporary or
     * not.
     * @param invitationId
     * @return
     */
    public boolean isAccountTemporary(String invitationId, String invitationRandomNo) {
        MetaProjectManager metaProjectManager = Protege3ProjectManager.getProjectManager().getMetaProjectManager();
        User tempUser = metaProjectManager.getMetaProject().getUser(invitationId);
        if (tempUser == null) {
            return false;
        }
        String isTempAccount = tempUser.getPropertyValue(InvitationConstants.USER_PROPERTY_IS_TEMPORARY_ACCOUNT);
        String randomNo = tempUser.getPropertyValue(InvitationConstants.USER_PROPERTY_TEMPORARY_ACCOUNT_RANDOM_NO);
        if (isTempAccount != null && isTempAccount.contains("true") && randomNo != null && randomNo.trim().equals(invitationRandomNo)) {
            return true;
        }
        return false;
    }

    /**
     * Updated the temporary created Invitation account. This method updates the
     * name and password of the temporary account. This method also removes the
     * flag <code></code>
     * @param name
     * @param hashedPassword
     * @param emailId
     * @return
     */
    public AccessPolicyUserData updateInvitedTemporaryAccount(String name, String hashedPassword, String emailId) {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String salt = (String) session.getAttribute(AuthenticationConstants.NEW_SALT);

        User user = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject().getUser(emailId);
        user.setDigestedPassword(hashedPassword, salt);
        user.setName(name);
        user.removePropertyValue(InvitationConstants.USER_PROPERTY_IS_TEMPORARY_ACCOUNT, "true");

        String invitationRandomNo = user.getPropertyValue(InvitationConstants.USER_PROPERTY_TEMPORARY_ACCOUNT_RANDOM_NO);
        user.removePropertyValue(InvitationConstants.USER_PROPERTY_TEMPORARY_ACCOUNT_RANDOM_NO, invitationRandomNo);

        String invitationDate = user.getPropertyValue(InvitationConstants.USER_PROPERTY_ACCOUNT_INVITATION_DATE);
        user.removePropertyValue(InvitationConstants.USER_PROPERTY_ACCOUNT_INVITATION_DATE, invitationDate);

        return new AccessPolicyUserData(name, null);
    }

    /**
     * Checks if invitation has expired or not not.
     * @param invitationId
     * @return
     */
    public boolean isInvitationValid(String invitationId) {
        boolean result = false;
        int expirationPeriodInDays = ApplicationProperties.getAccountInvitationExpirationPeriodInDays();

        MetaProjectManager metaProjectManager = Protege3ProjectManager.getProjectManager().getMetaProjectManager();
        User tempUser = metaProjectManager.getMetaProject().getUser(invitationId);
        if (tempUser == null) {
            return false;
        }
        String invitationDateString = tempUser.getPropertyValue(InvitationConstants.USER_PROPERTY_ACCOUNT_INVITATION_DATE);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date invitationDate = formatter.parse(invitationDateString);

            Date currentDate = new Date();

            if (currentDate.compareTo(invitationDate) > 0) {
                long invitationMillis = invitationDate.getTime();
                long currentMillis = currentDate.getTime();
                long difference = currentMillis - invitationMillis;

                long diffenceInDays = difference / (24 * 60 * 60 * 1000);
                if (diffenceInDays < expirationPeriodInDays) {
                    result = true;
                }
                else {
                    result = false;
                }
            }

        }
        catch (ParseException e) {
            Log.getLogger().severe("Error in parsing invitation date :" + e);
        }

        return result;
    }
}
