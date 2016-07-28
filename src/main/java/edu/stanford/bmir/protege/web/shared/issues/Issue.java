package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.annotation.TypeAlias;

import java.io.Serializable;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public class Issue implements IsSerializable {

    private  long number;

    private  ImmutableList<IssueTarget> issueTarget;

    private  String title;

    private  String body;

    private  UserId owner;

    private  long createdAt;

    private  long updatedAt;

    private  long closedAt;

    private  Status status;

    private  UserId assignee;

    private  int numberOfComments;

    private  String milestone;

    private  ImmutableList<String> labels;

    private Issue() {
    }

    public Issue(long number, List<IssueTarget> issueTargets, String title, String body, UserId owner, long createdAt, long updatedAt, long closedAt, Status status, UserId assignee, int numberOfComments, String milestone, ImmutableList<String> labels) {
        this.number = number;
        this.issueTarget = ImmutableList.copyOf(issueTargets);
        this.title = title;
        this.body = body;
        this.owner = owner;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.closedAt = closedAt;
        this.status = status;
        this.assignee = assignee;
        this.numberOfComments = numberOfComments;
        this.milestone = milestone;
        this.labels = labels;
    }

    public long getNumber() {
        return number;
    }

    public List<IssueTarget> getIssueTarget() {
        return issueTarget;
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

    public UserId getAssignee() {
        return assignee;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public String getMilestone() {
        return milestone;
    }

    public ImmutableList<String> getLabels() {
        return labels;
    }
}
