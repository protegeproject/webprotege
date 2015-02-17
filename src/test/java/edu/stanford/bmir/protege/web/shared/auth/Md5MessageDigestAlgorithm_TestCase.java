package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.io.BaseEncoding;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
public class Md5MessageDigestAlgorithm_TestCase {

    public static final String HELLO_WEBPROTEGE_HEX_DIGEST = "0214cff36e786b25ad3543f6970c80f6";

    private Md5MessageDigestAlgorithm algorithm;

    private byte[] helloWebProtegeDigest = BaseEncoding.base16().decode(HELLO_WEBPROTEGE_HEX_DIGEST);
    private byte[] emptyDigest;

    @Before
    public void setUp() throws Exception {
        algorithm = new Md5MessageDigestAlgorithm();
        emptyDigest = BaseEncoding.base16().decode("d41d8cd98f00b204e9800998ecf8427e");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullBytes() {
        algorithm.update(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullUtf8String() {
        algorithm.updateWithBytesFromUtf8String(null);
    }

    @Test
    public void shouldComputeEmptyDigest() throws UnsupportedEncodingException {
        byte [] digest = algorithm.computeDigest();
        assertThat(digest, is(emptyDigest));
    }

    @Test
    public void shouldUpdateWithEmptyBytes() {
        algorithm.update(new byte[0]);
        assertThat(algorithm.computeDigest(), is(emptyDigest));
    }

    @Test
    public void shouldUpdateWithEmptyString() {
        algorithm.updateWithBytesFromUtf8String("");
        assertThat(algorithm.computeDigest(), is(emptyDigest));
    }

    @Test
    public void shouldUpdateAndComputeDigest() throws UnsupportedEncodingException {
        algorithm.update("Hello ".getBytes("utf-8"));
        algorithm.update("WebProtege".getBytes("utf-8"));
        assertThat(algorithm.computeDigest(), is(helloWebProtegeDigest));
    }

    @Test
    public void shouldRecomputeDigest() throws UnsupportedEncodingException {
        algorithm.update("Hello ".getBytes("utf-8"));
        algorithm.computeDigest();
        algorithm.update("WebProtege".getBytes("utf-8"));
        assertThat(algorithm.computeDigest(), is(helloWebProtegeDigest));
    }

    @Test
    public void shouldComputeDigestAsString() throws UnsupportedEncodingException {
        algorithm.update("Hello WebProtege".getBytes("utf-8"));
        assertThat(algorithm.computeDigestAsBase16Encoding(), is(HELLO_WEBPROTEGE_HEX_DIGEST));
    }

    @Test
    public void shouldUpdateWithBytesFromUtf8String() {
        algorithm.updateWithBytesFromUtf8String("Hello WebProtege");
        assertThat(algorithm.computeDigest(), is(helloWebProtegeDigest));
    }
}
