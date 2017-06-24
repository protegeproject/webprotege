
package edu.stanford.bmir.protege.web.shared.obo;

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
public class GetOboNamespacesAction_TestCase {

    private GetOboNamespacesAction getOboNamespacesAction;
    @Mock
    private ProjectId projectId;

    @Before
    public void setUp() {
        getOboNamespacesAction = new GetOboNamespacesAction(projectId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new GetOboNamespacesAction(null);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(getOboNamespacesAction.getProjectId(), is(this.projectId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboNamespacesAction, is(getOboNamespacesAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboNamespacesAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboNamespacesAction, is(new GetOboNamespacesAction(projectId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(getOboNamespacesAction, is(not(new GetOboNamespacesAction(mock(ProjectId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboNamespacesAction.hashCode(), is(new GetOboNamespacesAction(projectId).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboNamespacesAction.toString(), Matchers.startsWith("GetOboNamespacesAction"));
    }
}
