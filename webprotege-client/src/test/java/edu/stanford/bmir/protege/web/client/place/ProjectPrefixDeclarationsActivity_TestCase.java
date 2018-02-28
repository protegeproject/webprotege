
package edu.stanford.bmir.protege.web.client.place;

import edu.stanford.bmir.protege.web.client.project.ProjectPrefixDeclarationsPresenter;
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

@RunWith(MockitoJUnitRunner.class)
public class ProjectPrefixDeclarationsActivity_TestCase {

    private ProjectPrefixDeclarationsActivity activity;

    private ProjectId projectId;

    @Mock
    private ProjectPrefixDeclarationsPresenter presenter;

    @Before
    public void setUp() {
        projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");
        activity = new ProjectPrefixDeclarationsActivity(projectId, presenter);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new ProjectPrefixDeclarationsActivity(null, presenter);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_presenter_IsNull() {
        new ProjectPrefixDeclarationsActivity(projectId, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(activity, is(activity));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(activity.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(activity, is(new ProjectPrefixDeclarationsActivity(projectId, presenter)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(activity, is(not(new ProjectPrefixDeclarationsActivity(ProjectId.get("12345678-abcd-abcd-abcd-123456789abcd"), presenter))));
    }

    @Test
    public void shouldBeEqualToOtherThatHasDifferent_presenter() {
        assertThat(activity, is(new ProjectPrefixDeclarationsActivity(projectId, Mockito.mock(ProjectPrefixDeclarationsPresenter.class))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(activity.hashCode(), is(new ProjectPrefixDeclarationsActivity(projectId, presenter).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(activity.toString(), Matchers.startsWith("ProjectPrefixDeclarationsActivity"));
    }
}
