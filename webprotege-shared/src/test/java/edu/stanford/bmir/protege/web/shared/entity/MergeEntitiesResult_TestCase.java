
package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@RunWith(MockitoJUnitRunner.class)
public class MergeEntitiesResult_TestCase {

    private MergeEntitiesResult mergeEntitiesResult;
    @Mock
    private EventList<ProjectEvent<?>> eventList;

    @Before
    public void setUp() {
        mergeEntitiesResult = new MergeEntitiesResult(eventList);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_eventList_IsNull() {
        new MergeEntitiesResult(null);
    }

    @Test
    public void shouldReturnSupplied_eventList() {
        assertThat(mergeEntitiesResult.getEventList(), is(this.eventList));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(mergeEntitiesResult, is(mergeEntitiesResult));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(mergeEntitiesResult.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(mergeEntitiesResult, is(new MergeEntitiesResult(eventList)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_eventList() {
        assertThat(mergeEntitiesResult, is(not(new MergeEntitiesResult(Mockito.mock(EventList.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(mergeEntitiesResult.hashCode(), is(new MergeEntitiesResult(eventList).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(mergeEntitiesResult.toString(), startsWith("MergeEntitiesResult"));
    }

}
