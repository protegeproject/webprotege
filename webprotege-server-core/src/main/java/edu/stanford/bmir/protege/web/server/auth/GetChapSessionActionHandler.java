package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.auth.ChapSession;
import edu.stanford.bmir.protege.web.shared.auth.GetChapSessionAction;
import edu.stanford.bmir.protege.web.shared.auth.GetChapSessionResult;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class GetChapSessionActionHandler implements ApplicationActionHandler<GetChapSessionAction, GetChapSessionResult> {

    private static final Logger logger = LoggerFactory.getLogger(GetChapSessionActionHandler.class);

    private ChapSessionManager chapSessionManager;

    private AuthenticationManager authenticationManager;

    @Inject
    public GetChapSessionActionHandler(ChapSessionManager chapSessionManager,
                                       AuthenticationManager authenticationManager) {
        this.chapSessionManager = checkNotNull(chapSessionManager);
        this.authenticationManager = checkNotNull(authenticationManager);
    }

    @Nonnull
    @Override
    public Class<GetChapSessionAction> getActionClass() {
        return GetChapSessionAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetChapSessionAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetChapSessionResult execute(@Nonnull GetChapSessionAction action, @Nonnull ExecutionContext executionContext) {
        UserId userId = action.getUserId();
        if(userId.isGuest()) {
            logger.info("Attempt at authenticating guest user");
            return new GetChapSessionResult(Optional.empty());
        }
        Optional<Salt> salt = authenticationManager.getSalt(userId);
        if(!salt.isPresent()) {
            logger.info("Attempt to authenticate non-existing user: {}", userId);
            return new GetChapSessionResult(Optional.empty());
        }
        ChapSession challengeMessage = chapSessionManager.getSession(salt.get());
        return new GetChapSessionResult(Optional.of(challengeMessage));
    }
}
