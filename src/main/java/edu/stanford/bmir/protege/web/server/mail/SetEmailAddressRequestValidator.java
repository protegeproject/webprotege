package edu.stanford.bmir.protege.web.server.mail;

import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectPermissionsManager;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.user.UserId;

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
        ProjectPermissionsManager mpm = MetaProjectManager.getManager();
        return mpm.isUserAdmin(userId);
    }
}
