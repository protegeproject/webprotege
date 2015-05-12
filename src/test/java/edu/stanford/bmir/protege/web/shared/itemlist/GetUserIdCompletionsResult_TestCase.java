
package edu.stanford.bmir.protege.web.shared.itemlist;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class GetUserIdCompletionsResult_TestCase {

    private GetUserIdCompletionsResult result;

    @Mock
    private UserId userId;

    private List<UserId> possibleItemCompletions;

    @Before
    public void setUp()
        throws Exception
    {
        possibleItemCompletions = Arrays.asList(userId);
        result = new GetUserIdCompletionsResult(possibleItemCompletions);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_possibleItemCompletions_IsNull() {
        new GetUserIdCompletionsResult(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(result, Matchers.is(result));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(result.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(result, Matchers.is(new GetUserIdCompletionsResult(possibleItemCompletions)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_possibleItemCompletions() {
        MatcherAssert.assertThat(result, Matchers.is(Matchers.not(new GetUserIdCompletionsResult(Arrays.asList(mock(UserId.class))))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(result.hashCode(), Matchers.is(new GetUserIdCompletionsResult(possibleItemCompletions).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(result.toString(), Matchers.startsWith("GetUserIdCompletionsResult"));
    }

}
