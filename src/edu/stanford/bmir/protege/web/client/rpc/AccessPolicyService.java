package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.AccessPolicyUserData;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.domain.Invitation;

import java.util.List;

/**
 *
 *
 * @author z.khan
 */
@RemoteServiceRelativePath("../accesspolicy")
public interface AccessPolicyService extends RemoteService {

//    String getOwner(String projectName);

//    boolean canManageProject(String projectName, String userName);
//
//    List<String> getUsers();
//
//    void addReadPermission(String projectName, List<String> userName);
//
//    void removeReadPermission(String projectName, List<String> userName);
//
//    void addWritePermission(String projectName, List<String> userName);
//
//    void removeWritePermission(String projectName, List<String> userName);

//    Collection<AccessPolicyUserData> getUsersWithReadOnlyAccess(String projectName);
//
//    Collection<AccessPolicyUserData> getUsersWithWriteAccess(String projectName);
//
//    boolean checkIfAdditionalPolicyPresent(String projectName);

    void createTemporaryAccountForInvitation(String projectName, String invitationBaseURL, List<Invitation> invitation);

    boolean isInvitedAccountPresent(String invitationId);

    boolean isAccountTemporary(String invitationId, String invitationRandomNo);

    AccessPolicyUserData updateInvitedTemporaryAccount(String name, String hashedPassword, String emailId);

    boolean isInvitationValid(String invitationId);

//    Collection<AccessPolicyUserData> getUsersWithAccess(String projectName);

}
