
package edu.stanford.bmir.protege.web.shared.project;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class LoadProjectAction_TestCase {

    private LoadProjectAction loadProjectAction;
    @Mock
    private edu.stanford.bmir.protege.web.shared.project.ProjectId projectId;

    @Before
    public void setUp()
        throws Exception
    {
        loadProjectAction = new LoadProjectAction(projectId);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new LoadProjectAction(null);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        MatcherAssert.assertThat(loadProjectAction.getProjectId(), Matchers.is(this.projectId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(loadProjectAction, Matchers.is(loadProjectAction));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(loadProjectAction.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(loadProjectAction, Matchers.is(new LoadProjectAction(projectId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        MatcherAssert.assertThat(loadProjectAction, Matchers.is(Matchers.not(new LoadProjectAction(Mockito.mock(edu.stanford.bmir.protege.web.shared.project.ProjectId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(loadProjectAction.hashCode(), Matchers.is(new LoadProjectAction(projectId).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(loadProjectAction.toString(), Matchers.startsWith("LoadProjectAction"));
    }

}
