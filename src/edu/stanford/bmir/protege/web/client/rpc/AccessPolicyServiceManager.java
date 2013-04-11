package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.AccessPolicyUserData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.domain.Invitation;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Collection;
import java.util.List;

/**
 * @author z.khan
 */
public class AccessPolicyServiceManager {

    private static AccessPolicyServiceAsync proxy;
    private static AccessPolicyServiceManager instance;

    private AccessPolicyServiceManager() {
        proxy = (AccessPolicyServiceAsync) GWT.create(AccessPolicyService.class);
    }

    public static AccessPolicyServiceManager getInstance() {
        if (instance == null) {
            instance = new AccessPolicyServiceManager();
        }
        return instance;
    }

//    public void getOwner(ProjectId projectId, AsyncCallback<String> callback) {
//        proxy.getOwner(projectId.getDisplayName(), callback);
//    }

//    public void canManageProject(ProjectId projectId, UserId userId, AsyncCallback<Boolean> callback) {
//        proxy.canManageProject(projectId.getDisplayName(), userId.getUserName(), callback);
//    }
//
//    public void getUsers(AsyncCallback<List<String>> callback) {
//        proxy.getUsers(callback);
//    }
//
//    public void addReadPermission(ProjectId projectId, List<String> userName, AsyncCallback<Void> callback) {
//        proxy.addReadPermission(projectId.getDisplayName(), userName, callback);
//    }
//
//    public void removeReadPermission(ProjectId projectId, List<String> userName, AsyncCallback<Void> callback) {
//        proxy.removeReadPermission(projectId.getDisplayName(), userName, callback);
//    }
//
//    public void addWritePermission(ProjectId projectId, List<String> userName, AsyncCallback<Void> callback) {
//        proxy.addWritePermission(projectId.getDisplayName(), userName, callback);
//    }
//
//    public void removeWritePermission(ProjectId projectId, List<String> userName, AsyncCallback<Void> callback) {
//        proxy.removeWritePermission(projectId.getDisplayName(), userName, callback);
//    }
//
//    public void getUsersWithReadOnlyAccess(ProjectId projectId, AsyncCallback<Collection<AccessPolicyUserData>> callback) {
//        proxy.getUsersWithReadOnlyAccess(projectId.getDisplayName(), callback);
//    }
//
//    public void getUsersWithWriteAccess(ProjectId projectId, AsyncCallback<Collection<AccessPolicyUserData>> callback) {
//        proxy.getUsersWithWriteAccess(projectId.getDisplayName(), callback);
//    }
//
//    public void checkIfAdditionalPolicyPresent(ProjectId projectId, AsyncCallback<Boolean> callback) {
//        proxy.checkIfAdditionalPolicyPresent(projectId.getDisplayName(), callback);
//    }

    public void createTemporaryAccountForInvitation(ProjectId projectId, String invitationBaseURL,
                                                    List<Invitation> invitations, AsyncCallback<Void> callback) {
        proxy.createTemporaryAccountForInvitation(projectId.getId(), invitationBaseURL, invitations, callback);
    }

    public void isInvitedAccountPresent(String invitationId, AsyncCallback<Boolean> callback) {
        proxy.isInvitedAccountPresent(invitationId, callback);
    }

    public void isAccountTemporary(String invitationId, String invitationRandomNo, AsyncCallback<Boolean> callback) {
        proxy.isAccountTemporary(invitationId, invitationRandomNo, callback);
    }

    public void updateInvitedTemporaryAccount(String name, String hashedPassword, String emailId,
                                              AsyncCallback<AccessPolicyUserData> callback) {
        proxy.updateInvitedTemporaryAccount(name, hashedPassword, emailId, callback);
    }

    public void isInvitationValid(String invitationId, AsyncCallback<Boolean> callback){
        proxy.isInvitationValid(invitationId, callback);
    }
}
