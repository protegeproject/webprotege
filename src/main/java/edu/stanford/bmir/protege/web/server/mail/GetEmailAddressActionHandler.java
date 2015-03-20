package edu.stanford.bmir.protege.web.server.mail;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;

import javax.annotation.Nullable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class GetEmailAddressActionHandler implements ActionHandler<GetEmailAddressAction, GetEmailAddressResult> {

    @Override
    public Class<GetEmailAddressAction> getActionClass() {
        return GetEmailAddressAction.class;
    }

    @Override
    public RequestValidator<GetEmailAddressAction> getRequestValidator(GetEmailAddressAction action, RequestContext requestContext) {
        return new RequestValidator<GetEmailAddressAction>() {
            @Override
            public RequestValidationResult validateAction(GetEmailAddressAction action, RequestContext requestContext) {
                if(!requestContext.getUserId().isGuest()) {
                    return RequestValidationResult.getValid();
                }
                else {
                    return RequestValidationResult.getInvalid("Cannot get the email address of the guest user");
                }
            }
        };
    }

    @Override
    public GetEmailAddressResult execute(GetEmailAddressAction action, ExecutionContext executionContext) {
        Optional<EmailAddress> address = MetaProjectManager.getManager().getEmail(action.getUserId()).transform(new Function<String, EmailAddress>() {
            @Nullable
            @Override
            public EmailAddress apply(String s) {
                return new EmailAddress(s);
            }
        });
        return new GetEmailAddressResult(action.getUserId(), address);
    }
}
