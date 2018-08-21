
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
        getProjectDetailsResult = GetProjectDetailsResult.get(projectDetails);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectDetails_IsNull() {
        GetProjectDetailsResult.get(null);
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
        assertThat(getProjectDetailsResult, is(GetProjectDetailsResult.get(projectDetails)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectDetails() {
        assertThat(getProjectDetailsResult, is(not(GetProjectDetailsResult.get(Mockito.mock(ProjectDetails.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getProjectDetailsResult.hashCode(), is(GetProjectDetailsResult.get(projectDetails).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getProjectDetailsResult.toString(), startsWith("GetProjectDetailsResult"));
    }

}
