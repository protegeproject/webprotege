package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.server.app.UserInSessionFactory;
import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.server.user.UserActivityManager;
import edu.stanford.bmir.protege.web.shared.auth.AuthenticationResponse;
import edu.stanford.bmir.protege.web.shared.auth.PerformLoginAction;
import edu.stanford.bmir.protege.web.shared.auth.PerformLoginResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class PerformLoginActionHandler extends AuthenticatedActionHandler<PerformLoginAction, PerformLoginResult> implements ApplicationActionHandler<PerformLoginAction, PerformLoginResult> {

    private static final Logger logger = LoggerFactory.getLogger(PerformLoginActionHandler.class);

    @Nonnull
    private final UserActivityManager activityManager;

    @Nonnull
    private final UserInSessionFactory userInSessionFactory;

    @Inject
    public PerformLoginActionHandler(@Nonnull UserActivityManager activityManager,
                                     @Nonnull ChapSessionManager chapSessionManager,
                                     @Nonnull AuthenticationManager authenticationManager,
                                     @Nonnull ChapResponseChecker chapResponseChecker,
                                     @Nonnull UserInSessionFactory userInSessionFactory) {
        super(chapSessionManager, authenticationManager, chapResponseChecker);
        this.activityManager = checkNotNull(activityManager);
        this.userInSessionFactory = userInSessionFactory;
    }

    @Nonnull
    @Override
    public Class<PerformLoginAction> getActionClass() {
        return PerformLoginAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull PerformLoginAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    protected PerformLoginResult executeAuthenticatedAction(PerformLoginAction action, ExecutionContext executionContext) {
        WebProtegeSession session = executionContext.getSession();
        UserId userId = action.getUserId();
        session.setUserInSession(userId);
        activityManager.setLastLogin(userId, System.currentTimeMillis());
        logger.info("{} logged in", userId);
        return new PerformLoginResult(AuthenticationResponse.SUCCESS, userInSessionFactory.getUserInSession(userId));
    }

    @Override
    protected PerformLoginResult createAuthenticationFailedResult() {
        return new PerformLoginResult(AuthenticationResponse.FAIL,
                                      userInSessionFactory.getUserInSession(UserId.getGuest()));
    }
}
