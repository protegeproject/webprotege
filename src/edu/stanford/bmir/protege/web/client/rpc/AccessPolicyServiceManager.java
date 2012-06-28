package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.AccessPolicyUserData;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.domain.Invitation;

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

    public void getOwner(String projectName, AsyncCallback<String> callback) {
        proxy.getOwner(projectName, callback);
    }

    public void canManageProject(String projectName, String userName, AsyncCallback<Boolean> callback) {
        proxy.canManageProject(projectName, userName, callback);
    }

    public void getUsers(AsyncCallback<List<String>> callback) {
        proxy.getUsers(callback);
    }

    public void addReadPermission(String projectName, List<String> userName, AsyncCallback<Void> callback) {
        proxy.addReadPermission(projectName, userName, callback);
    }

    public void removeReadPermission(String projectName, List<String> userName, AsyncCallback<Void> callback) {
        proxy.removeReadPermission(projectName, userName, callback);
    }

    public void addWritePermission(String projectName, List<String> userName, AsyncCallback<Void> callback) {
        proxy.addWritePermission(projectName, userName, callback);
    }

    public void removeWritePermission(String projectName, List<String> userName, AsyncCallback<Void> callback) {
        proxy.removeWritePermission(projectName, userName, callback);
    }

    public void getUsersWithReadOnlyAccess(String projectName, AsyncCallback<Collection<AccessPolicyUserData>> callback) {
        proxy.getUsersWithReadOnlyAccess(projectName, callback);
    }

    public void getUsersWithWriteAccess(String projectName, AsyncCallback<Collection<AccessPolicyUserData>> callback) {
        proxy.getUsersWithWriteAccess(projectName, callback);
    }

    public void checkIfAdditionalPolicyPresent(String projectName, AsyncCallback<Boolean> callback) {
        proxy.checkIfAdditionalPolicyPresent(projectName, callback);
    }

    public void createTemporaryAccountForInvitation(String projectName, String invitationBaseURL,
                                                    List<Invitation> invitations, AsyncCallback<Void> callback) {
        proxy.createTemporaryAccountForInvitation(projectName, invitationBaseURL, invitations, callback);
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
