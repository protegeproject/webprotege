package edu.stanford.bmir.protege.web.server.user;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.metaproject.AuthenticationManager;
import edu.stanford.bmir.protege.web.shared.user.CreateUserAccountAction;
import edu.stanford.bmir.protege.web.shared.user.CreateUserAccountResult;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public class CreateUserAccountActionHandler implements ActionHandler<CreateUserAccountAction, CreateUserAccountResult> {

    private AuthenticationManager authenticationManager;

    @Inject
    public CreateUserAccountActionHandler(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Class<CreateUserAccountAction> getActionClass() {
        return CreateUserAccountAction.class;
    }

    @Override
    public RequestValidator<CreateUserAccountAction> getRequestValidator(CreateUserAccountAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public CreateUserAccountResult execute(CreateUserAccountAction action, ExecutionContext executionContext) {
        authenticationManager.registerUser(action.getUserId(), action.getEmailAddress(), action.getPasswordDigest(), action.getSalt());
        return new CreateUserAccountResult();
    }
}
