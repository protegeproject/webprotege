package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public class IssueComment {

    private final int issueNumber;

    private final UserId userId;

    private final long createdAt;

    private final long updatedAt;

    private final String title;

    private final String body;

    public IssueComment(int issueNumber, UserId userId, long createdAt, long updatedAt, String title, String body) {
        this.issueNumber = issueNumber;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.title = title;
        this.body = body;
    }

    public int getIssueNumber() {
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
}
