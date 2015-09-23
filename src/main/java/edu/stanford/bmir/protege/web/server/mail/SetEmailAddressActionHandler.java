package edu.stanford.bmir.protege.web.server.mail;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.metaproject.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.User;

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
        String emailAddress = action.getEmailAddress();
        Optional<UserId> userId = userDetailsManager.getUserIdByEmailAddress(new EmailAddress(emailAddress));
        if(userId.isPresent()) {
            if(userId.get().equals(action.getUserId())) {
                // Same user, same address
                return new SetEmailAddressResult(SetEmailAddressResult.Result.ADDRESS_UNCHANGED);
            }
            else {
                // Already exists
                return new SetEmailAddressResult(SetEmailAddressResult.Result.ADDRESS_ALREADY_EXISTS);
            }
        }
        else {
            userDetailsManager.setEmail(action.getUserId(), emailAddress);
            return new SetEmailAddressResult(SetEmailAddressResult.Result.ADDRESS_CHANGED);
        }
    }
}
