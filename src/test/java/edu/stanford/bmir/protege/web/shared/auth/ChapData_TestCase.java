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
public class ChapData_TestCase {


    private ChapData chapData;

    private ChapData otherChapData;

    @Mock
    private ChapSessionId id;

    @Mock
    private ChallengeMessage challenge;

    @Mock
    private Salt salt;

    @Before
    public void setUp() throws Exception {
        chapData = new ChapData(id, challenge, salt);
        otherChapData = new ChapData(id, challenge, salt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Id_IsNull() {
        new ChapData(null, challenge, salt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Challenge_IsNull() {
        new ChapData(id, null, salt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Salt_IsNull() {
        new ChapData(id, challenge, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(chapData, is(equalTo(chapData)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(chapData, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(chapData, is(equalTo(otherChapData)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(chapData.hashCode(), is(otherChapData.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(chapData.toString(), startsWith("ChallengeMessage"));
    }

    @Test
    public void shouldReturnSuppliedChallenge() {
        assertThat(chapData.getChallengeMessage(), is(challenge));
    }

    @Test
    public void shouldReturnSuppliedSalt() {
        assertThat(chapData.getSalt(), is(salt));
    }

    @Test
    public void shouldReturnSuppliedId() {
        assertThat(chapData.getId(), is(id));
    }

}