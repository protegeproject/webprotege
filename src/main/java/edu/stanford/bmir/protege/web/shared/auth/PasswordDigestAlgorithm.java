package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.io.BaseEncoding;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class PasswordDigestAlgorithm {

    private static final String CHARSET_NAME = "utf-8";

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
    public SaltedPasswordDigest getDigestOfSaltedPassword(String clearTextPassword, Salt salt) {
        try {
            // Digests the password and salt in the same (funky) way that the old meta-project code does.
            checkNotNull(clearTextPassword);
            checkNotNull(salt);
            MessageDigestAlgorithm messageDigestAlgorithm = messageDigestAlgorithmProvider.get();
            // We have to do this because of the wonky way that the meta-projects code works.  The meta-project
            // stores the salt as a string that is a base 16 encoding of the salt.  When it computes the salted
            // password it uses the bytes from this string rather than the actual bytes corresponding to the salt.
            String base16EncodedSalt = BaseEncoding.base16().lowerCase().encode(salt.getBytes());
            byte[] base16EncodedSaltBytes = base16EncodedSalt.getBytes(CHARSET_NAME);
            messageDigestAlgorithm.update(base16EncodedSaltBytes);
            messageDigestAlgorithm.update(clearTextPassword.getBytes(CHARSET_NAME));
            return new SaltedPasswordDigest(messageDigestAlgorithm.computeDigest());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
