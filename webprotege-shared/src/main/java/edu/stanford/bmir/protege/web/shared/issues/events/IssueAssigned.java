package edu.stanford.bmir.protege.web.shared.issues.events;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 16
 */
public class IssueAssigned extends AbstractIssueEvent {

    @Nonnull
    private UserId assignee;

    @GwtSerializationConstructor
    private IssueAssigned() {
    }


    public IssueAssigned(@Nonnull UserId userId, long timestamp, @Nonnull UserId assignee) {
        super(userId, timestamp);
        this.assignee = checkNotNull(assignee);
    }

    @Nonnull
    public UserId getAssignee() {
        return assignee;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId(), getTimestamp(), getAssignee());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssueAssigned)) {
            return false;
        }
        IssueAssigned other = (IssueAssigned) obj;
        return this.getUserId().equals(other.getUserId())
                && this.getTimestamp() == other.getTimestamp()
                && this.getAssignee().equals(other.getAssignee());
    }


    @Override
    public String toString() {
        return toStringHelper("IssueAssigned")
                .addValue(getUserId())
                .add("timestamp", getTimestamp())
                .add("assignee", assignee)
                .toString();
    }
}
