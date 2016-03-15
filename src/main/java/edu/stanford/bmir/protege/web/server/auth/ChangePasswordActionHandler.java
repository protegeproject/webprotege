package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserIsSignedInValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public class ChangePasswordActionHandler extends AuthenticatedActionHandler<ChangePasswordAction, ChangePasswordResult> {

    private final AuthenticationManager authenticationManager;


    @Inject
    public ChangePasswordActionHandler(ChapSessionManager chapSessionManager, AuthenticationManager authMan, ChapResponseChecker chapResponseChecker, WebProtegeLogger logger) {
        super(chapSessionManager, authMan, chapResponseChecker, logger);
        this.authenticationManager = authMan;
    }

    @Override
    public Class<ChangePasswordAction> getActionClass() {
        return ChangePasswordAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(ChangePasswordAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    protected ChangePasswordResult createAuthenticationFailedResult() {
        return new ChangePasswordResult(AuthenticationResponse.FAIL);
    }

    @Override
    protected ChangePasswordResult executeAuthenticatedAction(ChangePasswordAction action, ExecutionContext executionContext) {
        SaltedPasswordDigest newPassword = action.getNewPassword();
        Salt newSalt = action.getNewSalt();
        UserId userId = action.getUserId();
        authenticationManager.setDigestedPassword(userId, newPassword, newSalt);
        return new ChangePasswordResult(AuthenticationResponse.SUCCESS);
    }
}
