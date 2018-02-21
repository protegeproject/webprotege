package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public abstract class AuthenticatedActionHandler<A extends AbstractAuthenticationAction<R>, R extends AbstractAuthenticationResult> implements ActionHandler<A, R> {

    @Nonnull
    private final ChapSessionManager chapSessionManager;

    @Nonnull
    private final AuthenticationManager authenticationManager;

    @Nonnull
    private final ChapResponseChecker chapResponseChecker;

    @Nonnull
    private static final Logger logger = LoggerFactory.getLogger(AuthenticatedActionHandler.class);

    public AuthenticatedActionHandler(@Nonnull ChapSessionManager chapSessionManager,
                                      @Nonnull AuthenticationManager authenticationManager,
                                      @Nonnull ChapResponseChecker chapResponseChecker) {
        this.chapSessionManager = chapSessionManager;
        this.authenticationManager = authenticationManager;
        this.chapResponseChecker = chapResponseChecker;
    }

    @Nonnull
    @Override
    public final R execute(@Nonnull A action, @Nonnull ExecutionContext executionContext) {
        UserId userId = action.getUserId();
        Optional<SaltedPasswordDigest> passwordDigest = authenticationManager.getSaltedPasswordDigest(userId);
        if (!passwordDigest.isPresent()) {
            logger.info("Authentication attempt, but no digest of salted password set for user {}", userId);
            return createAuthenticationFailedResult();
        }
        Optional<ChapSession> chapDataOptional = chapSessionManager.retrieveChallengeMessage(action.getChapSessionId());
        if (!chapDataOptional.isPresent()) {
            logger.info("Challenge expired for user {}", userId);
            return createAuthenticationFailedResult();
        }
        ChapSession chapSession = chapDataOptional.get();
        ChallengeMessage challenge = chapSession.getChallengeMessage();
        ChapResponse chapResponse = action.getChapResponse();

        boolean expectedResponse = chapResponseChecker.isExpectedResponse(
                chapResponse,
                challenge,
                passwordDigest.get());

        if(expectedResponse) {
            return executeAuthenticatedAction(action, executionContext);
        }
        else {
            return createAuthenticationFailedResult();
        }
    }

    protected abstract R createAuthenticationFailedResult();

    protected abstract R executeAuthenticatedAction(A action, ExecutionContext executionContext);
}
