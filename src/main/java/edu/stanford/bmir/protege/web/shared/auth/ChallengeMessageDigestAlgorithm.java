package edu.stanford.bmir.protege.web.shared.auth;

import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
public class ChallengeMessageDigestAlgorithm {

    private Provider<MessageDigestAlgorithm> digestAlgorithmProvider;

    public ChallengeMessageDigestAlgorithm(Provider<MessageDigestAlgorithm> digestAlgorithmProvider) {
        this.digestAlgorithmProvider = checkNotNull(digestAlgorithmProvider);
    }

    public byte [] getDigestOfMessageAndPassword(byte[] challengeMessage, byte[] digestOfSaltedPassword) {
        MessageDigestAlgorithm algorithm = digestAlgorithmProvider.get();
        algorithm.update(checkNotNull(challengeMessage));
        algorithm.update(checkNotNull(digestOfSaltedPassword));
        return algorithm.computeDigest();
    }
}
