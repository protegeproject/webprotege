
package edu.stanford.bmir.protege.web.shared.itemlist;

import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class GetPersonIdCompletionsResult_TestCase {

    private GetPersonIdCompletionsResult result;

    @Mock
    private PersonId personId;

    private List<PersonId> possibleItemCompletions;

    @Before
    public void setUp()
        throws Exception
    {
        possibleItemCompletions = new ArrayList<>();
        possibleItemCompletions.add(personId);
        result = new GetPersonIdCompletionsResult(possibleItemCompletions);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_possibleItemCompletions_IsNull() {
        new GetPersonIdCompletionsResult(null);
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
        MatcherAssert.assertThat(result, Matchers.is(new GetPersonIdCompletionsResult(possibleItemCompletions)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_possibleItemCompletions() {
        MatcherAssert.assertThat(result, Matchers.is(Matchers.not(new GetPersonIdCompletionsResult(Arrays.asList(mock(PersonId.class))))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(result.hashCode(), Matchers.is(new GetPersonIdCompletionsResult(possibleItemCompletions).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(result.toString(), Matchers.startsWith("GetPersonIdCompletionsResult"));
    }

}
