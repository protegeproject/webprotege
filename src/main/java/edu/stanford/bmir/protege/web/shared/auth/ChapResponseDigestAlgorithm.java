package edu.stanford.bmir.protege.web.shared.auth;

import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
public class ChapResponseDigestAlgorithm {

    private Provider<MessageDigestAlgorithm> digestAlgorithmProvider;

    public ChapResponseDigestAlgorithm(Provider<MessageDigestAlgorithm> digestAlgorithmProvider) {
        this.digestAlgorithmProvider = checkNotNull(digestAlgorithmProvider);
    }

    public byte [] getDigestOfMessageAndPassword(ChallengeMessage challengeMessage, SaltedPasswordDigest saltedPasswordDigest) {
        MessageDigestAlgorithm algorithm = digestAlgorithmProvider.get();
        algorithm.update(checkNotNull(challengeMessage).getBytes());
        algorithm.update(checkNotNull(saltedPasswordDigest).getBytes());
        return algorithm.computeDigest();
    }
}
