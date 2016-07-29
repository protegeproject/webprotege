
package edu.stanford.bmir.protege.web.shared.place;

import edu.stanford.bmir.protege.web.client.project.ProjectPresenter;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ProjectViewActivity_TestCase {

    private ProjectViewActivity projectViewActivity;
    @Mock
    private ProjectPresenter projectPresenter;
    @Mock
    private ProjectViewPlace place;

    @Before
    public void setUp()
        throws Exception
    {
        projectViewActivity = new ProjectViewActivity(projectPresenter, place);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectPresenter_IsNull() {
        new ProjectViewActivity(null, place);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_place_IsNull() {
        new ProjectViewActivity(projectPresenter, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(projectViewActivity, is(projectViewActivity));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(projectViewActivity.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(projectViewActivity, is(new ProjectViewActivity(projectPresenter, place)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectPresenter() {
        assertThat(projectViewActivity, is(not(new ProjectViewActivity(mock(ProjectPresenter.class), place))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(projectViewActivity.hashCode(), is(new ProjectViewActivity(projectPresenter, place).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(projectViewActivity.toString(), Matchers.startsWith("ProjectViewActivity"));
    }
}
