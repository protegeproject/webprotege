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
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ChallengeMessageDigestAlgorithm_TestCase {

    private ChallengeMessageDigestAlgorithm algorithm;

    @Mock
    private Provider<MessageDigestAlgorithm> digestAlgorithmProvider;

    @Mock
    private MessageDigestAlgorithm messageDigestAlgorithm;

    private byte [] digestOfSaltedPassword = {4, 4, 4, 4, 4};

    @Mock
    private ChallengeMessage challengeMessage;

    private byte [] challengeMessageBytes = {3, 3, 3, 3, 3};

    private byte [] result = {6, 6, 6, 6, 6};

    @Before
    public void setUp() throws Exception {
        when(digestAlgorithmProvider.get()).thenReturn(messageDigestAlgorithm);
        algorithm = new ChallengeMessageDigestAlgorithm(digestAlgorithmProvider);
        when(messageDigestAlgorithm.computeDigest()).thenReturn(result);
        when(challengeMessage.getBytes()).thenReturn(challengeMessageBytes);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Provider_IsNull() {
        new ChallengeMessageDigestAlgorithm(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Message_IsNull() {
        algorithm.getDigestOfMessageAndPassword(null, digestOfSaltedPassword);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Password_IsNull() {
        algorithm.getDigestOfMessageAndPassword(challengeMessage, null);
    }

    @Test
    public void shouldComputeDigestWithChallengeFirstAndPasswordSecond() throws Exception {
        algorithm.getDigestOfMessageAndPassword(challengeMessage, digestOfSaltedPassword);
        InOrder inOrder = inOrder(messageDigestAlgorithm);
        inOrder.verify(messageDigestAlgorithm, times(1)).update(challengeMessage.getBytes());
        inOrder.verify(messageDigestAlgorithm, times(1)).update(digestOfSaltedPassword);
    }

    @Test
    public void shouldReturnResult() {
        assertThat(algorithm.getDigestOfMessageAndPassword(challengeMessage, digestOfSaltedPassword), is(result));
    }
}
