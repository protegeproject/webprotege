
package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class LoadProjectResult_TestCase {

    private LoadProjectResult loadProjectResult;

    @Mock
    private ProjectId projectId;

    @Mock
    private UserId loadedBy;

    @Mock
    private ProjectDetails projectDetails;

    @Before
    public void setUp()
            throws Exception {
        loadProjectResult = LoadProjectResult.get(projectId, loadedBy, projectDetails);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        LoadProjectResult.get(null, loadedBy, projectDetails);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        MatcherAssert.assertThat(loadProjectResult.getProjectId(), Matchers.is(this.projectId));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_loadedBy_IsNull() {
        LoadProjectResult.get(projectId, null, projectDetails);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectDetails_IsNull() {
        LoadProjectResult.get(projectId, loadedBy, null);
    }

    @Test
    public void shouldReturnSupplied_projectDetails() {
        MatcherAssert.assertThat(loadProjectResult.getProjectDetails(), Matchers.is(this.projectDetails));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(loadProjectResult, Matchers.is(loadProjectResult));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(loadProjectResult.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(loadProjectResult,
                                 Matchers.is(LoadProjectResult.get(projectId, loadedBy, projectDetails)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        MatcherAssert.assertThat(loadProjectResult,
                                 Matchers.is(Matchers.not(LoadProjectResult.get(Mockito.mock(ProjectId.class),
                                                                                loadedBy,
                                                                                projectDetails))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_loadedBy() {
        MatcherAssert.assertThat(loadProjectResult,
                                 Matchers.is(Matchers.not(LoadProjectResult.get(projectId,
                                                                                Mockito.mock(UserId.class),
                                                                                projectDetails))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectDetails() {
        MatcherAssert.assertThat(loadProjectResult,
                                 Matchers.is(Matchers.not(LoadProjectResult.get(projectId,
                                                                                loadedBy,
                                                                                Mockito.mock(ProjectDetails.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(loadProjectResult.hashCode(),
                                 Matchers.is(LoadProjectResult.get(projectId, loadedBy, projectDetails).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(loadProjectResult.toString(), Matchers.startsWith("LoadProjectResult"));
    }

    @Test
    public void should_getUserId() {
        MatcherAssert.assertThat(loadProjectResult.getUserId(), Matchers.is(loadedBy));
    }
}
