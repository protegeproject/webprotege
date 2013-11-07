package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.AccessPolicyService;
import edu.stanford.bmir.protege.web.client.rpc.data.AccessPolicyUserData;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.domain.Invitation;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Service for Access Privilege's Management
 * @author z.khan
 */
public class AccessPolicyServiceImpl extends WebProtegeRemoteServiceServlet implements AccessPolicyService {

    private static final long serialVersionUID = -2752914519157198904L;

    private final AccessPolicyManager accessPolicyManager = AccessPolicyManager.get();


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
