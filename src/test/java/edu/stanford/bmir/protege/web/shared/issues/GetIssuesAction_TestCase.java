
package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
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
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GetIssuesAction_TestCase {

    private GetIssuesAction getIssuesAction;

    @Mock
    private ProjectId projectId;

    @Before
    public void setUp() {
        getIssuesAction = new GetIssuesAction(projectId);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new GetIssuesAction(null);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(getIssuesAction.getProjectId(), is(this.projectId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getIssuesAction, is(getIssuesAction));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(getIssuesAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getIssuesAction, is(new GetIssuesAction(projectId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(getIssuesAction, is(not(new GetIssuesAction(mock(ProjectId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getIssuesAction.hashCode(), is(new GetIssuesAction(projectId).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getIssuesAction.toString(), Matchers.startsWith("GetIssuesAction"));
    }

}
