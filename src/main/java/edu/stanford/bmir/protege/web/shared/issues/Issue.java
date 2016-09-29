package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.issues.events.IssueEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
@Document(collection = "Issues" )
@TypeAlias("Issue" )
@CompoundIndexes({
        @CompoundIndex(unique = true, def = "{'projectId': 1, 'number': 1}" )
})
public class Issue implements IsSerializable {

    @Nonnull
    private ProjectId projectId;

    private int number;

    @Nonnull
    private UserId creator;

    private long createdAt;

    @Null
    private Long updatedAt;

    @Nonnull
    private List<OWLEntity> targetEntities;

    @Nonnull
    private String title;

    @Nonnull
    private String body;

    @Nonnull
    private Status status;

    @Nonnull
    private List<UserId> assignees;

    @Nullable
    private Milestone milestone;

    @Nonnull
    private LockSetting lockSetting;

    @Nonnull
    private List<String> labels;

    @Nonnull
    private List<Comment> comments;

    @Nonnull
    private List<Mention> mentions;

    @Nonnull
    private List<UserId> participants;

    @Nonnull
    private List<IssueEvent> events;


    /**
     * Creates an issues record
     * @param projectId    The project that the issue pertains to.
     * @param number       The issue number.
     * @param creator      The issue creator.  That is, the user id of the person that created the issue.
     * @param createdAt    A timestamp specifying when the issue was created.
     * @param updatedAt    A timestamp specifying when the issue was last updated.  An empty value indicates that the
*                     issue has not been updated.
     * @param targetEntities
     * @param title        The issue title.  May be empty.
     * @param body         The issue body. May be empty.
     * @param status       The status of the issue.
     * @param assignees    The UserId of the person that the issue was assigned to. An absent value indicates that the
*                     issue has not been assigned to any user.
     * @param milestone    A milestone for the issue.  An empty string indicates that there is no milestone set.
     * @param lockSetting  The setting that specifies whether or not the issue is locked.
     * @param labels       A list of labels for the issue.  The values in the list must not be {@code null}.
     * @param mentions     A list of Mentions that occur in the body of the issue or in issue comments.
     * @param participants A list of users that participate in the tracking of this issue.
     */
    public Issue(@Nonnull ProjectId projectId,
                 int number,
                 @Nonnull UserId creator,
                 long createdAt,
                 @Nonnull Optional<Long> updatedAt,
                 @Nonnull ImmutableList<OWLEntity> targetEntities,
                 @Nonnull String title,
                 @Nonnull String body,
                 @Nonnull Status status,
                 @Nonnull ImmutableList<UserId> assignees,
                 @Nonnull Optional<Milestone> milestone,
                 @Nonnull LockSetting lockSetting, @Nonnull ImmutableList<String> labels,
                 @Nonnull ImmutableList<Comment> comments,
                 @Nonnull ImmutableList<Mention> mentions,
                 ImmutableList<UserId> participants,
                 @Nonnull ImmutableList<IssueEvent> events) {
        this.projectId = checkNotNull(projectId);
        this.number = number;
        this.creator = checkNotNull(creator);
        this.createdAt = createdAt;
        this.updatedAt = checkNotNull(updatedAt).orElse(null);
        this.targetEntities = checkNotNull(targetEntities);
        this.title = checkNotNull(title);
        this.body = checkNotNull(body);
        this.status = checkNotNull(status);
        this.assignees = checkNotNull(assignees);
        this.milestone = checkNotNull(milestone).orElse(null);
        this.lockSetting = checkNotNull(lockSetting);
        this.labels = checkNotNull(labels);
        this.mentions = checkNotNull(mentions);
        this.events = checkNotNull(events);
        this.participants = checkNotNull(participants);
        this.comments = checkNotNull(comments);
    }

    /**
     * This is a persistence constructor for SpringData.
     */
    @PersistenceConstructor
    protected Issue(@Nonnull ProjectId projectId,
                    int number,
                    @Nonnull UserId creator,
                    long createdAt,
                    @Nullable Long updatedAt,
                    @Nonnull List<OWLEntity> targetEntities,
                    @Nonnull String title,
                    @Nonnull String body,
                    @Nonnull Status status,
                    @Nonnull List<UserId> assignees,
                    @Nullable Milestone milestone,
                    @Nonnull LockSetting lockSetting,
                    @Nonnull List<String> labels,
                    @Nonnull List<Comment> comments,
                    @Nonnull List<Mention> mentions,
                    @Nonnull List<UserId> participants,
                    @Nonnull List<IssueEvent> events) {
        this.projectId = checkNotNull(projectId);
        this.number = number;
        this.creator = checkNotNull(creator);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.targetEntities = ImmutableList.copyOf(targetEntities);
        this.title = checkNotNull(title);
        this.body = checkNotNull(body);
        this.status = checkNotNull(status);
        this.assignees = ImmutableList.copyOf(checkNotNull(assignees));
        this.milestone = milestone;
        this.lockSetting = checkNotNull(lockSetting);
        this.labels = ImmutableList.copyOf(checkNotNull(labels));
        this.comments = ImmutableList.copyOf(checkNotNull(comments));
        this.mentions = ImmutableList.copyOf(checkNotNull(mentions));
        this.participants = ImmutableList.copyOf(checkNotNull(participants));
        this.events = ImmutableList.copyOf(checkNotNull(events));
    }

    @GwtSerializationConstructor
    private Issue() {
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
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

    public Optional<Long> getUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    @Nonnull
    public List<OWLEntity> getTargetEntities() {
        return ImmutableList.copyOf(targetEntities);
    }

    @Nonnull
    public Status getStatus() {
        return status;
    }

    @Nonnull
    public List<UserId> getAssignees() {
        return ImmutableList.copyOf(assignees);
    }

    @Nonnull
    public Optional<Milestone> getMilestone() {
        return Optional.ofNullable(milestone);
    }

    @Nonnull
    public ImmutableList<String> getLabels() {
        return ImmutableList.copyOf(labels);
    }

    @Nonnull
    public ImmutableList<Comment> getComments() {
        return ImmutableList.copyOf(comments);
    }

    @Nonnull
    public ImmutableList<Mention> getMentions() {
        return ImmutableList.copyOf(mentions);
    }

    @Nonnull
    public List<UserId> getParticipants() {
        return ImmutableList.copyOf(participants);
    }

    @Nonnull
    public LockSetting getLockSetting() {
        return lockSetting;
    }

    @Nonnull
    public List<IssueEvent> getEvents() {
        return ImmutableList.copyOf(events);
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
                updatedAt,
                status,
                assignees,
                milestone,
                lockSetting,
                labels,
                events,
                participants,
                comments
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Issue)) {
            return false;
        }
        Issue other = (Issue) obj;
        return this.projectId.equals(other.projectId)
                && this.number == other.number
                && this.title.equals(other.title)
                && this.body.equals(other.body)
                && this.creator.equals(other.creator)
                && this.createdAt == other.createdAt
                && Objects.equal(this.updatedAt, other.updatedAt)
                && this.targetEntities.equals(other.targetEntities)
                && this.status == other.status
                && Objects.equal(this.assignees, other.assignees)
                && Objects.equal(this.milestone, other.milestone)
                && this.lockSetting.equals(other.lockSetting)
                && this.labels.equals(other.labels)
                && this.comments.equals(other.comments)
                && this.mentions.equals(other.mentions)
                && this.participants.equals(other.participants)
                && this.events.equals(other.events);
    }

    @Override
    public String toString() {
        return toStringHelper("IssueRecord" )
                .addValue(projectId)
                .add("number" , number)
                .add("owner" , creator)
                .add("createdAt" , createdAt)
                .add("updatedAt" , updatedAt)
                .add("targetEntities", targetEntities)
                .addValue(status)
                .add("assignees" , assignees)
                .add("milestone" , milestone)
                .add("locked" , lockSetting)
                .add("labels" , labels)
                .add("body" , body)
                .add("comments" , comments)
                .add("mentions" , mentions)
                .add("participants" , participants)
                .add("events" , events)
                .toString();
    }

    /**
     * Creates an {@link IssueBuilder} initialised with the specified values.  All other values will have default values.
     *
     * @param projectId The project id for the issue.
     * @param number    The issue number.
     * @param creator   The issue creator.
     * @param createdAt The time that the issue was created at.
     * @return The builder.
     */
    @Nonnull
    public static IssueBuilder builder(@Nonnull ProjectId projectId,
                                       int number,
                                       @Nonnull UserId creator,
                                       long createdAt) {
        return new IssueBuilder(projectId, number, creator, createdAt);
    }

    /**
     * Creates an {@link IssueBuilder} that is initialised with the values contained in this issue.
     *
     * @return The builder.
     */
    @Nonnull
    public IssueBuilder builder() {
        return new IssueBuilder(
                projectId,
                number,
                creator,
                createdAt,
                Optional.ofNullable(updatedAt),
                targetEntities,
                title,
                body,
                status,
                assignees,
                Optional.ofNullable(milestone),
                lockSetting,
                labels,
                comments,
                mentions,
                participants,
                events);
    }

}
