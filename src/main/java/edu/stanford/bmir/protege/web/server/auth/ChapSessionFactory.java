package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.shared.auth.ChapSession;
import edu.stanford.bmir.protege.web.shared.auth.Salt;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public interface ChapSessionFactory {

    /**
     * Creates a fresh ChapSession.
     * @param salt The salt that should be used for the session.
     * @return A fresh ChapSession.  A unique {@link edu.stanford.bmir.protege.web.shared.auth.ChapSessionId}
     * will be generated and a random {@link edu.stanford.bmir.protege.web.shared.auth.ChallengeMessage}
     * will be generated.
     */
    ChapSession getChapSession(Salt salt);
}
