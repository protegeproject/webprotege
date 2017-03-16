
package edu.stanford.bmir.protege.web.shared.perspective;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
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
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ResetPerspectiveLayoutAction_TestCase {

    private ResetPerspectiveLayoutAction resetPerspectiveLayoutAction;
    @Mock
    private ProjectId projectId;
    @Mock
    private PerspectiveId perspectiveId;

    @Before
    public void setUp()
        throws Exception
    {
        resetPerspectiveLayoutAction = new ResetPerspectiveLayoutAction(projectId, perspectiveId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new ResetPerspectiveLayoutAction(null, perspectiveId);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(resetPerspectiveLayoutAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_perspectiveId_IsNull() {
        new ResetPerspectiveLayoutAction(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_perspectiveId() {
        assertThat(resetPerspectiveLayoutAction.getPerspectiveId(), is(this.perspectiveId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(resetPerspectiveLayoutAction, is(resetPerspectiveLayoutAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(resetPerspectiveLayoutAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(resetPerspectiveLayoutAction, is(new ResetPerspectiveLayoutAction(projectId, perspectiveId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(resetPerspectiveLayoutAction, is(not(new ResetPerspectiveLayoutAction(mock(ProjectId.class), perspectiveId))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_perspectiveId() {
        assertThat(resetPerspectiveLayoutAction, is(not(new ResetPerspectiveLayoutAction(projectId, mock(PerspectiveId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(resetPerspectiveLayoutAction.hashCode(), is(new ResetPerspectiveLayoutAction(projectId, perspectiveId).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(resetPerspectiveLayoutAction.toString(), Matchers.startsWith("ResetPerspectiveLayoutAction"));
    }
}
