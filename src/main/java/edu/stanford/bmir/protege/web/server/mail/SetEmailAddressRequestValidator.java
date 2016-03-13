package edu.stanford.bmir.protege.web.server.mail;

import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectPermissionsManager;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class SetEmailAddressRequestValidator implements RequestValidator<SetEmailAddressAction> {

    @Inject
    public SetEmailAddressRequestValidator() {
    }

    @Override
    public RequestValidationResult validateAction(SetEmailAddressAction action, RequestContext requestContext) {
        if(action.getUserId().equals(requestContext.getUserId())) {
            return RequestValidationResult.getValid();
        }
        else {
            return RequestValidationResult.getInvalid("Users can only change their own email addresses");
        }
    }
}
