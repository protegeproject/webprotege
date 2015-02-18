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
public class ChapSession_TestCase {


    private ChapSession chapSession;

    private ChapSession otherChapSession;

    @Mock
    private ChapSessionId id;

    @Mock
    private ChallengeMessage challenge;

    @Mock
    private Salt salt;

    @Before
    public void setUp() throws Exception {
        chapSession = new ChapSession(id, challenge, salt);
        otherChapSession = new ChapSession(id, challenge, salt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Id_IsNull() {
        new ChapSession(null, challenge, salt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Challenge_IsNull() {
        new ChapSession(id, null, salt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Salt_IsNull() {
        new ChapSession(id, challenge, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(chapSession, is(equalTo(chapSession)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(chapSession, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(chapSession, is(equalTo(otherChapSession)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(chapSession.hashCode(), is(otherChapSession.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(chapSession.toString(), startsWith("ChapSession"));
    }

    @Test
    public void shouldReturnSuppliedChallenge() {
        assertThat(chapSession.getChallengeMessage(), is(challenge));
    }

    @Test
    public void shouldReturnSuppliedSalt() {
        assertThat(chapSession.getSalt(), is(salt));
    }

    @Test
    public void shouldReturnSuppliedId() {
        assertThat(chapSession.getId(), is(id));
    }

}