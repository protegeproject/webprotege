package edu.stanford.bmir.protege.web.shared.auth;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class PasswordDigestAlgorithm {

    private final MessageDigestAlgorithm messageDigestAlgorithm;

    /**
     * Constructs a PasswordDigestAlgorithm.
     * @param messageDigestAlgorithm The digest algorithm that performs the actual digesting.  Not {@code null}.
     */
    @Inject
    public PasswordDigestAlgorithm(MessageDigestAlgorithm messageDigestAlgorithm) {
        this.messageDigestAlgorithm = checkNotNull(messageDigestAlgorithm);
    }

    /**
     * Gets the digested salted password for the given clear text password and salt.
     * @param clearTextPassword The clear text password.  Not {@code null}.
     * @param salt The salt.  Not {@code null}.  Not empty.
     * @return The digest of the salted password.
     * @throws java.lang.IllegalArgumentException if the salt is empty.
     * @throws java.lang.NullPointerException if any parameters are {@code null}.
     */
    public byte [] getDigestOfSaltedPassword(String clearTextPassword, byte[] salt) {
        try {
            messageDigestAlgorithm.update(checkNotNull(salt));
            if(salt.length == 0) {
                throw new IllegalArgumentException("Salt is empty.  This is probably unintentional.");
            }
            byte [] utf8EncodedPassword = checkNotNull(clearTextPassword).getBytes("UTF-8");
            messageDigestAlgorithm.update(utf8EncodedPassword);
            return messageDigestAlgorithm.computeDigest();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
