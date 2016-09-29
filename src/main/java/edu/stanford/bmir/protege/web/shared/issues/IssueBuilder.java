package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.issues.events.*;
import edu.stanford.bmir.protege.web.shared.issues.mention.MentionParser;
import edu.stanford.bmir.protege.web.shared.issues.mention.UserIdMention;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Sep 2016
 */
public class IssueBuilder {

    @Nonnull
    private final ProjectId projectId;

    private final int number;

    @Nonnull
    private final UserId creator;

    private final long createdAt;

    @Nonnull
    private Optional<Long> updatedAt = Optional.empty();

    @Nonnull
    private List<OWLEntity> targetEntities = new ArrayList<>();

    @Nonnull
    private String title = "";

    @Nonnull
    private String body = "";

    @Nonnull
    private Status status = Status.OPEN;

    @Nonnull
    private List<UserId> assignees = new ArrayList<>();

    @Nonnull
    private Optional<Milestone> milestone = Optional.empty();

    @Nonnull
    private LockSetting lockSetting = LockSetting.UNLOCKED;

    @Nonnull
    private List<String> labels = new ArrayList<>();

    @Nonnull
    private List<Comment> comments = new ArrayList<>();

    @Nonnull
    private List<Mention> mentions = new ArrayList<>();

    @Nonnull
    private List<UserId> participants = new ArrayList<>();

    @Nonnull
    private List<IssueEvent> events = new ArrayList<>();

    public IssueBuilder(@Nonnull ProjectId projectId, int number, @Nonnull UserId creator, long createdAt) {
        this.projectId = projectId;
        this.number = number;
        this.creator = creator;
        this.createdAt = createdAt;
    }

    public IssueBuilder(@Nonnull ProjectId projectId, int number, @Nonnull UserId creator, long createdAt, @Nonnull Optional<Long> updatedAt, @Nonnull  List<OWLEntity> targetEntities, @Nonnull String title, @Nonnull String body, @Nonnull Status status, @Nonnull List<UserId> assignees, @Nonnull Optional<Milestone> milestone, @Nonnull LockSetting lockSetting, @Nonnull List<String> labels, @Nonnull List<Comment> comments, @Nonnull List<Mention> mentions, @Nonnull List<UserId> participants, @Nonnull List<IssueEvent> events) {
        this.projectId = checkNotNull(projectId);
        this.number = number;
        this.creator = checkNotNull(creator);
        this.createdAt = checkNotNull(createdAt);
        this.updatedAt = checkNotNull(updatedAt);
        this.targetEntities.addAll(checkNotNull(targetEntities));
        this.title = checkNotNull(title);
        this.body = checkNotNull(body);
        this.status = checkNotNull(status);
        this.assignees = checkNotNull(assignees);
        this.milestone = checkNotNull(milestone);
        this.lockSetting = checkNotNull(lockSetting);
        this.labels.addAll(checkNotNull(labels));
        this.comments.addAll(checkNotNull(comments));
        this.mentions.addAll(checkNotNull(mentions));
        this.participants.addAll(checkNotNull(participants));
        this.events = checkNotNull(events);
    }

    public Issue build(MentionParser parser) {
        ImmutableSet.Builder<Mention> mentionsBuilder = ImmutableSet.builder();
        parser.parseMentions(body).stream()
                .map(pm -> pm.getParsedMention())
                .forEach(mentionsBuilder::add);
        comments.stream()
                .flatMap(c -> parser.parseMentions(c.getBody()).stream())
                .map(pm -> pm.getParsedMention())
                .forEach(mentionsBuilder::add);
        ImmutableSet<Mention> mentions = mentionsBuilder.build();
        ImmutableSet.Builder<UserId> participantsBuilder = ImmutableSet.builder();
        participantsBuilder.addAll(participants);
        mentions.stream()
                .map(m -> m.getMentionedUserId())
                .forEach(u -> u.ifPresent(participantsBuilder::add));

        return new Issue(
                projectId,
                number,
                creator,
                createdAt,
                updatedAt,
                ImmutableList.copyOf(targetEntities),
                title,
                body,
                status,
                ImmutableList.copyOf(assignees),
                milestone,
                lockSetting,
                ImmutableList.copyOf(labels),
                ImmutableList.copyOf(comments),
                ImmutableList.copyOf(mentions),
                ImmutableList.copyOf(participantsBuilder.build()),
                ImmutableList.copyOf(events));
    }

    @Nonnull
    public IssueBuilder updatedAt(long timestamp) {
        this.updatedAt = Optional.of(timestamp);
        return this;
    }

    @Nonnull
    public IssueBuilder withTitle(@Nonnull String title) {
        this.title = checkNotNull(title);
        return this;
    }

    @Nonnull
    public IssueBuilder updateTitle(@Nonnull String title, @Nonnull UserId userId, long timestamp) {
        if (this.title.equals(title)) {
            return this;
        }
        this.title = checkNotNull(title);
        this.updatedAt = Optional.of(timestamp);
        this.events.add(new IssueRenamed(checkNotNull(userId), timestamp, title, this.title));
        return this;
    }

    @Nonnull
    public IssueBuilder withBody(@Nonnull String body) {
        this.body = checkNotNull(body);
        return this;
    }

    @Nonnull
    public IssueBuilder updateBody(@Nonnull String body, long timestamp) {
        if (!this.body.equals(body)) {
            this.body = checkNotNull(body);
            this.updatedAt = Optional.of(timestamp);
        }
        return this;
    }

    @Nonnull
    public IssueBuilder close(@Nonnull UserId userId, long timestamp) {
        if (this.status != Status.CLOSED) {
            this.status = Status.CLOSED;
            this.updatedAt = Optional.of(timestamp);
            this.events.add(new IssueClosed(checkNotNull(userId), timestamp));
        }
        return this;
    }

    @Nonnull
    public IssueBuilder reopen(@Nonnull UserId userId, long timestamp) {
        if (this.status != Status.OPEN) {
            this.status = Status.OPEN;
            this.updatedAt = Optional.of(timestamp);
            this.events.add(new IssueReopened(checkNotNull(userId), timestamp));
        }
        return this;
    }

    @Nonnull
    public IssueBuilder assignTo(@Nonnull UserId assignee, @Nonnull UserId userId, long timestamp) {
        if (!this.assignees.contains(assignee)) {
            this.assignees.add(assignee);
            this.updatedAt = Optional.of(timestamp);
            this.events.add(new IssueAssigned(checkNotNull(userId), timestamp, assignee));
        }
        return this;
    }

    @Nonnull
    public IssueBuilder unassign(@Nonnull  UserId assignee, @Nonnull UserId userId, long timestamp) {
        if (!this.assignees.contains(checkNotNull(assignee))) {
            this.updatedAt = Optional.of(timestamp);
            this.events.add(new IssueUnassigned(checkNotNull(userId), timestamp, assignee));
            this.assignees.add(assignee);
        }
        return this;
    }

    @Nonnull
    public IssueBuilder milestone(@Nonnull Milestone milestone, UserId userId, long timestamp) {
        Optional<Milestone> theMilestone = Optional.of(checkNotNull(milestone));
        if (!this.milestone.equals(theMilestone)) {
            this.milestone = theMilestone;
            this.updatedAt = Optional.of(timestamp);
            this.events.add(new IssueMilestoned(userId, timestamp, milestone));
        }
        return this;
    }

    @Nonnull
    public IssueBuilder demilestone(@Nonnull UserId userId, long timestamp) {
        this.milestone.ifPresent(m -> {
            this.milestone = Optional.empty();
            this.updatedAt = Optional.of(timestamp);
            this.events.add(new IssueDemilestoned(checkNotNull(userId), timestamp, m));
        });
        return this;
    }

    @Nonnull
    public IssueBuilder addLabel(@Nonnull String label, @Nonnull UserId userId, long timestamp) {
        if (this.labels.add(label)) {
            this.updatedAt = Optional.of(timestamp);
            this.events.add(new IssueLabelled(checkNotNull(userId), timestamp, label));
        }
        return this;
    }

    @Nonnull
    public IssueBuilder removeLabel(@Nonnull String label, @Nonnull UserId userId, long timestamp) {
        if (this.labels.remove(label)) {
            this.updatedAt = Optional.of(timestamp);
            this.events.add(new IssueUnlabelled(checkNotNull(userId), timestamp, label));
        }
        return this;
    }

    @Nonnull
    public IssueBuilder addComment(@Nonnull Comment comment, long timestamp) {
        if (this.comments.add(checkNotNull(comment))) {
            this.updatedAt = Optional.of(timestamp);
        }
        return this;
    }

    @Nonnull
    public IssueBuilder replaceComment(@Nonnull Comment comment, @Nonnull Comment replacementComment, long timestamp) {
        int index = this.comments.indexOf(comment);
        if(index != -1) {
            comments.set(index, replacementComment);
            this.updatedAt = Optional.of(timestamp);
        }
        return this;
    }

    @Nonnull
    public IssueBuilder lock(@Nonnull UserId userId, long timestamp) {
        if(!this.lockSetting.equals(LockSetting.LOCKED)) {
            this.lockSetting = LockSetting.LOCKED;
            this.updatedAt = Optional.of(timestamp);
            this.events.add(new IssueLocked(checkNotNull(userId), timestamp));
        }
        return this;
    }

    @Nonnull
    public IssueBuilder unlock(@Nonnull UserId userId, long timestamp) {
        if(!this.lockSetting.equals(LockSetting.UNLOCKED)) {
            this.lockSetting = LockSetting.UNLOCKED;
            this.updatedAt = Optional.of(timestamp);
            this.events.add(new IssueUnlocked(checkNotNull(userId), timestamp));
        }
        return this;
    }

    @Nonnull
    public IssueBuilder addParticipant(@Nonnull UserId participant) {
        if (!participants.contains(checkNotNull(participant))) {
            this.participants.add(participant);
        }
        return this;
    }

    @Nonnull
    public IssueBuilder removeParticipant(@Nonnull UserId participant) {
        this.participants.remove(checkNotNull(participant));
        return this;
    }

    public IssueBuilder setTargetEntities(@Nonnull Collection<OWLEntity> targetEntity) {
        this.targetEntities.clear();
        this.targetEntities.addAll(targetEntity);
        return this;
    }

}
