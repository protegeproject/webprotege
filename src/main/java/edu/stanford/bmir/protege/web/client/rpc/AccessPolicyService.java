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

    void createTemporaryAccountForInvitation(String projectName, String invitationBaseURL, List<Invitation> invitation);

    boolean isInvitedAccountPresent(String invitationId);

    boolean isAccountTemporary(String invitationId, String invitationRandomNo);

    AccessPolicyUserData updateInvitedTemporaryAccount(String name, String hashedPassword, String emailId);

    boolean isInvitationValid(String invitationId);
}
