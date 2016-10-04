package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.io.BaseEncoding;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class Md5MessageDigestAlgorithm implements MessageDigestAlgorithm {

    private static final String UTF_8 = "UTF-8";

    private MessageDigest messageDigest;

    @Inject
    public Md5MessageDigestAlgorithm() {
        try {
            this.messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(byte[] bytes) {
        messageDigest.update(checkNotNull(bytes));
    }

    @Override
    public void updateWithBytesFromUtf8String(String utf8String) {
        try {
            messageDigest.update(checkNotNull(utf8String).getBytes(UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Broken JVM.  UTF-8 Encoding is not available.");
        }
    }

    @Override
    public byte[] computeDigest() {
        return messageDigest.digest();
    }

    @Override
    public String computeDigestAsBase16Encoding() {
        byte [] digest = messageDigest.digest();
        return BaseEncoding.base16().lowerCase().encode(digest);
    }
}
