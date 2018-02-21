
package edu.stanford.bmir.protege.web.shared.change;

import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class RevertRevisionResult_TestCase {

    private RevertRevisionResult revertRevisionResult;

    @Mock
    private ProjectId projectId;

    @Mock
    private EventList<ProjectEvent<?>> eventList;

    @Before
    public void setUp() {
        revertRevisionResult = new RevertRevisionResult(projectId, eventList);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new RevertRevisionResult(null, eventList);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        MatcherAssert.assertThat(revertRevisionResult.getProjectId(), Matchers.is(this.projectId));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_eventList_IsNull() {
        new RevertRevisionResult(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_eventList() {
        MatcherAssert.assertThat(revertRevisionResult.getEventList(), Matchers.is(this.eventList));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(revertRevisionResult, Matchers.is(revertRevisionResult));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(revertRevisionResult.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(revertRevisionResult, Matchers.is(new RevertRevisionResult(projectId, eventList)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        MatcherAssert.assertThat(revertRevisionResult, Matchers.is(Matchers.not(new RevertRevisionResult(Mockito.mock(ProjectId.class), eventList))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_eventList() {
        MatcherAssert.assertThat(revertRevisionResult, Matchers.is(Matchers.not(new RevertRevisionResult(projectId, Mockito.mock(EventList.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(revertRevisionResult.hashCode(), Matchers.is(new RevertRevisionResult(projectId, eventList).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(revertRevisionResult.toString(), Matchers.startsWith("RevertRevisionResult"));
    }
}
