package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Optional;
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
 * 17/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetChapSessionResult_TestCase {


    private GetChapSessionResult result;

    private GetChapSessionResult otherResult;


    @Mock
    private Optional<ChapSession> chapSession;

    @Before
    public void setUp() throws Exception {
        result = new GetChapSessionResult(chapSession);
        otherResult = new GetChapSessionResult(chapSession);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new GetChapSessionResult(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(result, is(equalTo(result)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(result, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(result, is(equalTo(otherResult)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(result.hashCode(), is(otherResult.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(result.toString(), startsWith("GetChapSessionResult"));
    }

    @Test
    public void shouldReturnSuppliedChapData() {
        assertThat(result.getChapSession(), is(chapSession));
    }
}