
package edu.stanford.bmir.protege.web.server.access;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ProjectResource_TestCase {

    private ProjectResource projectResource;

    @Mock
    private ProjectId projectId;

    @Before
    public void setUp() {
        projectResource = new ProjectResource(projectId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new ProjectResource(null);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(projectResource.getProjectId(), is(Optional.of(this.projectId)));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(projectResource, is(projectResource));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(projectResource.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(projectResource, is(new ProjectResource(projectId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(projectResource, is(not(new ProjectResource(mock(ProjectId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(projectResource.hashCode(), is(new ProjectResource(projectId).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(projectResource.toString(), startsWith("ProjectResource"));
    }

    @Test
    public void shouldReturn_true_For_isProjectTarget() {
        assertThat(projectResource.isProject(projectId), is(true));
    }

    @Test
    public void shouldReturn_false_For_isProjectTarget() {
        assertThat(projectResource.isProject(mock(ProjectId.class)), is(false));
    }

    @Test
    public void shouldReturn_false_For_isApplicationTarget() {
        assertThat(projectResource.isApplication(), is(false));
    }

}
