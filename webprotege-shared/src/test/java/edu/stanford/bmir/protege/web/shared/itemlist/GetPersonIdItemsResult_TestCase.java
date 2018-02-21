
package edu.stanford.bmir.protege.web.shared.itemlist;

import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class GetPersonIdItemsResult_TestCase {

    private GetPersonIdItemsResult result;

    private List<PersonId> items;

    @Before
    public void setUp()
        throws Exception
    {
        items = new ArrayList<>();
        result = new GetPersonIdItemsResult(items);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_items_IsNull() {
        new GetPersonIdItemsResult(null);
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
        MatcherAssert.assertThat(result, Matchers.is(new GetPersonIdItemsResult(items)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_items() {
        List<PersonId> otherItems = Arrays.asList(mock(PersonId.class));
        MatcherAssert.assertThat(result, Matchers.is(Matchers.not(new GetPersonIdItemsResult(otherItems))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(result.hashCode(), Matchers.is(new GetPersonIdItemsResult(items).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(result.toString(), Matchers.startsWith("GetPersonIdItemsResult"));
    }

}
