package edu.stanford.bmir.protege.web.shared.auth;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public interface MessageDigestAlgorithm {

    /**
     * Update the digest with the specified bytes.
     * @param bytes The bytes.  Not {@code null}.  May be empty.
     * @throws java.lang.NullPointerException if bytes is {@code null}.
     */
    void update(byte [] bytes);

    /**
     * Updates the digest with the bytes that are obtained by encoding the specified string with a UTF-8
     * encoding.  This is essentially the same as calling {@link #update(byte[])} with the bytes from
     * {@link String#getBytes(String)} with an argument of "utf-8".
     * @param utf8String The string.  Not {@code null}.  May be empty.
     */
    void updateWithBytesFromUtf8String(String utf8String);

    /**
     * Computes the current digest.
     * @return A byte array representing the current digest.  Not {@code null}.
     */
    byte [] computeDigest();

    /**
     * Computes the current digest, returning a string representation of the Base16 encoding of the digest.
     * @return A string representing the Base16 encoding of the digest.  Not {@code null}.
     */
    String computeDigestAsBase16Encoding();

}
