package edu.stanford.bmir.protege.web.server.mail;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class GetEmailAddressActionHandler implements ApplicationActionHandler<GetEmailAddressAction, GetEmailAddressResult> {

    private final UserDetailsManager userDetailsManager;

    @Inject
    public GetEmailAddressActionHandler(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    public Class<GetEmailAddressAction> getActionClass() {
        return GetEmailAddressAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetEmailAddressAction action, RequestContext requestContext) {
        return () -> {
            if(!requestContext.getUserId().isGuest()) {
                return RequestValidationResult.getValid();
            }
            else {
                return RequestValidationResult.getInvalid("Cannot get the email address of the guest user");
            }
        };
    }

    @Override
    public GetEmailAddressResult execute(GetEmailAddressAction action, ExecutionContext executionContext) {
        Optional<EmailAddress> address = userDetailsManager.getEmail(action.getUserId()).map(EmailAddress::new);
        return new GetEmailAddressResult(action.getUserId(), address);
    }
}
