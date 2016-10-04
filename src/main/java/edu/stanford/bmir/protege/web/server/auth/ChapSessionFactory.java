package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.shared.auth.ChallengeMessage;
import edu.stanford.bmir.protege.web.shared.auth.ChapSession;
import edu.stanford.bmir.protege.web.shared.auth.ChapSessionId;
import edu.stanford.bmir.protege.web.shared.auth.Salt;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
// TODO Auto-generate this
public class ChapSessionFactory {


    @Nonnull
    private final ChapSessionId id;

    @Nonnull
    private final ChallengeMessage challengeMessage;

    @Inject
    public ChapSessionFactory(@Nonnull ChapSessionId id,
                              @Nonnull ChallengeMessage challengeMessage) {
        this.id = checkNotNull(id);
        this.challengeMessage = checkNotNull(challengeMessage);
    }

    /**
     * Creates a fresh ChapSession.
     * @param salt The salt that should be used for the session.
     * @return A fresh ChapSession.  A unique {@link edu.stanford.bmir.protege.web.shared.auth.ChapSessionId}
     * will be generated and a random {@link edu.stanford.bmir.protege.web.shared.auth.ChallengeMessage}
     * will be generated.
     */
    public ChapSession getChapSession(Salt salt) {
        return new ChapSession(id, challengeMessage, salt);
    }
}
