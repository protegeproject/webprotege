
package edu.stanford.bmir.protege.web.shared.issues;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(MockitoJUnitRunner.class)
public class Milestone_TestCase {

    private Milestone milestone;

    private String label = "The label";

    @Before
    public void setUp() {
        milestone = new Milestone(label);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_label_IsNull() {
        new Milestone(null);
    }

    @Test
    public void shouldReturnSupplied_label() {
        assertThat(milestone.getLabel(), is(this.label));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(milestone, is(milestone));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(milestone.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(milestone, is(new Milestone(label)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_label() {
        assertThat(milestone, is(not(new Milestone("Other Milestone"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(milestone.hashCode(), is(new Milestone(label).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(milestone.toString(), Matchers.startsWith("Milestone"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowEmptyLabel() {
        new Milestone("");
    }

}
