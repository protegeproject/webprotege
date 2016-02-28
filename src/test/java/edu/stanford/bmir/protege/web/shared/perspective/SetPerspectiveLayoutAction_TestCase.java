
package edu.stanford.bmir.protege.web.shared.perspective;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.NullPointerException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SetPerspectiveLayoutAction_TestCase {

    private SetPerspectiveLayoutAction setPerspectiveLayoutAction;
    @Mock
    private ProjectId projectId;
    @Mock
    private UserId userId;
    @Mock
    private PerspectiveLayout layout;

    @Before
    public void setUp()
        throws Exception
    {
        setPerspectiveLayoutAction = new SetPerspectiveLayoutAction(projectId, userId, layout);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new SetPerspectiveLayoutAction(null, userId, layout);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(setPerspectiveLayoutAction.getProjectId(), is(this.projectId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new SetPerspectiveLayoutAction(projectId, null, layout);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(setPerspectiveLayoutAction.getUserId(), is(this.userId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_layout_IsNull() {
        new SetPerspectiveLayoutAction(projectId, userId, null);
    }

    @Test
    public void shouldReturnSupplied_layout() {
        assertThat(setPerspectiveLayoutAction.getLayout(), is(this.layout));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(setPerspectiveLayoutAction, is(setPerspectiveLayoutAction));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(setPerspectiveLayoutAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(setPerspectiveLayoutAction, is(new SetPerspectiveLayoutAction(projectId, userId, layout)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(setPerspectiveLayoutAction, is(Matchers.not(new SetPerspectiveLayoutAction(mock(ProjectId.class), userId, layout))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(setPerspectiveLayoutAction, is(Matchers.not(new SetPerspectiveLayoutAction(projectId, mock(UserId.class), layout))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_layout() {
        assertThat(setPerspectiveLayoutAction, is(Matchers.not(new SetPerspectiveLayoutAction(projectId, userId, mock(PerspectiveLayout.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(setPerspectiveLayoutAction.hashCode(), is(new SetPerspectiveLayoutAction(projectId, userId, layout).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(setPerspectiveLayoutAction.toString(), Matchers.startsWith("SetPerspectiveLayoutAction"));
    }

}
