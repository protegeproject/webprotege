package edu.stanford.bmir.protege.web.shared.auth;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.UnsupportedEncodingException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class PasswordDigestAlgorithm {

    private final Provider<MessageDigestAlgorithm> messageDigestAlgorithmProvider;

    /**
     * Constructs a PasswordDigestAlgorithm.
     * @param messageDigestAlgorithmProvider The digest algorithm that performs the actual digesting.  Not {@code null}.
     */
    @Inject
    public PasswordDigestAlgorithm(Provider<MessageDigestAlgorithm> messageDigestAlgorithmProvider) {
        this.messageDigestAlgorithmProvider = checkNotNull(messageDigestAlgorithmProvider);
    }

    /**
     * Gets the digested salted password for the given clear text password and salt.
     * @param clearTextPassword The clear text password.  Not {@code null}.
     * @param salt The salt.  Not {@code null}.
     * @return The digest of the salted password.
     * @throws java.lang.IllegalArgumentException if the salt is empty.
     * @throws java.lang.NullPointerException if any parameters are {@code null}.
     */
    public byte [] getDigestOfSaltedPassword(String clearTextPassword, Salt salt) {
        try {
            MessageDigestAlgorithm messageDigestAlgorithm = messageDigestAlgorithmProvider.get();
            messageDigestAlgorithm.update(checkNotNull(salt).getBytes());
            byte [] utf8EncodedPassword = checkNotNull(clearTextPassword).getBytes("UTF-8");
            messageDigestAlgorithm.update(utf8EncodedPassword);
            return messageDigestAlgorithm.computeDigest();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
