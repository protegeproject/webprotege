package edu.stanford.bmir.protege.web.server.auth;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.metaproject.AuthenticationManager;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class PerformLoginActionHandler implements ActionHandler<PerformLoginAction, PerformLoginResult> {

    private ChapSessionManager chapSessionManager;

    private AuthenticationManager authenticationManager;

    private ChapResponseChecker chapResponseChecker;

    private WebProtegeLogger logger;

    @Inject
    public PerformLoginActionHandler(ChapSessionManager chapSessionManager, AuthenticationManager authenticationManager, ChapResponseChecker chapResponseChecker, WebProtegeLogger logger) {
        this.chapSessionManager = chapSessionManager;
        this.authenticationManager = authenticationManager;
        this.chapResponseChecker = chapResponseChecker;
        this.logger = logger;
    }

    @Override
    public Class<PerformLoginAction> getActionClass() {
        return PerformLoginAction.class;
    }

    @Override
    public RequestValidator<PerformLoginAction> getRequestValidator(PerformLoginAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public PerformLoginResult execute(PerformLoginAction action, ExecutionContext executionContext) {
        UserId userId = action.getUserId();
        Optional<SaltedPasswordDigest> passwordDigest = authenticationManager.getSaltedPasswordDigest(userId);
        if (!passwordDigest.isPresent()) {
            logger.info("Authentication attempt, but no digestOfSaltedPassword set for user %s", userId);
            return new PerformLoginResult(AuthenticationResponse.FAIL);
        }
        Optional<ChapSession> chapDataOptional = chapSessionManager.retrieveChallengeMessage(action.getChapSessionId());
        if (!chapDataOptional.isPresent()) {
            logger.info("Challenge expired for user %s", userId);
            return new PerformLoginResult(AuthenticationResponse.FAIL);
        }
        ChapSession chapSession = chapDataOptional.get();
        ChallengeMessage challenge = chapSession.getChallengeMessage();
        ChapResponse chapResponse = action.getChapResponse();

        boolean expectedResponse = chapResponseChecker.isExpectedResponse(
                chapResponse,
                challenge,
                passwordDigest.get());
        if (expectedResponse) {
            WebProtegeSession session = executionContext.getSession();
            session.setUserInSession(userId);
            return new PerformLoginResult(AuthenticationResponse.SUCCESS);
        } else {
            return new PerformLoginResult(AuthenticationResponse.FAIL);
        }

    }
}
