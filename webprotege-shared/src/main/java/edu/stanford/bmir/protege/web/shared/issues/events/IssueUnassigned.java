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
public class IssueUnassigned extends AbstractIssueEvent {

    @Nonnull
    private UserId assignee;

    public IssueUnassigned(@Nonnull UserId userId,
                           long timestamp,
                           @Nonnull UserId assignee) {
        super(userId, timestamp);
        this.assignee = checkNotNull(assignee);
    }

    @GwtSerializationConstructor
    private IssueUnassigned() {
    }

    @Nonnull
    public UserId getAssignee() {
        return assignee;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTimestamp(), getUserId(), assignee);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssueUnassigned)) {
            return false;
        }
        IssueUnassigned other = (IssueUnassigned) obj;
        return this.getTimestamp() == other.getTimestamp()
                && this.getUserId().equals(other.getUserId())
                && this.assignee.equals(other.assignee);
    }


    @Override
    public String toString() {
        return toStringHelper("IssueUnassigned")
                .add("userId", getUserId())
                .add("timestamp", getTimestamp())
                .add("assignee", assignee)
                .toString();
    }
}
