
package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.issues.events.IssueEvent;
import edu.stanford.bmir.protege.web.shared.issues.mention.MentionParser;
import edu.stanford.bmir.protege.web.shared.issues.mention.ParsedMention;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class Issue_TestCase {


    private static final long ISSUE_NUMBER = 33L;

    private static final long OTHER_ISSUE_NUMBER = 55L;

    private Issue issue;

    @Mock
    private ProjectId projectId;

    private int number = 1;

    @Mock
    private UserId creator;

    private long createdAt = 1L;

    private Optional<Long> updatedAt = Optional.of(ISSUE_NUMBER);

    private String title = "The title";

    private String body = "The body";

    private Status status = Status.OPEN;

    private Optional<UserId> assignee = Optional.of(mock(UserId.class));

    private Optional<Milestone> milestone = Optional.of(mock(Milestone.class));

    @Mock
    private ImmutableList<String> labels = ImmutableList.of("The Label" );

    @Mock
    private ImmutableList<Comment> comments = ImmutableList.of(mock(Comment.class));

    @Mock
    private ImmutableList<Mention> mentions = ImmutableList.of(mock(Mention.class));

    @Mock
    private ImmutableList<UserId> participants = ImmutableList.of(mock(UserId.class));

    @Mock
    private ImmutableList<IssueEvent> events = ImmutableList.of(mock(IssueEvent.class));

    @Before
    public void setUp()
            throws Exception {
        issue = new Issue(projectId,
                          number,
                          creator,
                          createdAt,
                          updatedAt,
                          title,
                          body,
                          status,
                          assignee,
                          milestone,
                          labels,
                          comments,
                          mentions,
                          participants,
                          events);
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new Issue(null,
                  number,
                  creator,
                  createdAt,
                  updatedAt,
                  title,
                  body,
                  status,
                  assignee,
                  milestone,
                  labels,
                  comments,
                  mentions,
                  participants,
                  events);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(issue.getProjectId(), is(this.projectId));
    }

    @Test
    public void shouldReturnSupplied_number() {
        assertThat(issue.getNumber(), is(this.number));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_creator_IsNull() {
        new Issue(projectId,
                  number,
                  null,
                  createdAt,
                  updatedAt,
                  title,
                  body,
                  status,
                  assignee,
                  milestone,
                  labels,
                  comments,
                  mentions,
                  participants,
                  events);
    }

    @Test
    public void shouldReturnSupplied_creator() {
        assertThat(issue.getCreator(), is(this.creator));
    }

    @Test
    public void shouldReturnSupplied_createdAt() {
        assertThat(issue.getCreatedAt(), is(this.createdAt));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_updatedAt_IsNull() {
        new Issue(projectId,
                  number,
                  creator,
                  createdAt,
                  null,
                  title,
                  body,
                  status,
                  assignee,
                  milestone,
                  labels,
                  comments,
                  mentions,
                  participants,
                  events);
    }

    @Test
    public void shouldReturnSupplied_updatedAt() {
        assertThat(issue.getUpdatedAt(), is(this.updatedAt));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_title_IsNull() {
        new Issue(projectId,
                  number,
                  creator,
                  createdAt,
                  updatedAt,
                  null,
                  body,
                  status,
                  assignee,
                  milestone,
                  labels,
                  comments,
                  mentions,
                  participants,
                  events);
    }

    @Test
    public void shouldReturnSupplied_title() {
        assertThat(issue.getTitle(), is(this.title));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_body_IsNull() {
        new Issue(projectId,
                  number,
                  creator,
                  createdAt,
                  updatedAt,
                  title,
                  null,
                  status,
                  assignee,
                  milestone,
                  labels,
                  comments,
                  mentions,
                  participants,
                  events);
    }

    @Test
    public void shouldReturnSupplied_body() {
        assertThat(issue.getBody(), is(this.body));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_status_IsNull() {
        new Issue(projectId,
                  number,
                  creator,
                  createdAt,
                  updatedAt,
                  title,
                  body,
                  null,
                  assignee,
                  milestone,
                  labels,
                  comments,
                  mentions,
                  participants,
                  events);
    }

    @Test
    public void shouldReturnSupplied_status() {
        assertThat(issue.getStatus(), is(this.status));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_assignee_IsNull() {
        new Issue(projectId,
                  number,
                  creator,
                  createdAt,
                  updatedAt,
                  title,
                  body,
                  status,
                  null,
                  milestone,
                  labels,
                  comments,
                  mentions,
                  participants,
                  events);
    }

    @Test
    public void shouldReturnSupplied_assignee() {
        assertThat(issue.getAssignee(), is(this.assignee));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_milestone_IsNull() {
        new Issue(projectId,
                  number,
                  creator,
                  createdAt,
                  updatedAt,
                  title,
                  body,
                  status,
                  assignee,
                  null,
                  labels,
                  comments,
                  mentions,
                  participants,
                  events);
    }

    @Test
    public void shouldReturnSupplied_milestone() {
        assertThat(issue.getMilestone(), is(this.milestone));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_labels_IsNull() {
        new Issue(projectId,
                  number,
                  creator,
                  createdAt,
                  updatedAt,
                  title,
                  body,
                  status,
                  assignee,
                  milestone,
                  null,
                  comments,
                  mentions,
                  participants,
                  events);
    }

    @Test
    public void shouldReturnSupplied_labels() {
        assertThat(issue.getLabels(), is(this.labels));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_comments_IsNull() {
        new Issue(projectId,
                  number,
                  creator,
                  createdAt,
                  updatedAt,
                  title,
                  body,
                  status,
                  assignee,
                  milestone,
                  labels,
                  null,
                  mentions,
                  participants,
                  events);
    }

    @Test
    public void shouldReturnSupplied_comments() {
        assertThat(issue.getComments(), is(this.comments));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_mentions_IsNull() {
        new Issue(projectId,
                  number,
                  creator,
                  createdAt,
                  updatedAt,
                  title,
                  body,
                  status,
                  assignee,
                  milestone,
                  labels,
                  comments,
                  null,
                  participants,
                  events);
    }

    @Test
    public void shouldReturnSupplied_mentions() {
        assertThat(issue.getMentions(), is(this.mentions));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_participants_IsNull() {
        new Issue(projectId,
                  number,
                  creator,
                  createdAt,
                  updatedAt,
                  title,
                  body,
                  status,
                  assignee,
                  milestone,
                  labels,
                  comments,
                  mentions,
                  null,
                  events);
    }

    @Test
    public void shouldReturnSupplied_participants() {
        assertThat(issue.getParticipants(), is(this.participants));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_events_IsNull() {
        new Issue(projectId,
                  number,
                  creator,
                  createdAt,
                  updatedAt,
                  title,
                  body,
                  status,
                  assignee,
                  milestone,
                  labels,
                  comments,
                  mentions,
                  participants,
                  null);
    }

    @Test
    public void shouldReturnSupplied_events() {
        assertThat(issue.getEvents(), is(this.events));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issue, is(issue));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull" )
    public void shouldNotBeEqualToNull() {
        assertThat(issue.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issue,
                                 is(new Issue(projectId,
                                                       number,
                                                       creator,
                                                       createdAt,
                                                       updatedAt,
                                                       title,
                                                       body,
                                                       status,
                                                       assignee,
                                                       milestone,
                                                       labels,
                                                       comments,
                                                       mentions,
                                                       participants,
                                                       events)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(issue,
                                 is(Matchers.not(new Issue(mock(ProjectId.class),
                                                                    number,
                                                                    creator,
                                                                    createdAt,
                                                                    updatedAt,
                                                                    title,
                                                                    body,
                                                                    status,
                                                                    assignee,
                                                                    milestone,
                                                                    labels,
                                                                    comments,
                                                                    mentions,
                                                                    participants,
                                                                    events))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_number() {
        assertThat(issue,
                                 is(Matchers.not(new Issue(projectId,
                                                                    2,
                                                                    creator,
                                                                    createdAt,
                                                                    updatedAt,
                                                                    title,
                                                                    body,
                                                                    status,
                                                                    assignee,
                                                                    milestone,
                                                                    labels,
                                                                    comments,
                                                                    mentions,
                                                                    participants,
                                                                    events))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_creator() {
        assertThat(issue,
                                 is(Matchers.not(new Issue(projectId,
                                                                    number,
                                                                    mock(UserId.class),
                                                                    createdAt,
                                                                    updatedAt,
                                                                    title,
                                                                    body,
                                                                    status,
                                                                    assignee,
                                                                    milestone,
                                                                    labels,
                                                                    comments,
                                                                    mentions,
                                                                    participants,
                                                                    events))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_createdAt() {
        assertThat(issue,
                                 is(Matchers.not(new Issue(projectId,
                                                                    number,
                                                                    creator,
                                                                    2L,
                                                                    updatedAt,
                                                                    title,
                                                                    body,
                                                                    status,
                                                                    assignee,
                                                                    milestone,
                                                                    labels,
                                                                    comments,
                                                                    mentions,
                                                                    participants,
                                                                    events))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_updatedAt() {
        assertThat(issue,
                                 is(Matchers.not(new Issue(projectId,
                                                                    number,
                                                                    creator,
                                                                    createdAt,
                                                                    Optional.of(OTHER_ISSUE_NUMBER),
                                                                    title,
                                                                    body,
                                                                    status,
                                                                    assignee,
                                                                    milestone,
                                                                    labels,
                                                                    comments,
                                                                    mentions,
                                                                    participants,
                                                                    events))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_title() {
        assertThat(issue,
                                 is(Matchers.not(new Issue(projectId,
                                                                    number,
                                                                    creator,
                                                                    createdAt,
                                                                    updatedAt,
                                                                    "String-524c2461-a479-48d9-9ab7-6d171b0f7ab3" ,
                                                                    body,
                                                                    status,
                                                                    assignee,
                                                                    milestone,
                                                                    labels,
                                                                    comments,
                                                                    mentions,
                                                                    participants,
                                                                    events))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_body() {
        assertThat(issue,
                                 is(Matchers.not(new Issue(projectId,
                                                                    number,
                                                                    creator,
                                                                    createdAt,
                                                                    updatedAt,
                                                                    title,
                                                                    "String-308adff8-401c-4eb1-b31d-3b4b794a2406" ,
                                                                    status,
                                                                    assignee,
                                                                    milestone,
                                                                    labels,
                                                                    comments,
                                                                    mentions,
                                                                    participants,
                                                                    events))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_status() {
        assertThat(issue,
                                 is(Matchers.not(new Issue(projectId,
                                                                    number,
                                                                    creator,
                                                                    createdAt,
                                                                    updatedAt,
                                                                    title,
                                                                    body,
                                                                    Status.CLOSED,
                                                                    assignee,
                                                                    milestone,
                                                                    labels,
                                                                    comments,
                                                                    mentions,
                                                                    participants,
                                                                    events))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_assignee() {
        assertThat(issue,
                                 is(Matchers.not(new Issue(projectId,
                                                                    number,
                                                                    creator,
                                                                    createdAt,
                                                                    updatedAt,
                                                                    title,
                                                                    body,
                                                                    status,
                                                                    Optional.of(mock(UserId.class)),
                                                                    milestone,
                                                                    labels,
                                                                    comments,
                                                                    mentions,
                                                                    participants,
                                                                    events))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_milestone() {
        assertThat(issue,
                                 is(Matchers.not(new Issue(projectId,
                                                                    number,
                                                                    creator,
                                                                    createdAt,
                                                                    updatedAt,
                                                                    title,
                                                                    body,
                                                                    status,
                                                                    assignee,
                                                                    Optional.of(mock(Milestone.class)),
                                                                    labels,
                                                                    comments,
                                                                    mentions,
                                                                    participants,
                                                                    events))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_labels() {
        assertThat(issue,
                                 is(Matchers.not(new Issue(projectId,
                                                                    number,
                                                                    creator,
                                                                    createdAt,
                                                                    updatedAt,
                                                                    title,
                                                                    body,
                                                                    status,
                                                                    assignee,
                                                                    milestone,
                                                                    ImmutableList.of("Other Label"),
                                                                    comments,
                                                                    mentions,
                                                                    participants,
                                                                    events))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_comments() {
        assertThat(issue,
                                 is(Matchers.not(new Issue(projectId,
                                                                    number,
                                                                    creator,
                                                                    createdAt,
                                                                    updatedAt,
                                                                    title,
                                                                    body,
                                                                    status,
                                                                    assignee,
                                                                    milestone,
                                                                    labels,
                                                                    ImmutableList.of(mock(Comment.class)),
                                                                    mentions,
                                                                    participants,
                                                                    events))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_mentions() {
        assertThat(issue,
                                 is(Matchers.not(new Issue(projectId,
                                                                    number,
                                                                    creator,
                                                                    createdAt,
                                                                    updatedAt,
                                                                    title,
                                                                    body,
                                                                    status,
                                                                    assignee,
                                                                    milestone,
                                                                    labels,
                                                                    comments,
                                                                    ImmutableList.of(mock(Mention.class)),
                                                                    participants,
                                                                    events))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_participants() {
        assertThat(issue,
                                 is(Matchers.not(new Issue(projectId,
                                                                    number,
                                                                    creator,
                                                                    createdAt,
                                                                    updatedAt,
                                                                    title,
                                                                    body,
                                                                    status,
                                                                    assignee,
                                                                    milestone,
                                                                    labels,
                                                                    comments,
                                                                    mentions,
                                                                    ImmutableList.of(mock(UserId.class)),
                                                                    events))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_events() {
        assertThat(issue,
                                 is(Matchers.not(new Issue(projectId,
                                                                    number,
                                                                    creator,
                                                                    createdAt,
                                                                    updatedAt,
                                                                    title,
                                                                    body,
                                                                    status,
                                                                    assignee,
                                                                    milestone,
                                                                    labels,
                                                                    comments,
                                                                    mentions,
                                                                    participants,
                                                                    ImmutableList.of(mock(IssueEvent.class))))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issue.hashCode(),
                                 is(new Issue(projectId,
                                                       number,
                                                       creator,
                                                       createdAt,
                                                       updatedAt,
                                                       title,
                                                       body,
                                                       status,
                                                       assignee,
                                                       milestone,
                                                       labels,
                                                       comments,
                                                       mentions,
                                                       participants,
                                                       events).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issue.toString(), Matchers.startsWith("Issue" ));
    }

    @Test
    public void shouldBuildIssueWithReturnedBuilder() {
        IssueBuilder builder = issue.builder();
        MentionParser noopParser = new MentionParser() {
            @Nonnull
            @Override
            public List<ParsedMention> parseMentions(@Nonnull String text) {
                return Collections.emptyList();
            }
        };
        Issue rebuiltIssue = builder.build(noopParser);
        assertThat(rebuiltIssue, is(equalTo(issue)));
    }
}
