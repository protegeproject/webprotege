package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.shared.auth.ChapData;
import edu.stanford.bmir.protege.web.shared.auth.Salt;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public interface ChapDataFactory {

    /**
     * Creates a fresh instance of ChapData.
     * @param salt The salt that should be used for the data.
     * @return A fresh instance of ChapData.  A unique {@link edu.stanford.bmir.protege.web.shared.auth.ChapSessionId}
     * will be generated and a random {@link edu.stanford.bmir.protege.web.shared.auth.ChallengeMessage}
     * will be generated.
     */
    ChapData getChapData(Salt salt);
}
