
package edu.stanford.bmir.protege.web.shared.project;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class GetProjectDetailsResult_TestCase {

    private GetProjectDetailsResult getProjectDetailsResult;
    @Mock
    private ProjectDetails projectDetails;

    @Before
    public void setUp()
    {
        getProjectDetailsResult = new GetProjectDetailsResult(projectDetails);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectDetails_IsNull() {
        new GetProjectDetailsResult(null);
    }

    @Test
    public void shouldReturnSupplied_projectDetails() {
        assertThat(getProjectDetailsResult.getProjectDetails(), is(this.projectDetails));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getProjectDetailsResult, is(getProjectDetailsResult));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(getProjectDetailsResult.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getProjectDetailsResult, is(new GetProjectDetailsResult(projectDetails)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectDetails() {
        assertThat(getProjectDetailsResult, is(not(new GetProjectDetailsResult(Mockito.mock(ProjectDetails.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getProjectDetailsResult.hashCode(), is(new GetProjectDetailsResult(projectDetails).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getProjectDetailsResult.toString(), startsWith("GetProjectDetailsResult"));
    }

}
