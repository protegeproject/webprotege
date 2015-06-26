package edu.stanford.bmir.protege.web.server.mail;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.metaproject.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressResult;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class SetEmailAddressActionHandler implements ActionHandler<SetEmailAddressAction, SetEmailAddressResult> {

    private final UserDetailsManager userDetailsManager;

    private final SetEmailAddressRequestValidator validator;

    @Inject
    public SetEmailAddressActionHandler(UserDetailsManager userDetailsManager, SetEmailAddressRequestValidator validator) {
        this.userDetailsManager = userDetailsManager;
        this.validator = validator;
    }

    @Override
    public Class<SetEmailAddressAction> getActionClass() {
        return SetEmailAddressAction.class;
    }

    @Override
    public RequestValidator<SetEmailAddressAction> getRequestValidator(SetEmailAddressAction action, RequestContext requestContext) {
        return validator;
    }

    @Override
    public SetEmailAddressResult execute(SetEmailAddressAction action, ExecutionContext executionContext) {
        userDetailsManager.setEmail(action.getUserId(), action.getEmailAddress());
        return new SetEmailAddressResult();
    }
}
