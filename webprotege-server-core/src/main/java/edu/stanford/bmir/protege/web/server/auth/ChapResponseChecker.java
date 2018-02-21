package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.shared.auth.*;

import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
public class ChapResponseChecker {

    private Provider<MessageDigestAlgorithm> digestAlgorithmProvider;

    @Inject
    public ChapResponseChecker(Provider<MessageDigestAlgorithm> digestAlgorithm) {
        this.digestAlgorithmProvider = digestAlgorithm;
    }

    public boolean isExpectedResponse(ChapResponse chapResponse, ChallengeMessage challengeMessage, SaltedPasswordDigest saltedPasswordDigest) {
        ChapResponseDigestAlgorithm algorithm = new ChapResponseDigestAlgorithm(digestAlgorithmProvider);
        ChapResponse expectedResponse = algorithm.getChapResponseDigest(challengeMessage, saltedPasswordDigest);
        return expectedResponse.equals(checkNotNull(chapResponse));
    }
}
