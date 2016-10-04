package edu.stanford.bmir.protege.web.server.mail;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class SetEmailAddressActionHandler implements ActionHandler<SetEmailAddressAction, SetEmailAddressResult> {

    private final UserDetailsManager userDetailsManager;

    @Inject
    public SetEmailAddressActionHandler(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    public Class<SetEmailAddressAction> getActionClass() {
        return SetEmailAddressAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(SetEmailAddressAction action, RequestContext requestContext) {
        return new SetEmailAddressRequestValidator(action.getUserId(), requestContext.getUserId());
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
