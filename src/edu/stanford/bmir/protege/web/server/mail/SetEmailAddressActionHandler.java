package edu.stanford.bmir.protege.web.server.mail;

import edu.stanford.bmir.protege.web.server.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressResult;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class SetEmailAddressActionHandler implements ActionHandler<SetEmailAddressAction, SetEmailAddressResult> {

    public static final SetEmailAddressRequestValidator VALIDATOR = new SetEmailAddressRequestValidator();

    @Override
    public Class<SetEmailAddressAction> getActionClass() {
        return SetEmailAddressAction.class;
    }

    @Override
    public RequestValidator<SetEmailAddressAction> getRequestValidator(SetEmailAddressAction action, RequestContext requestContext) {
        return VALIDATOR;
    }

    @Override
    public SetEmailAddressResult execute(SetEmailAddressAction action, ExecutionContext executionContext) {
        MetaProjectManager.getManager().setUserEmail(action.getUserId().getUserName(), action.getEmailAddress());
        return new SetEmailAddressResult();
    }
}
