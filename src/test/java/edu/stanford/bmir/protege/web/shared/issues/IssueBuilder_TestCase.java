package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.issues.events.*;
import edu.stanford.bmir.protege.web.shared.issues.mention.MentionParser;
import edu.stanford.bmir.protege.web.shared.issues.mention.ParsedMention;
import edu.stanford.bmir.protege.web.shared.issues.mention.UserIdMention;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Sep 2016
 */
@RunWith(MockitoJUnitRunner.class)
public class IssueBuilder_TestCase {

    private static final int ISSUE_NUMBER = 3;

    private static final long CREATED_AT = 33L;

    public static final long TIMESTAMP = 55L;

    @Mock
    private ProjectId projectId;

    private int number = ISSUE_NUMBER;

    @Mock
    private UserId creator, userId;

    private long createdAt = CREATED_AT;

    private IssueBuilder builder;

    @Mock
    private MentionParser mentionParser;

    @Mock
    private ParsedMention parsedMention;

    @Mock
    private Mention mention;

    @Before
    public void setUp() throws Exception {
        builder = new IssueBuilder(projectId, number, creator, createdAt);
        when(mention.getMentionedUserId()).thenReturn(Optional.empty());
        when(parsedMention.getParsedMention()).thenReturn(mention);
        when(mentionParser.parseMentions(anyString())).thenReturn(ImmutableList.of(parsedMention));
    }

    @Test
    public void shouldBuildIssueWithSpecifiedProjectId() {
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getProjectId(), is(projectId));
    }

    @Test
    public void shouldBuildIssueWithSpecifiedNumber() {
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getNumber(), is(ISSUE_NUMBER));
    }

    @Test
    public void shouldBuildIssueWithSpecifiedCreator() {
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getCreator(), is(creator));
    }

    @Test
    public void shouldBuildIssueWithSpecifiedCreatedAt() {
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getCreatedAt(), is(createdAt));
    }

    @Test
    public void shouldBuildIssueWithEmptyTitleByDefault() {
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getTitle(), is(""));
    }

    @Test
    public void shouldBuildIssueWithSpecifiedTitle() {
        String title = "The title";
        builder.withTitle(title );
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getTitle(), is(title));
    }

    @Test
    public void shouldBuildIssueWithEmptyBodyByDefault() {
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getBody(), is(""));
    }

    @Test
    public void shouldBuildIssueWithSpecifiedBody() {
        String body = "The body";
        builder.withBody(body);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getBody(), is(body));
    }

    @Test
    public void shouldBuildIssueWithEmptyTargetEntitiesByDefault() {
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getTargetEntities(), is(empty()));
    }

    @Test
    public void shouldBuildIssueWithSpecifiedTargetEntity() {
        OWLEntity entity = mock(OWLEntity.class);
        builder.addTargetEntity(entity, userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getTargetEntities(), hasItem(entity));
    }

    @Test
    public void shouldBuildIssueWithAddedTargetEntityEvent() {
        OWLEntity entity = mock(OWLEntity.class);
        builder.addTargetEntity(entity, userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getEvents(), hasItem(new IssueTargetAdded(userId, TIMESTAMP, entity)));
    }


    @Test
    public void shouldBuildIssueWithUpdatedTimestampOnAddTargetEntity() {
        OWLEntity entity = mock(OWLEntity.class);
        builder.addTargetEntity(entity, userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThatIssueUpdatedTimestampWasUpdated(issue);
    }

    @Test
    public void shouldBuildIssueWithoutRemovedTargetEntity() {
        OWLEntity entity = mock(OWLEntity.class);
        builder.addTargetEntity(entity, userId, TIMESTAMP);
        builder.removeTargetEntity(entity, userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getTargetEntities(), is(empty()));
    }

    @Test
    public void shouldBuildIssueWithRemovedTargetEntityEvent() {
        OWLEntity entity = mock(OWLEntity.class);
        builder.addTargetEntity(entity, userId, TIMESTAMP);
        builder.removeTargetEntity(entity, userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getEvents(), hasItem(new IssueTargetRemoved(userId, TIMESTAMP, entity)));
    }

    @Test
    public void shouldBuildIssueWithUpdatedTimestampOnRemoveTargetEntity() {
        OWLEntity entity = mock(OWLEntity.class);
        builder.addTargetEntity(entity, userId, TIMESTAMP - 5);
        builder.removeTargetEntity(entity, userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThatIssueUpdatedTimestampWasUpdated(issue);
    }

    @Test
    public void shouldBuildIssueWithOpenStatusByDefault() {
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getStatus(), is(Status.OPEN));
    }

    @Test
    public void shouldBuildIssueWithClosedStatus() {
        builder.close(mock(UserId.class), TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getStatus(), is(Status.CLOSED));
    }

    @Test
    public void shouldBuildIssueWithUpdatedTimestampOnClose() {
        builder.close(mock(UserId.class), TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getStatus(), is(Status.CLOSED));
        assertThatIssueUpdatedTimestampWasUpdated(issue);
    }

    @Test
    public void shouldBuildIssueWithCloseEvent() {
        builder.close(userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getEvents(), hasItem(new IssueClosed(userId, TIMESTAMP)));
    }

    @Test
    public void shouldBuildIssueWithEmptyAssigneesByDefault() {
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getAssignees(), is(empty()));
    }

    @Test
    public void shouldBuildIssueWithSpecifiedAssignees() {
        UserId assignee = mock(UserId.class);
        builder.assignTo(assignee, userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getAssignees(), hasItem(assignee));
    }

    @Test
    public void shouldUpdateTimestampOnAssign() {
        UserId assignee = mock(UserId.class);
        builder.assignTo(assignee, userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThatIssueUpdatedTimestampWasUpdated(issue);
    }

    @Test
    public void shouldBuildIssueWithAssignedEvent() {
        UserId assignee = mock(UserId.class);
        builder.assignTo(assignee, userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getEvents(), hasItem(new IssueAssigned(userId, TIMESTAMP, assignee)));
    }



    @Test
    public void shouldBuildIssueWithEmptyMilestoneByDefault() {
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getMilestone(), is(Optional.empty()));
    }

    @Test
    public void shouldBuildIssueWithSpecifiedMilestone() {
        Milestone milestone = mock(Milestone.class);
        builder.milestone(milestone, userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getMilestone(), is(Optional.of(milestone)));
    }

    @Test
    public void shouldUpdateTimestampOnMilestone() {
        Milestone milestone = mock(Milestone.class);
        builder.milestone(milestone, userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThatIssueUpdatedTimestampWasUpdated(issue);
    }

    @Test
    public void shouldBuildIssueWithMilestonedEvent() {
        Milestone milestone = mock(Milestone.class);
        builder.milestone(milestone, userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getEvents(), hasItem(new IssueMilestoned(userId, TIMESTAMP, milestone)));
    }




    @Test
    public void shouldBuildIssueWithUnlockedLockSettingByDefault() {
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getLockSetting(), is(LockSetting.UNLOCKED));
    }

    @Test
    public void shouldBuildIssueWithLockedSetting() {
        builder.lock(userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getLockSetting(), is(LockSetting.LOCKED));
    }

    @Test
    public void shouldUpdateTimestampOnLockSetting() {
        builder.lock(userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThatIssueUpdatedTimestampWasUpdated(issue);
    }

    @Test
    public void shouldBuildIssueWithLockSettingEvent() {
        builder.lock(userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getEvents(), hasItem(new IssueLocked(userId, TIMESTAMP)));
    }

    @Test
    public void shouldBuildIssueWithUnlockedSetting() {
        builder.lock(userId, TIMESTAMP);
        builder.unlock(userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getLockSetting(), is(LockSetting.UNLOCKED));
    }

    @Test
    public void shouldBuildIssueWithUnlockedEvent() {
        builder.lock(userId, TIMESTAMP);
        builder.unlock(userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getEvents(), hasItem(new IssueUnlocked(userId, TIMESTAMP)));
    }




    @Test
    public void shouldBuildIssueWithEmptyLabelsByDefault() {
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getLabels(), is(empty()));
    }

    @Test
    public void shouldBuildIssueWithSpecifiedLabels() {
        String label = "The Label";
        builder.addLabel(label , userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getLabels(), hasItem(label));
    }

    @Test
    public void shouldUpdateTimestampOnAddLabel() {
        String label = "The Label";
        builder.addLabel(label, userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThatIssueUpdatedTimestampWasUpdated(issue);
    }

    @Test
    public void shouldBuildIssueWithLabelledEvent() {
        String label = "The Label";
        builder.addLabel(label, userId, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getEvents(), hasItem(new IssueLabelled(userId, TIMESTAMP, label)));
    }

    @Test
    public void shouldBuildIssueWithEmptyCommentsByDefault() {
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getComments(), is(empty()));
    }

    @Test
    public void shouldBuildIssueWithAddedComment() {
        Comment comment = mock(Comment.class);
        builder.addComment(comment, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getComments(), hasItem(comment));
    }

    @Test
    public void shouldUpdateTimestampOnAddComment() {
        Comment comment = mock(Comment.class);
        builder.addComment(comment, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThatIssueUpdatedTimestampWasUpdated(issue);
    }

    @Test
    public void shouldUpdateComment() {
        Comment comment = mock(Comment.class);
        Comment replacementComment = mock(Comment.class);
        builder.addComment(comment, TIMESTAMP);
        builder.replaceComment(comment, replacementComment, TIMESTAMP);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getComments(), hasItem(replacementComment));
        assertThatIssueUpdatedTimestampWasUpdated(issue);
    }

    @Test
    public void shouldUpdateMentionsOnBuild() {
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getMentions(), hasItem(mention));
    }

    @Test
    public void shouldUpdateParticipantsOnBuild() {
        UserId participant = mock(UserId.class);
        UserIdMention userIdMention = mock(UserIdMention.class);
        when(userIdMention.getMentionedUserId()).thenReturn(Optional.of(participant));
        when(parsedMention.getParsedMention()).thenReturn(userIdMention);
        Issue issue = builder.build(mentionParser);
        assertThat(issue.getParticipants(), hasItems(creator, participant));
    }


    private void assertThatIssueUpdatedTimestampWasUpdated(Issue issue) {
        assertThat(issue.getUpdatedAt(), is(Optional.of(TIMESTAMP)));
    }

}
