package edu.stanford.bmir.protege.web.shared.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ChallengeMessage_TestCase {


    private ChallengeMessage challengeMessage;

    private ChallengeMessage otherChallengeMessage;


    private byte [] message = {1, 2, 3, 4, 5, 6, 7, 8};

    @Before
    public void setUp() throws Exception {
        challengeMessage = new ChallengeMessage(message);
        otherChallengeMessage = new ChallengeMessage(message);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new ChallengeMessage(null);
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
    public void shouldReturnSuppliedMessage() {
        assertThat(challengeMessage.getBytes(), is(message));
    }
}