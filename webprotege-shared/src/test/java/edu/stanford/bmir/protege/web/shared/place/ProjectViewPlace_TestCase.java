
package edu.stanford.bmir.protege.web.shared.place;

import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class ProjectViewPlace_TestCase {

    private ProjectViewPlace projectViewPlace;

    @Mock
    private ProjectId projectId;

    @Mock
    private PerspectiveId perspectiveId;

    @Mock
    private ItemSelection itemSelection;

    @Before
    public void setUp() throws Exception {
        projectViewPlace = new ProjectViewPlace(projectId, perspectiveId, itemSelection);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new ProjectViewPlace(null, perspectiveId, itemSelection);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        MatcherAssert.assertThat(projectViewPlace.getProjectId(), Matchers.is(this.projectId));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_perspectiveId_IsNull() {
        new ProjectViewPlace(projectId, null, itemSelection);
    }

    @Test
    public void shouldReturnSupplied_perspectiveId() {
        MatcherAssert.assertThat(projectViewPlace.getPerspectiveId(), Matchers.is(this.perspectiveId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(projectViewPlace, Matchers.is(projectViewPlace));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(projectViewPlace.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(projectViewPlace, Matchers.is(new ProjectViewPlace(projectId, perspectiveId, itemSelection)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        MatcherAssert.assertThat(projectViewPlace, Matchers.is(Matchers.not(new ProjectViewPlace(Mockito.mock(ProjectId.class), perspectiveId, itemSelection))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_perspectiveId() {
        MatcherAssert.assertThat(projectViewPlace, Matchers.is(Matchers.not(new ProjectViewPlace(projectId, Mockito.mock(PerspectiveId.class), itemSelection))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(projectViewPlace.hashCode(), Matchers.is(new ProjectViewPlace(projectId, perspectiveId, itemSelection).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(projectViewPlace.toString(), Matchers.startsWith("ProjectViewPlace"));
    }

    @Test
    public void should_getItemSelection() {
        MatcherAssert.assertThat(projectViewPlace.getItemSelection(), Matchers.is(itemSelection));
    }
}
