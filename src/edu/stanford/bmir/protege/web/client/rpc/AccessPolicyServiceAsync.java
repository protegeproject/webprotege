package edu.stanford.bmir.protege.web.client.rpc;


import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.AccessPolicyUserData;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.domain.Invitation;

import java.util.Collection;
import java.util.List;

/**
 * @author z.khan
 *
 */
public interface AccessPolicyServiceAsync {

//    void getOwner(String projectName, AsyncCallback<String> callback);

//    void canManageProject(String projectName, String userName, AsyncCallback<Boolean> callback);
//
//    void getUsers(AsyncCallback<List<String>> callback);
//
//    void addReadPermission(String projectName, List<String> userName, AsyncCallback<Void> callback);
//
//    void removeReadPermission(String projectName, List<String> userName, AsyncCallback<Void> callback);
//
//    void addWritePermission(String projectName, List<String> userName, AsyncCallback<Void> callback);
//
//    void removeWritePermission(String projectName, List<String> userName, AsyncCallback<Void> callback);
//
//    void getUsersWithReadOnlyAccess(String projectName, AsyncCallback<Collection<AccessPolicyUserData>> callback);
//
//    void getUsersWithWriteAccess(String projectName, AsyncCallback<Collection<AccessPolicyUserData>> callback);
//
//    void checkIfAdditionalPolicyPresent(String projectName, AsyncCallback<Boolean> callback);

    void createTemporaryAccountForInvitation(String projectName,String invitationBaseURL, List<Invitation> invitation, AsyncCallback<Void> callback);

    void isInvitedAccountPresent(String invitationId, AsyncCallback<Boolean> callback);

    void isAccountTemporary(String invitationId, String invitationRandomNo, AsyncCallback<Boolean> callback);

    void updateInvitedTemporaryAccount(String name, String hashedPassword, String emailId,
                                       AsyncCallback<AccessPolicyUserData> callback);

    void isInvitationValid(String invitationId, AsyncCallback<Boolean> callback);

//    void getUsersWithAccess(String projectName, AsyncCallback<Collection<AccessPolicyUserData>> async);
}

