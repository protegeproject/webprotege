package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public class ChangePasswordActionHandler extends AuthenticatedActionHandler<ChangePasswordAction, ChangePasswordResult> implements ApplicationActionHandler<ChangePasswordAction, ChangePasswordResult> {

    private final AuthenticationManager authenticationManager;


    @Inject
    public ChangePasswordActionHandler(ChapSessionManager chapSessionManager, AuthenticationManager authMan, ChapResponseChecker chapResponseChecker) {
        super(chapSessionManager, authMan, chapResponseChecker);
        this.authenticationManager = authMan;
    }

    @Nonnull
    @Override
    public Class<ChangePasswordAction> getActionClass() {
        return ChangePasswordAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull ChangePasswordAction action, @Nonnull RequestContext requestContext) {
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
