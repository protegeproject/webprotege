package edu.stanford.bmir.protege.web.shared.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ChapResponseDigestAlgorithm_TestCase {

    private ChapResponseDigestAlgorithm algorithm;

    @Mock
    private Provider<MessageDigestAlgorithm> digestAlgorithmProvider;

    @Mock
    private MessageDigestAlgorithm messageDigestAlgorithm;

    @Mock
    private SaltedPasswordDigest digestOfSaltedPassword;

    @Mock
    private ChallengeMessage challengeMessage;

    private byte [] challengeMessageBytes = {3, 3, 3, 3, 3};

    private byte [] result = {6, 6, 6, 6, 6};

    @Before
    public void setUp() throws Exception {
        when(digestAlgorithmProvider.get()).thenReturn(messageDigestAlgorithm);
        algorithm = new ChapResponseDigestAlgorithm(digestAlgorithmProvider);
        when(messageDigestAlgorithm.computeDigest()).thenReturn(result);
        when(challengeMessage.getBytes()).thenReturn(challengeMessageBytes);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Provider_IsNull() {
        new ChapResponseDigestAlgorithm(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Message_IsNull() {
        algorithm.getChapResponseDigest(null, digestOfSaltedPassword);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Password_IsNull() {
        algorithm.getChapResponseDigest(challengeMessage, null);
    }

    @Test
    public void shouldComputeDigestWithChallengeFirstAndPasswordSecond() throws Exception {
        algorithm.getChapResponseDigest(challengeMessage, digestOfSaltedPassword);
        InOrder inOrder = inOrder(messageDigestAlgorithm);
        inOrder.verify(messageDigestAlgorithm, times(1)).update(challengeMessage.getBytes());
        inOrder.verify(messageDigestAlgorithm, times(1)).update(digestOfSaltedPassword.getBytes());
    }

    @Test
    public void shouldReturnResult() {
        ChapResponse expectedResult = new ChapResponse(result);
        assertThat(algorithm.getChapResponseDigest(challengeMessage, digestOfSaltedPassword), is(expectedResult));
    }
}
