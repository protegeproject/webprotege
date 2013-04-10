package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.AccessPolicyService;
import edu.stanford.bmir.protege.web.client.rpc.data.AccessPolicyUserData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.domain.Invitation;
import edu.stanford.smi.protege.server.metaproject.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Service for Access Privilege's Management
 * @author z.khan
 */
public class AccessPolicyServiceImpl extends WebProtegeRemoteServiceServlet implements AccessPolicyService {

    private static final long serialVersionUID = -2752914519157198904L;

    private final AccessPolicyManager accessPolicyManager = AccessPolicyManager.get();

    public String getOwner(String projectName) {

        return accessPolicyManager.getOwner(ProjectId.get(projectName));
    }

    public boolean canManageProject(String projectName, String userName) {
        return accessPolicyManager.canManageProject(ProjectId.get(projectName), UserId.getUserId(userName));
    }

    public List<String> getUsers() {
        return accessPolicyManager.getUsers();
    }

    public void addReadPermission(String projectName, List<String> userNameList) {

        //Checking if the server user mentioned in webprotege.user in protege.properties is present in users readers group. If not then adds it.
        //The server user is required while loading the project and should have read access.

        accessPolicyManager.addReadPermission(ProjectId.get(projectName), userNameList);
    }

    private synchronized boolean doesProjectContainsGroupAndOperation(ProjectInstance projectInstance, Group group, Operation operation) {

        return accessPolicyManager.doesProjectContainsGroupAndOperation(projectInstance, group, operation);
    }

    private synchronized void addPermission(MetaProject metaProject, List<String> userNameList, ProjectInstance projectInstance, Group group, Operation operation, boolean doesProjectContainsGroup) {

        accessPolicyManager.addPermission(metaProject, userNameList, projectInstance, group, operation, doesProjectContainsGroup);
    }

    public void removeReadPermission(String projectName, List<String> userNameList) {

        accessPolicyManager.removeReadPermission(ProjectId.get(projectName), userNameList);
    }

    private synchronized void removePermission(MetaProject metaProject, List<String> userNameList, ProjectInstance projectInstance, Group group, Operation operation) {
        accessPolicyManager.removePermission(metaProject, userNameList, projectInstance, group, operation);
    }

    public void addWritePermission(String projectName, List<String> userNameList) {

        accessPolicyManager.addWritePermission(ProjectId.get(projectName), userNameList);
    }

    public void removeWritePermission(String projectName, List<String> userNameList) {

        accessPolicyManager.removeWritePermission(ProjectId.get(projectName), userNameList);
    }

    public Collection<AccessPolicyUserData> getUsersWithReadOnlyAccess(String projectName) {
        return accessPolicyManager.getUsersWithReadOnlyAccess(ProjectId.get(projectName));
    }

    public Collection<AccessPolicyUserData> getUsersWithReadOnlyAccess(String projectName, boolean includeTemporaryAccount) {
        return accessPolicyManager.getUsersWithReadOnlyAccess(ProjectId.get(projectName), includeTemporaryAccount);
    }

    public Collection<AccessPolicyUserData> getUsersWithAccess(String projectName) {
//        MetaProject metaProject = MetaProjectManager.getManager().getMetaProject();
//
//        Collection<AccessPolicyUserData> userList = new TreeSet<AccessPolicyUserData>(new AccessPolicyUserData());
//        ProjectInstance projectInstance = metaProject.getProject(ProjectId.get(projectName));
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
//        Group group = metaProject.getGroup(ProjectId.get(projectName) + OntologyShareAccessConstants.ONTOLOGY_READERS_GROUP_SUFFIX);// add
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
        return accessPolicyManager.getUsersWithAccess(ProjectId.get(projectName));
    }

    public Collection<AccessPolicyUserData> getUsersWithWriteAccess(String projectName) {
        return accessPolicyManager.getUsersWithWriteAccess(ProjectId.get(projectName));
    }

    /**
     * Retrieves uses having write access for the particular project
     * @param projectName
     * @param includeTemporaryAccount
     * @return
     */
    public Collection<AccessPolicyUserData> getUsersWithWriteAccess(String projectName, boolean includeTemporaryAccount) {

        return accessPolicyManager.getUsersWithWriteAccess(ProjectId.get(projectName), includeTemporaryAccount);
    }

    /**
     * Retrieves users list from group operations as list of string.
     * @param projectInstance
     * @param group
     * @param operation
     * @return
     */
    private synchronized List<String> getUserListForGroupOperation(ProjectInstance projectInstance, Group group, Operation operation) {

        return accessPolicyManager.getUserListForGroupOperation(projectInstance, group, operation);
    }

    /**
     * Checks if the users is from default policy
     * @param groupOperations
     * @param operation
     * @return
     */
    private synchronized boolean isUsersFromDefaultPolicy(Set<GroupOperation> groupOperations, Operation operation) {

        return accessPolicyManager.isUsersFromDefaultPolicy(groupOperations, operation);
    }

    private synchronized void removeDefaultPolicyIfExists(ProjectInstance projectInstance, Operation operation) {
        accessPolicyManager.removeDefaultPolicyIfExists(projectInstance, operation);
    }

    private synchronized void updateProjectOwnerPolicy(ProjectInstance projectInstance) {

        accessPolicyManager.updateProjectOwnerPolicy(projectInstance);
    }

    private synchronized void updateProjectOwnerPolicyIfExists(ProjectInstance projectInstance, Group group, MetaProject metaProject) {

        accessPolicyManager.updateProjectOwnerPolicyIfExists(projectInstance, group, metaProject);
    }

    public synchronized boolean checkIfAdditionalPolicyPresent(String projectName) {

        return accessPolicyManager.checkIfAdditionalPolicyPresent(ProjectId.get(projectName));
    }

    /**
     * Creates temporary account for the invitees.
     * @param projectName the project name for which to create temporary account
     * @param invitationBaseURL
     * @param invitationList
     * @return
     */
    public void createTemporaryAccountForInvitation(String projectName, String invitationBaseURL, List<Invitation> invitationList) {
        accessPolicyManager.createTemporaryAccountForInvitation(ProjectId.get(projectName), invitationBaseURL, invitationList);
    }

    /**
     * Extracts the name from collection of UserData and returns Arraylist of
     * names.
     * @param collection
     * @return
     */
    private ArrayList<String> getNameListFromUserDataCollection(Collection<AccessPolicyUserData> collection) {

        return accessPolicyManager.getNameListFromUserDataCollection(collection);
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

        return accessPolicyManager.getInvitationEmailBody(isWriter, ProjectId.get(projectName), ownerName, invitationLink);
    }

    /**
     * Creates invitation url. URL contains parameter which distinguish it as an
     * invitation URL and URL also contains invitation Id.
     * @param invitationId
     * @return
     */
    private String getInvitationURL(String invitationBaseURL, String invitationId, String randomNo) {

        return accessPolicyManager.getInvitationURL(invitationBaseURL, invitationId, randomNo);
    }

    /**
     * Checks if the account identified by given invitation id is present or
     * not.
     * @param invitationId
     * @return
     */
    public boolean isInvitedAccountPresent(String invitationId) {
        return accessPolicyManager.isInvitedAccountPresent(invitationId);
    }

    /**
     * Checks if the account identified by given invitation id is temporary or
     * not.
     * @param invitationId
     * @return
     */
    public boolean isAccountTemporary(String invitationId, String invitationRandomNo) {
        return accessPolicyManager.isAccountTemporary(invitationId, invitationRandomNo);
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
        HttpServletRequest request = getThreadLocalRequest();
        HttpSession session = request.getSession();
        String salt = (String) session.getAttribute(AuthenticationConstants.NEW_SALT);
        return accessPolicyManager.updateInvitedTemporaryAccount(name, hashedPassword, salt, emailId);
    }

    /**
     * Checks if invitation has expired or not not.
     * @param invitationId
     * @return
     */
    public boolean isInvitationValid(String invitationId) {

        return accessPolicyManager.isInvitationValid(invitationId);
    }
}
