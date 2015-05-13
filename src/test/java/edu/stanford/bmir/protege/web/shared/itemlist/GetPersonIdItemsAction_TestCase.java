
package edu.stanford.bmir.protege.web.shared.itemlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class GetPersonIdItemsAction_TestCase {

    private GetPersonIdItemsAction action;

    private List<String> itemNames;

    @Before
    public void setUp()
        throws Exception
    {
        itemNames = new ArrayList<>();
        action = new GetPersonIdItemsAction(itemNames);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_itemNames_IsNull() {
        new GetPersonIdItemsAction(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(action, Matchers.is(action));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(action.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(action, Matchers.is(new GetPersonIdItemsAction(itemNames)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_itemNames() {
        List<String> otherItemNames = Arrays.asList("Other Item");
        MatcherAssert.assertThat(action, Matchers.is(Matchers.not(new GetPersonIdItemsAction(otherItemNames))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(action.hashCode(), Matchers.is(new GetPersonIdItemsAction(itemNames).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(action.toString(), Matchers.startsWith("GetPersonIdItemsAction"));
    }

}
