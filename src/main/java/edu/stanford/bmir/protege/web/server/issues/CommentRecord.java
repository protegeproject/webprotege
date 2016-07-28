package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
@Document(collection = "CommentRecords")
@TypeAlias("CommentRecord")
@CompoundIndex(unique = true, def = "{'projectId': 1, 'issueNumber': 1}")
public class CommentRecord implements HasTargetEntities {

    private final ProjectId projectId;

    private final long issueNumber;

    private final UserId userId;

    private final long createdAt;

    private final long updatedAt;

    private final String title;

    private final String body;

    private final ImmutableList<OWLEntity> targetEntities;

    public CommentRecord(ProjectId projectId, long issueNumber, UserId userId, long createdAt, long updatedAt, String title, String body, ImmutableList<OWLEntity> targetEntities) {
        this.projectId = projectId;
        this.issueNumber = issueNumber;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.title = title;
        this.body = body;
        this.targetEntities = targetEntities;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public long getIssueNumber() {
        return issueNumber;
    }

    public UserId getUserId() {
        return userId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    public ImmutableList<OWLEntity> getTargetEntities() {
        return targetEntities;
    }
}
