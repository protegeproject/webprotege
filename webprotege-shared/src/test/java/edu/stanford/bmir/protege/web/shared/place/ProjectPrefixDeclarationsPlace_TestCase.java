
package edu.stanford.bmir.protege.web.shared.place;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@RunWith(MockitoJUnitRunner.class)
public class ProjectPrefixDeclarationsPlace_TestCase {

    private ProjectPrefixDeclarationsPlace place;

    private ProjectId projectId;

    @Before
    public void setUp() {
        projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");
        place = new ProjectPrefixDeclarationsPlace(projectId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new ProjectPrefixDeclarationsPlace(null);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(place.getProjectId(), is(this.projectId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(place, is(place));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(place.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(place, is(new ProjectPrefixDeclarationsPlace(projectId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(place, is(not(new ProjectPrefixDeclarationsPlace(Mockito.mock(ProjectId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(place.hashCode(), is(new ProjectPrefixDeclarationsPlace(projectId).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(place.toString(), startsWith("ProjectPrefixDeclarationsPlace"));
    }

}
