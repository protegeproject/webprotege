package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.shared.auth.ChallengeMessage;
import edu.stanford.bmir.protege.web.shared.auth.ChapResponse;
import edu.stanford.bmir.protege.web.shared.auth.MessageDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ChapResponseChecker_TestCase {

    private ChapResponseChecker checker;

    @Mock
    private Provider<MessageDigestAlgorithm> digestAlgorithmProvider;

    @Mock
    private MessageDigestAlgorithm digestAlgorithm;

    private byte[] digest = {3, 3, 3, 3};

    private ChapResponse chapResponse;

    @Mock
    private ChallengeMessage challengeMessage;

    private byte [] challengeMessageBytes = {4, 5, 6};

    @Mock
    private SaltedPasswordDigest saltedPasswordDigest;

    private byte [] saltedPasswordBytes = {7, 8, 9};


    @Before
    public void setUp() throws Exception {
        chapResponse = new ChapResponse(digest);
        when(digestAlgorithmProvider.get()).thenReturn(digestAlgorithm);
        when(digestAlgorithm.computeDigest()).thenReturn(digest);
        checker = new ChapResponseChecker(digestAlgorithmProvider);

        when(challengeMessage.getBytes()).thenReturn(challengeMessageBytes);
        when(saltedPasswordDigest.getBytes()).thenReturn(saltedPasswordBytes);
    }

    @Test
    public void shouldUpdateDigestInOrder() {
        checker.isExpectedResponse(chapResponse, challengeMessage, saltedPasswordDigest);
        InOrder inOrder = Mockito.inOrder(digestAlgorithm);
        inOrder.verify(digestAlgorithm).update(challengeMessageBytes);
        inOrder.verify(digestAlgorithm).update(saltedPasswordBytes);
    }

    @Test
    public void shouldBeExpectedResponse() {
        boolean expected = checker.isExpectedResponse(chapResponse, challengeMessage, saltedPasswordDigest);
        assertThat(expected, is(true));
    }
}
