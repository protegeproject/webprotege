
package edu.stanford.bmir.protege.web.shared.sharing;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class PersonId_TestCase {

    private PersonId personId;
    private String id = "The id";

    @Before
    public void setUp()
        throws Exception
    {
        personId = PersonId.get(id);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_id_IsNull() {
        PersonId.get(null);
    }

    @Test
    public void shouldReturnSupplied_id() {
        MatcherAssert.assertThat(personId.getId(), Matchers.is(this.id));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(personId, Matchers.is(personId));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(personId.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(personId, Matchers.is(PersonId.get(id)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_id() {
        MatcherAssert.assertThat(personId, Matchers.is(Matchers.not(PersonId.get("String-42926bfe-4f72-4c74-99aa-aef99f86dabe"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(personId.hashCode(), Matchers.is(PersonId.get(id).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(personId.toString(), Matchers.startsWith("PersonId"));
    }

}
