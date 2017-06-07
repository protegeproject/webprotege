
package edu.stanford.bmir.protege.web.shared.place;

import java.util.Optional;

import com.google.gwt.place.shared.Place;
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

@RunWith(value = MockitoJUnitRunner.class)
public class ProjectSettingsPlace_TestCase {

    private ProjectSettingsPlace projectSettingsPlace;

    @Mock
    private ProjectId projectId;

    private Optional<Place> nextPlace = Optional.empty();

    @Before
    public void setUp() {
        projectSettingsPlace = new ProjectSettingsPlace(projectId, nextPlace);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new ProjectSettingsPlace(null, nextPlace);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(projectSettingsPlace.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_nextPlace_IsNull() {
        new ProjectSettingsPlace(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_nextPlace() {
        assertThat(projectSettingsPlace.getNextPlace(), is(this.nextPlace));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(projectSettingsPlace, is(projectSettingsPlace));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(projectSettingsPlace.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(projectSettingsPlace, is(new ProjectSettingsPlace(projectId, nextPlace)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(projectSettingsPlace, is(not(new ProjectSettingsPlace(mock(ProjectId.class), nextPlace))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_nextPlace() {
        assertThat(projectSettingsPlace, is(not(new ProjectSettingsPlace(projectId, Optional.of(mock(Place.class))))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(projectSettingsPlace.hashCode(), is(new ProjectSettingsPlace(projectId, nextPlace).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(projectSettingsPlace.toString(), startsWith("ProjectSettingsPlace"));
    }

}
