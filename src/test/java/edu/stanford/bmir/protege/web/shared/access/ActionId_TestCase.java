package edu.stanford.bmir.protege.web.shared.access;


import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class ActionId_TestCase {

    private ActionId actionId;

    private String id = "The id";

    @Before
    public void setUp() {
        actionId = new ActionId(id);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_id_IsNull() {
        new ActionId(null);
    }

    @Test
    public void shouldReturnSupplied_id() {
        assertThat(actionId.getId(), is(this.id));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(actionId, is(actionId));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(actionId.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(actionId, is(new ActionId(id)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_id() {
        assertThat(actionId, is(not(new ActionId("String-49f80fc5-f0c4-4013-accc-4f37f60d5632"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(actionId.hashCode(), is(new ActionId(id).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(actionId.toString(), Matchers.startsWith("ActionId"));
    }

}
