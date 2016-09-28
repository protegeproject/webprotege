package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.issues.events.IssueEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
@Document(collection = "Issues")
@TypeAlias("IssueRecord")
//@CompoundIndexes({
//        @CompoundIndex(unique = true, def = "{'projectId': 1, 'number': 1}")
//})
public class IssueRecord {

    @Nonnull
    private final ProjectId projectId;

    private final int number;

    @Nonnull
    private final UserId creator;

    private final long createdAt;

    @Nonnull
    private final String title;

    @Nonnull
    private final String body;

    @Nonnull
    private final Status status;

    @Nullable
    private final UserId assignee;

    @Nullable
    private final Milestone milestone;

    @Nonnull
    private final List<String> labels;

    @Nonnull
    private final List<Comment> comments;

    @Nonnull
    private final List<Mention> mentions;

    @Nonnull
    private final List<IssueEvent> events;



    /**
     * Creates an issues record
     * @param projectId The project that the issue pertains to.
     * @param number The issue number.
     * @param creator The issue creator.  That is, the user id of the person that created the issue.
     * @param createdAt A timestamp specifying when the issue was created.
     * @param title The issue title.  May be empty.
     * @param body The issue body. May be empty.
     * @param status The status of the issue.
     * @param assignee The UserId of the person that the issue was assigned to. An absent value indicates that the
*                 issue has not been assigned to any user.
     * @param milestone A milestone for the issue.  An empty string indicates that there is no milestone set.
     * @param labels A list of labels for the issue.  The values in the list must not be {@code null}.
     * @param mentions A list of Mention (things that are mentioned in the issue body or in the issue comments.
     */
    public IssueRecord(@Nonnull ProjectId projectId,
                       int number,
                       @Nonnull UserId creator,
                       long createdAt,
                       @Nonnull String title,
                       @Nonnull String body,
                       @Nonnull Status status,
                       @Nonnull Optional<UserId> assignee,
                       @Nonnull Optional<Milestone> milestone,
                       @Nonnull ImmutableList<String> labels,
                       @Nonnull ImmutableList<Comment> comments,
                       @Nonnull ImmutableList<Mention> mentions,
                       @Nonnull ImmutableList<IssueEvent> events) {
        this.projectId = checkNotNull(projectId);
        this.number = number;
        this.creator = checkNotNull(creator);
        this.createdAt = createdAt;
        this.title = checkNotNull(title);
        this.body = checkNotNull(body);
        this.status = checkNotNull(status);
        this.assignee = checkNotNull(assignee).orElse(null);
        this.milestone = checkNotNull(milestone).orElse(null);
        this.labels = new ArrayList<>(checkNotNull(labels));
        for(String label : labels) {
            checkNotNull(label);
        }
        this.mentions = new ArrayList<>(checkNotNull(mentions));
        for(Mention mention : mentions) {
            checkNotNull(mention);
        }

        this.events = new ArrayList<>(checkNotNull(events));
        for(IssueEvent record : events) {
            checkNotNull(record);
        }
        this.comments = new ArrayList<>(checkNotNull(comments));
        for(Comment commentRecord : comments) {
            checkNotNull(commentRecord);
        }
    }

    /**
     * This is a persistence constructor for SpringData.
     */
    @PersistenceConstructor
    protected IssueRecord(@Nonnull ProjectId projectId,
                          int number,
                          @Nonnull UserId creator,
                          long createdAt,
                          @Nonnull String title,
                          @Nonnull String body,
                          @Nonnull Status status,
                          @Nullable UserId assignee,
                          @Nullable Milestone milestone,
                          @Nonnull List<String> labels,
                          @Nonnull List<Comment> comments,
                          @Nonnull List<Mention> mentions,
                          @Nonnull List<IssueEvent> events) {
        this(
                projectId,
                number,
                creator,
                createdAt,
                title,
                body,
                status,
                Optional.ofNullable(assignee),
                Optional.ofNullable(milestone),
                ImmutableList.copyOf(labels),
                ImmutableList.copyOf(comments),
                ImmutableList.copyOf(mentions),
                ImmutableList.copyOf(events)
        );
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    public long getNumber() {
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
    public List<String> getLabels() {
        return labels;
    }

    @Nonnull
    public List<Mention> getMentions() {
        return mentions;
    }

    @Nonnull
    public List<IssueEvent> getEvents() {
        return events;
    }

    @Nonnull
    public List<Comment> getComments() {
        return comments;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                projectId,
                number,
                title,
                body,
                creator,
                createdAt,
                status,
                assignee,
                milestone,
                labels,
                events,
                comments
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssueRecord)) {
            return false;
        }
        IssueRecord other = (IssueRecord) obj;
        return this.projectId.equals(other.projectId)
                && this.number == other.number
                && this.title.equals(other.title)
                && this.body.equals(other.body)
                && this.creator.equals(other.creator)
                && this.createdAt == other.createdAt
                && this.status == other.status
                && Objects.equal(this.assignee, other.assignee)
                && Objects.equal(this.milestone, other.milestone)
                && this.labels.equals(other.labels)
                && this.comments.equals(other.comments)
                && this.mentions.equals(other.mentions)
                && this.events.equals(other.events);
    }

    @Override
    public String toString() {
        return toStringHelper("IssueRecord")
                .addValue(projectId)
                .add("number", number)
                .add("owner", creator)
                .add("createdAt", createdAt)
                .addValue(status)
                .add("assignee", assignee)
                .add("milestone", milestone)
                .add("labels", labels)
                .add("body", body)
                .add("comments", comments)
                .add("mentions", mentions)
                .add("events", events)
                .toString();
    }

    public Issue toIssue() {
        return new Issue(
                number,
                title,
                body,
                creator,
                createdAt,
                Optional.<Long>empty(),
                Optional.<Long>empty(),
                status,
                getAssignee(),
                getMilestone(),
                ImmutableList.copyOf(labels),
                ImmutableList.copyOf(comments),
                ImmutableList.copyOf(events),
                ImmutableList.of(),
                ImmutableList.of()

        );
    }


}
