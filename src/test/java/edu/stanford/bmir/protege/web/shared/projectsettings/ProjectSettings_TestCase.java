
package edu.stanford.bmir.protege.web.shared.projectsettings;

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
public class ProjectSettings_TestCase {

    private ProjectSettings projectSettings;
    @Mock
    private ProjectId projectId;

    private String projectDisplayName = "The projectDisplayName";

    private String projectDescription = "The projectDescription";

    @Mock
    private SlackIntegrationSettings slackIntegrationSettings;

    @Before
    public void setUp() {
        projectSettings = new ProjectSettings(projectId, projectDisplayName, projectDescription, slackIntegrationSettings);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new ProjectSettings(null, projectDisplayName, projectDescription, slackIntegrationSettings);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(projectSettings.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectDisplayName_IsNull() {
        new ProjectSettings(projectId, null, projectDescription, slackIntegrationSettings);
    }

    @Test
    public void shouldReturnSupplied_projectDisplayName() {
        assertThat(projectSettings.getProjectDisplayName(), is(this.projectDisplayName));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectDescription_IsNull() {
        new ProjectSettings(projectId, projectDisplayName, null, slackIntegrationSettings);
    }

    @Test
    public void shouldReturnSupplied_projectDescription() {
        assertThat(projectSettings.getProjectDescription(), is(this.projectDescription));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_slackIntegrationSettings_IsNull() {
        new ProjectSettings(projectId, projectDisplayName, projectDescription, null);
    }

    @Test
    public void shouldReturnSupplied_slackIntegrationSettings() {
        assertThat(projectSettings.getSlackIntegrationSettings(), is(this.slackIntegrationSettings));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(projectSettings, is(projectSettings));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(projectSettings.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(projectSettings, is(new ProjectSettings(projectId, projectDisplayName, projectDescription, slackIntegrationSettings)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(projectSettings, is(not(new ProjectSettings(mock(ProjectId.class), projectDisplayName, projectDescription, slackIntegrationSettings))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectDisplayName() {
        assertThat(projectSettings, is(not(new ProjectSettings(projectId, "String-bc3085d2-7e86-4428-8698-4662d1c3d587", projectDescription, slackIntegrationSettings))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectDescription() {
        assertThat(projectSettings, is(not(new ProjectSettings(projectId, projectDisplayName, "String-1045e7e3-69ef-452e-947c-90080ecdde87", slackIntegrationSettings))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_slackIntegrationSettings() {
        assertThat(projectSettings, is(not(new ProjectSettings(projectId, projectDisplayName, projectDescription, mock(SlackIntegrationSettings.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(projectSettings.hashCode(), is(new ProjectSettings(projectId, projectDisplayName, projectDescription, slackIntegrationSettings).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(projectSettings.toString(), startsWith("ProjectSettings"));
    }

}
