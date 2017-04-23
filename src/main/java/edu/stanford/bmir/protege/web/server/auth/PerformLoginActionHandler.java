package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.server.user.UserActivityManager;
import edu.stanford.bmir.protege.web.shared.auth.AuthenticationResponse;
import edu.stanford.bmir.protege.web.shared.auth.PerformLoginAction;
import edu.stanford.bmir.protege.web.shared.auth.PerformLoginResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class PerformLoginActionHandler extends AuthenticatedActionHandler<PerformLoginAction, PerformLoginResult> {

    private static final Logger logger = LoggerFactory.getLogger(PerformLoginActionHandler.class);

    private final UserActivityManager activityManager;

    @Inject
    public PerformLoginActionHandler(UserActivityManager activityManager,
                                     ChapSessionManager chapSessionManager, AuthenticationManager authenticationManager, ChapResponseChecker chapResponseChecker, WebProtegeLogger logger) {
        super(chapSessionManager, authenticationManager, chapResponseChecker, logger);
        this.activityManager = activityManager;
    }

    @Override
    public Class<PerformLoginAction> getActionClass() {
        return PerformLoginAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(PerformLoginAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    protected PerformLoginResult executeAuthenticatedAction(PerformLoginAction action, ExecutionContext executionContext) {
        WebProtegeSession session = executionContext.getSession();
        session.setUserInSession(action.getUserId());
        activityManager.setLastLogin(action.getUserId(), System.currentTimeMillis());
        logger.info("{} logged in", action.getUserId());
        return new PerformLoginResult(AuthenticationResponse.SUCCESS);
    }

    @Override
    protected PerformLoginResult createAuthenticationFailedResult() {
        return new PerformLoginResult(AuthenticationResponse.FAIL);
    }
}
