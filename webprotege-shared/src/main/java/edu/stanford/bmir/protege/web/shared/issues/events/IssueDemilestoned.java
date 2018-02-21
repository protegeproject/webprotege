package edu.stanford.bmir.protege.web.shared.issues.events;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.issues.Milestone;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 16
 */
public class IssueDemilestoned extends AbstractIssueEvent {

    @Nonnull
    private Milestone milestone;

    @GwtSerializationConstructor
    private IssueDemilestoned() {
    }


    public IssueDemilestoned(@Nonnull UserId userId, long timestamp, @Nonnull Milestone milestone) {
        super(userId, timestamp);
        this.milestone = checkNotNull(milestone);
    }

    @Nonnull
    public Milestone getMilestone() {
        return milestone;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(milestone, getUserId(), getTimestamp());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssueDemilestoned)) {
            return false;
        }
        IssueDemilestoned other = (IssueDemilestoned) obj;
        return this.getTimestamp() == other.getTimestamp()
                && this.milestone.equals(other.milestone)
                && this.getUserId().equals(other.getUserId());
    }


    @Override
    public String toString() {
        return toStringHelper("IssueDemilestoned")
                .addValue(getUserId())
                .add("timestamp", getTimestamp())
                .addValue(milestone)
                .toString();
    }
}
