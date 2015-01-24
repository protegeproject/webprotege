package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.AccessPolicyUserData;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.domain.Invitation;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

/**
 * @author z.khan
 */
public class AccessPolicyServiceManager {

    private static AccessPolicyServiceAsync proxy;
    private static AccessPolicyServiceManager instance;

    private AccessPolicyServiceManager() {
        proxy = GWT.create(AccessPolicyService.class);
    }

    public static AccessPolicyServiceManager getInstance() {
        if (instance == null) {
            instance = new AccessPolicyServiceManager();
        }
        return instance;
    }

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
