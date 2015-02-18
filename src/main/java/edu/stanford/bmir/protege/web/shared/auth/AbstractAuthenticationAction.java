package edu.stanford.bmir.protege.web.shared.auth;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public abstract class AbstractAuthenticationAction<R extends AbstractAuthenticationResult> implements Action<R> {

    private UserId userId;

    private ChapSessionId chapSessionId;

    private byte [] challengeResponse;

    /**
     * For serialization only
     */
    protected AbstractAuthenticationAction() {
    }

    public AbstractAuthenticationAction(UserId userId, ChapSessionId chapSessionId, byte[] challengeResponse) {
        this.chapSessionId = chapSessionId;
        this.challengeResponse = Arrays.copyOf(checkNotNull(challengeResponse), challengeResponse.length);
        this.userId = checkNotNull(userId);
    }

    public UserId getUserId() {
        return userId;
    }

    public ChapSessionId getId() {
        return chapSessionId;
    }

    public byte[] getChallengeResponse() {
        return Arrays.copyOf(challengeResponse, challengeResponse.length);
    }
}
