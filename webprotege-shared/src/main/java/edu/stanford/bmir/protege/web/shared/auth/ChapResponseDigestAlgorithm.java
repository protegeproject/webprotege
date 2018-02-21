package edu.stanford.bmir.protege.web.shared.auth;

import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
public class ChapResponseDigestAlgorithm {

    private Provider<MessageDigestAlgorithm> digestAlgorithmProvider;

    @Inject
    public ChapResponseDigestAlgorithm(Provider<MessageDigestAlgorithm> digestAlgorithmProvider) {
        this.digestAlgorithmProvider = checkNotNull(digestAlgorithmProvider);
    }

    /**
     * Gets the digest of the challenge message and the salted password digest.
     * @param challengeMessage The challenge message. Not {@code null}.
     * @param saltedPasswordDigest The salted password digest.  Not {@code null}.
     * @return The ChapResponse digest based on the challenge message and salted password digest.  Not {@code null}.
     */
    public ChapResponse getChapResponseDigest(ChallengeMessage challengeMessage, SaltedPasswordDigest saltedPasswordDigest) {
        MessageDigestAlgorithm algorithm = digestAlgorithmProvider.get();
        algorithm.update(checkNotNull(challengeMessage).getBytes());
        algorithm.update(checkNotNull(saltedPasswordDigest).getBytes());
        return new ChapResponse(algorithm.computeDigest());
    }
}
