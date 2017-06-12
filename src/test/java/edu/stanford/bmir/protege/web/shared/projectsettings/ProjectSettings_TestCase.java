
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

@RunWith(value = MockitoJUnitRunner.class)
public class ProjectSettings_TestCase {

    private ProjectSettings projectSettings;

    @Mock
    private ProjectId projectId;

    private String projectDisplayName = "The projectDisplayName";

    private String projectDescription = "The projectDescription";

    @Mock
    private SlackIntegrationSettings slackIntegrationSettings;

    @Mock
    private WebhookSettings webhookSettings;

    @Before
    public void setUp() {
        projectSettings = new ProjectSettings(projectId, projectDisplayName, projectDescription, slackIntegrationSettings, webhookSettings);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new ProjectSettings(null, projectDisplayName, projectDescription, slackIntegrationSettings, webhookSettings);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(projectSettings.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectDisplayName_IsNull() {
        new ProjectSettings(projectId, null, projectDescription, slackIntegrationSettings, webhookSettings);
    }

    @Test
    public void shouldReturnSupplied_projectDisplayName() {
        assertThat(projectSettings.getProjectDisplayName(), is(this.projectDisplayName));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectDescription_IsNull() {
        new ProjectSettings(projectId, projectDisplayName, null, slackIntegrationSettings, webhookSettings);
    }

    @Test
    public void shouldReturnSupplied_projectDescription() {
        assertThat(projectSettings.getProjectDescription(), is(this.projectDescription));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_slackIntegrationSettings_IsNull() {
        new ProjectSettings(projectId, projectDisplayName, projectDescription, null, webhookSettings);
    }

    @Test
    public void shouldReturnSupplied_slackIntegrationSettings() {
        assertThat(projectSettings.getSlackIntegrationSettings(), is(this.slackIntegrationSettings));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_webhookSettings_IsNull() {
        new ProjectSettings(projectId, projectDisplayName, projectDescription, slackIntegrationSettings, null);
    }

    @Test
    public void shouldReturnSupplied_webhookSettings() {
        assertThat(projectSettings.getWebhookSettings(), is(this.webhookSettings));
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
        assertThat(projectSettings, is(new ProjectSettings(projectId, projectDisplayName, projectDescription, slackIntegrationSettings, webhookSettings)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(projectSettings, is(not(new ProjectSettings(mock(ProjectId.class), projectDisplayName, projectDescription, slackIntegrationSettings, webhookSettings))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectDisplayName() {
        assertThat(projectSettings, is(not(new ProjectSettings(projectId, "String-e2778d26-1625-44d5-95be-360157916c2a", projectDescription, slackIntegrationSettings, webhookSettings))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectDescription() {
        assertThat(projectSettings, is(not(new ProjectSettings(projectId, projectDisplayName, "String-47556e3c-b862-44dc-a859-df860d6a2b59", slackIntegrationSettings, webhookSettings))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_slackIntegrationSettings() {
        assertThat(projectSettings, is(not(new ProjectSettings(projectId, projectDisplayName, projectDescription, mock(SlackIntegrationSettings.class), webhookSettings))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_webhookSettings() {
        assertThat(projectSettings, is(not(new ProjectSettings(projectId, projectDisplayName, projectDescription, slackIntegrationSettings, mock(WebhookSettings.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(projectSettings.hashCode(), is(new ProjectSettings(projectId, projectDisplayName, projectDescription, slackIntegrationSettings, webhookSettings).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(projectSettings.toString(), startsWith("ProjectSettings"));
    }

}
