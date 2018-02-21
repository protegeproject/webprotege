
package edu.stanford.bmir.protege.web.shared.webhook;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(value = org.mockito.runners.MockitoJUnitRunner.class)
public class ProjectWebhook_TestCase {

    private ProjectWebhook projectWebhook;

    @Mock
    private ProjectId projectId;

    private String payloadUrl = "The payloadUrl";

    @Mock
    private List<ProjectWebhookEventType> subscribedToEvents = new ArrayList<>();

    @Before
    public void setUp() {
        subscribedToEvents = Collections.singletonList(ProjectWebhookEventType.PROJECT_CHANGED);
        projectWebhook = new ProjectWebhook(projectId, payloadUrl, subscribedToEvents);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new ProjectWebhook(null, payloadUrl, subscribedToEvents);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(projectWebhook.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_payloadUrl_IsNull() {
        new ProjectWebhook(projectId, null, subscribedToEvents);
    }

    @Test
    public void shouldReturnSupplied_payloadUrl() {
        assertThat(projectWebhook.getPayloadUrl(), is(this.payloadUrl));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_subscribedToEvents_IsNull() {
        new ProjectWebhook(projectId, payloadUrl, null);
    }

    @Test
    public void shouldReturnSupplied_subscribedToEvents() {
        assertThat(projectWebhook.getSubscribedToEvents(), is(this.subscribedToEvents));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(projectWebhook, is(projectWebhook));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(projectWebhook.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(projectWebhook, is(new ProjectWebhook(projectId, payloadUrl, subscribedToEvents)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(projectWebhook, is(not(new ProjectWebhook(mock(ProjectId.class), payloadUrl, subscribedToEvents))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_payloadUrl() {
        assertThat(projectWebhook, is(not(new ProjectWebhook(projectId, "String-90554065-a9cf-485e-86b5-725b38241203", subscribedToEvents))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_subscribedToEvents() {
        assertThat(projectWebhook, is(not(new ProjectWebhook(projectId, payloadUrl, new ArrayList<>()))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(projectWebhook.hashCode(), is(new ProjectWebhook(projectId, payloadUrl, subscribedToEvents).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(projectWebhook.toString(), startsWith("ProjectWebhook"));
    }

}
