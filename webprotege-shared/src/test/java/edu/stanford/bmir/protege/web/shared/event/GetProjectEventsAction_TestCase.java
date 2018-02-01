
package edu.stanford.bmir.protege.web.shared.event;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GetProjectEventsAction_TestCase {

    private GetProjectEventsAction action;

    @Mock
    private EventTag sinceTag;

    @Mock
    private ProjectId projectId;

    @Before
    public void setUp() {
        action = new GetProjectEventsAction(sinceTag, projectId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_sinceTag_IsNull() {
        new GetProjectEventsAction(null, projectId);
    }

    @Test
    public void shouldReturnSupplied_sinceTag() {
        assertThat(action.getSinceTag(), is(this.sinceTag));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new GetProjectEventsAction(sinceTag, null);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(action.getProjectId(), is(this.projectId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(action, is(action));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(action.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(action, is(new GetProjectEventsAction(sinceTag, projectId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_sinceTag() {
        assertThat(action, is(not(new GetProjectEventsAction(mock(EventTag.class), projectId))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(action, is(not(new GetProjectEventsAction(sinceTag, mock(ProjectId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(action.hashCode(), is(new GetProjectEventsAction(sinceTag, projectId).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(action.toString(), Matchers.startsWith("GetProjectEventsAction"));
    }
}
