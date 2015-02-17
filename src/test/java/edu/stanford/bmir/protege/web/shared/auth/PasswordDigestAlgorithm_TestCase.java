package edu.stanford.bmir.protege.web.shared.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class PasswordDigestAlgorithm_TestCase {

    @Mock
    private MessageDigestAlgorithm messageDigestAlgorithm;

    private byte [] salt = new byte[]{3, 3, 3, 3, 3}, digestResult = new byte[] {4, 4, 4, 4, 4};

    private String password = "HelloWorld";

    private PasswordDigestAlgorithm algorithm;

    @Before
    public void setUp() throws Exception {
        algorithm = new PasswordDigestAlgorithm(messageDigestAlgorithm);
        when(messageDigestAlgorithm.computeDigest()).thenReturn(digestResult);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new PasswordDigestAlgorithm(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Password_IsNull() {
        algorithm.getDigestOfSaltedPassword(null, salt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_SaltIs_Null() {
        algorithm.getDigestOfSaltedPassword(password, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIf_Salt_IsEmpty() {
        algorithm.getDigestOfSaltedPassword(password, new byte[0]);
    }

    @Test
    public void shouldUseUtf8EncodingForPassword() throws UnsupportedEncodingException {
        algorithm.getDigestOfSaltedPassword(password, salt);
        verify(messageDigestAlgorithm, times(1)).update(password.getBytes("utf-8"));
    }

    @Test
    public void shouldPlaceSaltFirstAndPasswordSecond() throws UnsupportedEncodingException {
        InOrder inOrder = Mockito.inOrder(messageDigestAlgorithm);
        algorithm.getDigestOfSaltedPassword(password, salt);
        inOrder.verify(messageDigestAlgorithm, times(1)).update(salt);
        inOrder.verify(messageDigestAlgorithm, times(1)).update(password.getBytes("utf-8"));
    }

    @Test
    public void shouldReturnDigestAlgorithmResult() {
        assertThat(algorithm.getDigestOfSaltedPassword(password, salt), is(digestResult));
    }
}
