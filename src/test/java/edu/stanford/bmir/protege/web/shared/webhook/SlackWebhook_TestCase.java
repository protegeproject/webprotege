
package edu.stanford.bmir.protege.web.shared.webhook;

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
public class SlackWebhook_TestCase {

    private SlackWebhook slackWebhook;

    @Mock
    private ProjectId projectId;

    private String payloadUrl = "The payloadUrl";

    @Before
    public void setUp() {
        slackWebhook = new SlackWebhook(projectId, payloadUrl);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new SlackWebhook(null, payloadUrl);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(slackWebhook.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_payloadUrl_IsNull() {
        new SlackWebhook(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_payloadUrl() {
        assertThat(slackWebhook.getPayloadUrl(), is(this.payloadUrl));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(slackWebhook, is(slackWebhook));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(slackWebhook.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(slackWebhook, is(new SlackWebhook(projectId, payloadUrl)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(slackWebhook, is(not(new SlackWebhook(mock(ProjectId.class), payloadUrl))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_payloadUrl() {
        assertThat(slackWebhook, is(not(new SlackWebhook(projectId, "String-eefb20e3-a251-4c46-8a95-826ace1c2a15"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(slackWebhook.hashCode(), is(new SlackWebhook(projectId, payloadUrl).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(slackWebhook.toString(), startsWith("SlackWebhook"));
    }

}
