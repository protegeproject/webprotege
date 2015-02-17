package edu.stanford.bmir.protege.web.shared.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Provider;
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
    private Provider<MessageDigestAlgorithm> messageDigestAlgorithmProvider;

    @Mock
    private MessageDigestAlgorithm messageDigestAlgorithm;

    @Mock
    private Salt salt;

    private byte [] saltBytes = {2, 2, 2, 2, 2};

    private byte [] digestResult = {4, 4, 4, 4, 4};

    private String password = "HelloWorld";

    private PasswordDigestAlgorithm algorithm;

    @Before
    public void setUp() throws Exception {
        when(messageDigestAlgorithmProvider.get()).thenReturn(messageDigestAlgorithm);
        algorithm = new PasswordDigestAlgorithm(messageDigestAlgorithmProvider);
        when(messageDigestAlgorithm.computeDigest()).thenReturn(digestResult);
        when(salt.getBytes()).thenReturn(saltBytes);
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

    @Test
    public void shouldUseUtf8EncodingForPassword() throws UnsupportedEncodingException {
        algorithm.getDigestOfSaltedPassword(password, salt);
        verify(messageDigestAlgorithm, times(1)).update(password.getBytes("utf-8"));
    }

    @Test
    public void shouldPlaceSaltFirstAndPasswordSecond() throws UnsupportedEncodingException {
        InOrder inOrder = Mockito.inOrder(messageDigestAlgorithm);
        algorithm.getDigestOfSaltedPassword(password, salt);
        inOrder.verify(messageDigestAlgorithm, times(1)).update(salt.getBytes());
        inOrder.verify(messageDigestAlgorithm, times(1)).update(password.getBytes("utf-8"));
    }

    @Test
    public void shouldReturnDigestAlgorithmResult() {
        assertThat(algorithm.getDigestOfSaltedPassword(password, salt), is(digestResult));
    }
}
