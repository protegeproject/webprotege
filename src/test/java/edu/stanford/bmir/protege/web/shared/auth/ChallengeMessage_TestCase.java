package edu.stanford.bmir.protege.web.shared.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ChallengeMessage_TestCase {


    private ChallengeMessage challengeMessage;

    private ChallengeMessage otherChallengeMessage;

    @Mock
    private ChallengeMessageId id;


    private byte [] challenge = {3, 3, 3, 3};

    private byte [] salt = {2, 2, 2, 2, 2, 2, 2};

    @Before
    public void setUp() throws Exception {
        challengeMessage = new ChallengeMessage(id, challenge, salt);
        otherChallengeMessage = new ChallengeMessage(id, challenge, salt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Id_IsNull() {
        new ChallengeMessage(null, challenge, salt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Challenge_IsNull() {
        new ChallengeMessage(id, null, salt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Salt_IsNull() {
        new ChallengeMessage(id, challenge, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(challengeMessage, is(equalTo(challengeMessage)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(challengeMessage, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(challengeMessage, is(equalTo(otherChallengeMessage)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(challengeMessage.hashCode(), is(otherChallengeMessage.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(challengeMessage.toString(), startsWith("ChallengeMessage"));
    }

    @Test
    public void shouldReturnSuppliedChallenge() {
        assertThat(challengeMessage.getChallenge(), is(challenge));
    }

    @Test
    public void shouldReturnSuppliedSalt() {
        assertThat(challengeMessage.getSalt(), is(salt));
    }

    @Test
    public void shouldReturnSuppliedId() {
        assertThat(challengeMessage.getId(), is(id));
    }

}