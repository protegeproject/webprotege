
package edu.stanford.bmir.protege.web.shared.place;

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
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ProjectTagsPlace_TestCase {

    private ProjectTagsPlace place;

    @Mock
    private ProjectId projectId;

    @Before
    public void setUp() {
        place = new ProjectTagsPlace(projectId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new ProjectTagsPlace(null);
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
        assertThat(place, is(new ProjectTagsPlace(projectId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(place, is(not(new ProjectTagsPlace(mock(ProjectId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(place.hashCode(), is(new ProjectTagsPlace(projectId).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(place.toString(), startsWith("ProjectTagsPlace"));
    }

}
