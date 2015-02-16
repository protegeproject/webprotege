package edu.stanford.bmir.protege.web.shared.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class ChallengeMessageId_TestCase {

    public static final String ID = "testId";

    private ChallengeMessageId id;

    private ChallengeMessageId otherId;


    @Before
    public void setUp() throws Exception {
        id = new ChallengeMessageId(ID);
        otherId = new ChallengeMessageId(ID);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new ChallengeMessageId(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(id, is(equalTo(id)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(id, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(id, is(equalTo(otherId)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(id.hashCode(), is(otherId.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(id.toString(), startsWith("ChallengeMessageId"));
    }

    @Test
    public void shouldReturnSuppliedId() {
        assertThat(id.getId(), is(ID));
    }
}