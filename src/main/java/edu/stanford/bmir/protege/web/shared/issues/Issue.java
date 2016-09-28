package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.issues.events.IssueEvent;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public class Issue implements IsSerializable {

    private int number;

    @Nonnull
    private String title;

    @Nonnull
    private String body;

    @Nonnull
    private UserId creator;

    private long createdAt;

    @Nonnull
    private Status status;


    @Nullable
    private Long updatedAt;

    @Nullable
    private Long closedAt;

    @Nullable
    private UserId assignee;

    @Nullable
    private Milestone milestone;

    @Nonnull
    private ImmutableList<String> labels;

    @Nonnull
    private ImmutableList<Comment> comments;

    @Nonnull
    private ImmutableList<IssueEvent> events;

    @Nonnull
    private ImmutableList<IssueTarget> issueTarget;

    @Nonnull
    private ImmutableList<UserId> participants;


    private Issue() {
    }


    public Issue(int number,
                 @Nonnull String title,
                 @Nonnull String body,
                 @Nonnull UserId creator,
                 long createdAt,
                 @Nonnull Optional<Long> updatedAt,
                 @Nonnull Optional<Long> closedAt,
                 @Nonnull Status status,
                 @Nonnull Optional<UserId> assignee,
                 @Nonnull Optional<Milestone> milestone,
                 @Nonnull ImmutableList<String> labels,
                 @Nonnull ImmutableList<Comment> comments,
                 @Nonnull ImmutableList<IssueEvent> events,
                 @Nonnull ImmutableList<UserId> participants,
                 @Nonnull List<IssueTarget> issueTargets) {
        this.number = number;
        this.issueTarget = ImmutableList.copyOf(issueTargets);
        this.title = title;
        this.body = body;
        this.creator = creator;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt.orElse(null);
        this.closedAt = closedAt.orElse(null);
        this.status = status;
        this.assignee = assignee.orElse(null);
        this.milestone = milestone.orElse(null);
        this.comments = checkNotNull(comments);
        this.participants = checkNotNull(participants);
        this.labels = labels;
        this.events = events;
    }

    public int getNumber() {
        return number;
    }

    @Nonnull
    public String getTitle() {
        return title;
    }

    @Nonnull
    public String getBody() {
        return body;
    }

    @Nonnull
    public UserId getCreator() {
        return creator;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @Nonnull
    public Optional<Long> getUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    @Nonnull
    public Optional<Long> getClosedAt() {
        return Optional.ofNullable(closedAt);
    }

    @Nonnull
    public Status getStatus() {
        return status;
    }

    @Nonnull
    public Optional<UserId> getAssignee() {
        return Optional.ofNullable(assignee);
    }

    @Nonnull
    public Optional<Milestone> getMilestone() {
        return Optional.ofNullable(milestone);
    }

    @Nonnull
    public ImmutableList<String> getLabels() {
        return labels;
    }

    @Nonnull
    public ImmutableList<Comment> getComments() {
        return comments;
    }

    @Nonnull
    public ImmutableList<UserId> getParticipants() {
        return participants;
    }

    @Nonnull
    public ImmutableList<IssueEvent> getEvents() {
        return events;
    }

    @Nonnull
    public List<IssueTarget> getIssueTarget() {
        return issueTarget;
    }


    @Override
    public String toString() {
        return toStringHelper("Issue")
                .addValue(number)
                .add("title", title)
                .add("creator", creator)
                .add("createdAt", createdAt)
                .add("body", body)
                .add("status", status)
                .add("assignedTo", assignee)
                .add("milestone", milestone)
                .add("comments", comments)
                .add("events", events)
                .toString();
    }
}
