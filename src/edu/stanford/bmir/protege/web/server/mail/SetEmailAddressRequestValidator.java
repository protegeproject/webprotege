package edu.stanford.bmir.protege.web.server.mail;

import edu.stanford.bmir.protege.web.client.ui.constants.OntologyShareAccessConstants;
import edu.stanford.bmir.protege.web.server.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.Group;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.User;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class SetEmailAddressRequestValidator implements RequestValidator<SetEmailAddressAction> {

    @Override
    public RequestValidationResult validateAction(SetEmailAddressAction action, RequestContext requestContext) {
        if(action.getUserId().equals(requestContext.getUserId()) || isUserAdmin(requestContext.getUserId())) {
            return RequestValidationResult.getValid();
        }
        else {
            return RequestValidationResult.getInvalid("Users can only change their own email addresses");
        }
    }

    /**
     * Determines if the signed in user is an admin.
     * @return <code>true</code> if there is a user signed in AND the signed in user corresponds to a user which exists
     * where that user is an admin, otherwise <code>false</code>
     */
    protected boolean isUserAdmin(UserId userId) {
        if(userId.isGuest()) {
            return false;
        }
        MetaProjectManager mpm = MetaProjectManager.getManager();
        MetaProject metaProject = mpm.getMetaProject();
        User user = metaProject.getUser(userId.getUserName());
        if(user == null) {
            return false;
        }
        for(Group group : user.getGroups()) {
            if(OntologyShareAccessConstants.ADMIN_GROUP.equals(group.getName())) {
                return true;
            }
        }
        return false;
    }
}
