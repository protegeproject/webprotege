package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.issues.Status;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
@Document(collection = "IssueRecords")
@TypeAlias("IssueRecord")
@CompoundIndexes({
        @CompoundIndex(unique = true, def = "{'projectId': 1, 'number': 1}"),
        @CompoundIndex(unique = false, def = "{'projectId': 1, 'targetEntities': 1}")
})
public class IssueRecord implements HasTargetEntities {

    @Indexed(unique = false)
    private final ProjectId projectId;

    @Indexed(unique = false)
    private final long number;

    private final String title;

    private final String body;

    private final UserId owner;

    private final long createdAt;

    private final long updatedAt;

    private final long closedAt;

    private final Status status;

    private final Optional<UserId> assignee;

    private final String milestone;

    private final List<String> labels;

    private final List<OWLEntity> targetEntities;

    public IssueRecord(ProjectId projectId, long number, String title, String body, UserId owner, long createdAt, long updatedAt, long closedAt, Status status, Optional<UserId> assignee, String milestone, List<String> labels, List<OWLEntity> targetEntities) {
        this.projectId = checkNotNull(projectId);
        this.number = number;
        this.title = checkNotNull(title);
        this.body = checkNotNull(body);
        this.owner = checkNotNull(owner);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.closedAt = closedAt;
        this.status = checkNotNull(status);
        this.assignee = checkNotNull(assignee);
        this.milestone = checkNotNull(milestone);
        this.labels = ImmutableList.copyOf(checkNotNull(labels));
        this.targetEntities = ImmutableList.copyOf(checkNotNull(targetEntities));
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public long getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public UserId getOwner() {
        return owner;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public long getClosedAt() {
        return closedAt;
    }

    public Status getStatus() {
        return status;
    }

    public Optional<UserId> getAssignee() {
        return assignee;
    }

    public String getMilestone() {
        return milestone;
    }

    public List<String> getLabels() {
        return labels;
    }

    @Override
    public ImmutableList<OWLEntity> getTargetEntities() {
        return ImmutableList.copyOf(targetEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                projectId,
                number,
                title,
                body,
                owner,
                createdAt,
                updatedAt,
                closedAt,
                status,
                assignee,
                milestone,
                labels,
                targetEntities
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
                && this.owner.equals(other.owner)
                && this.createdAt == other.createdAt
                && this.updatedAt == other.updatedAt
                && this.closedAt == other.closedAt
                && this.status == other.status
                && this.assignee.equals(other.assignee)
                && this.milestone.equals(other.milestone)
                && this.labels.equals(other.labels)
                && this.targetEntities.equals(other.targetEntities);
    }

    @Override
    public String toString() {
        return toStringHelper("IssueRecord")
                .addValue(projectId)
                .add("number", number)
                .add("owner", owner)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .add("closedAt", closedAt)
                .addValue(status)
                .add("assignee", assignee)
                .add("milestone", milestone)
                .add("labels", labels)
                .add("targetEntities", targetEntities)
                .add("body", body)
                .toString();
    }


}
