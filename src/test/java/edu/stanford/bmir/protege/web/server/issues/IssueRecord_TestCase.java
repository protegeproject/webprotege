package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.issues.Status;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import java.lang.NullPointerException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class IssueRecord_TestCase {

    private IssueRecord issueRecord;

    @Mock
    private ProjectId projectId;

    private long number = 1L;

    private String title = "The title";

    private String body = "The body";

    @Mock
    private UserId owner;

    private long createdAt = 2L;

    private long updatedAt = 3L;

    private long closedAt = 4L;

    private Status status = Status.OPEN;

    private Optional<UserId> assignee;

    private String milestone = "The milestone";

    @Mock
    private ImmutableList<String> labels;

    @Mock
    private ImmutableList<OWLEntity> targetEntities;

    @Before
    public void setUp() {
        assignee = Optional.of(mock(UserId.class));
        issueRecord = new IssueRecord(projectId, number, title, body, owner, createdAt, updatedAt, closedAt, status, assignee, milestone, labels, targetEntities);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new IssueRecord(null, number, title, body, owner, createdAt, updatedAt, closedAt, status, assignee, milestone, labels, targetEntities);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(issueRecord.getProjectId(), is(this.projectId));
    }

    @Test
    public void shouldReturnSupplied_number() {
        assertThat(issueRecord.getNumber(), is(this.number));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_title_IsNull() {
        new IssueRecord(projectId, number, null, body, owner, createdAt, updatedAt, closedAt, status, assignee, milestone, labels, targetEntities);
    }

    @Test
    public void shouldReturnSupplied_title() {
        assertThat(issueRecord.getTitle(), is(this.title));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_body_IsNull() {
        new IssueRecord(projectId, number, title, null, owner, createdAt, updatedAt, closedAt, status, assignee, milestone, labels, targetEntities);
    }

    @Test
    public void shouldReturnSupplied_body() {
        assertThat(issueRecord.getBody(), is(this.body));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_owner_IsNull() {
        new IssueRecord(projectId, number, title, body, null, createdAt, updatedAt, closedAt, status, assignee, milestone, labels, targetEntities);
    }

    @Test
    public void shouldReturnSupplied_owner() {
        assertThat(issueRecord.getOwner(), is(this.owner));
    }

    @Test
    public void shouldReturnSupplied_createdAt() {
        assertThat(issueRecord.getCreatedAt(), is(this.createdAt));
    }

    @Test
    public void shouldReturnSupplied_updatedAt() {
        assertThat(issueRecord.getUpdatedAt(), is(this.updatedAt));
    }

    @Test
    public void shouldReturnSupplied_closedAt() {
        assertThat(issueRecord.getClosedAt(), is(this.closedAt));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_status_IsNull() {
        new IssueRecord(projectId, number, title, body, owner, createdAt, updatedAt, closedAt, null, assignee, milestone, labels, targetEntities);
    }

    @Test
    public void shouldReturnSupplied_status() {
        assertThat(issueRecord.getStatus(), is(this.status));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_assignee_IsNull() {
        new IssueRecord(projectId, number, title, body, owner, createdAt, updatedAt, closedAt, status, null, milestone, labels, targetEntities);
    }

    @Test
    public void shouldReturnSupplied_assignee() {
        assertThat(issueRecord.getAssignee(), is(this.assignee));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_milestone_IsNull() {
        new IssueRecord(projectId, number, title, body, owner, createdAt, updatedAt, closedAt, status, assignee, null, labels, targetEntities);
    }

    @Test
    public void shouldReturnSupplied_milestone() {
        assertThat(issueRecord.getMilestone(), is(this.milestone));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_labels_IsNull() {
        new IssueRecord(projectId, number, title, body, owner, createdAt, updatedAt, closedAt, status, assignee, milestone, null, targetEntities);
    }

    @Test
    public void shouldReturnSupplied_labels() {
        assertThat(issueRecord.getLabels(), is(this.labels));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_targetEntities_IsNull() {
        new IssueRecord(projectId, number, title, body, owner, createdAt, updatedAt, closedAt, status, assignee, milestone, labels, null);
    }

    @Test
    public void shouldReturnSupplied_targetEntities() {
        assertThat(issueRecord.getTargetEntities(), is(this.targetEntities));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issueRecord, is(issueRecord));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(issueRecord.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issueRecord, is(new IssueRecord(projectId, number, title, body, owner, createdAt, updatedAt, closedAt, status, assignee, milestone, labels, targetEntities)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(issueRecord, is(not(new IssueRecord(mock(ProjectId.class), number, title, body, owner, createdAt, updatedAt, closedAt, status, assignee, milestone, labels, targetEntities))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_number() {
        assertThat(issueRecord, is(not(new IssueRecord(projectId, 5L, title, body, owner, createdAt, updatedAt, closedAt, status, assignee, milestone, labels, targetEntities))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_title() {
        assertThat(issueRecord, is(not(new IssueRecord(projectId, number, "String-90b0d178-4fea-473c-bc49-67523220865e", body, owner, createdAt, updatedAt, closedAt, status, assignee, milestone, labels, targetEntities))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_body() {
        assertThat(issueRecord, is(not(new IssueRecord(projectId, number, title, "String-c2527ba1-6654-446b-bc13-93f5489fb18a", owner, createdAt, updatedAt, closedAt, status, assignee, milestone, labels, targetEntities))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_owner() {
        assertThat(issueRecord, is(not(new IssueRecord(projectId, number, title, body, mock(UserId.class), createdAt, updatedAt, closedAt, status, assignee, milestone, labels, targetEntities))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_createdAt() {
        assertThat(issueRecord, is(not(new IssueRecord(projectId, number, title, body, owner, 6L, updatedAt, closedAt, status, assignee, milestone, labels, targetEntities))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_updatedAt() {
        assertThat(issueRecord, is(not(new IssueRecord(projectId, number, title, body, owner, createdAt, 7L, closedAt, status, assignee, milestone, labels, targetEntities))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_closedAt() {
        assertThat(issueRecord, is(not(new IssueRecord(projectId, number, title, body, owner, createdAt, updatedAt, 8L, status, assignee, milestone, labels, targetEntities))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_status() {
        assertThat(issueRecord, is(not(new IssueRecord(projectId, number, title, body, owner, createdAt, updatedAt, closedAt, Status.CLOSED, assignee, milestone, labels, targetEntities))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_assignee() {
        assertThat(issueRecord, is(not(new IssueRecord(projectId, number, title, body, owner, createdAt, updatedAt, closedAt, status, Optional.of(mock(UserId.class)), milestone, labels, targetEntities))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_milestone() {
        assertThat(issueRecord, is(not(new IssueRecord(projectId, number, title, body, owner, createdAt, updatedAt, closedAt, status, assignee, "String-7fd0fb9e-fd12-49ee-8132-dff844b2803f", labels, targetEntities))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_labels() {
        assertThat(issueRecord, is(not(new IssueRecord(projectId, number, title, body, owner, createdAt, updatedAt, closedAt, status, assignee, milestone, mock(ImmutableList.class), targetEntities))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_targetEntities() {
        assertThat(issueRecord, is(not(new IssueRecord(projectId, number, title, body, owner, createdAt, updatedAt, closedAt, status, assignee, milestone, labels, mock(ImmutableList.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issueRecord.hashCode(), is(new IssueRecord(projectId, number, title, body, owner, createdAt, updatedAt, closedAt, status, assignee, milestone, labels, targetEntities).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issueRecord.toString(), startsWith("IssueRecord"));
    }

}
