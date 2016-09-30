package edu.stanford.bmir.protege.web.shared.issues.events;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.issues.Milestone;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.annotation.TypeAlias;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 16
 */
public class IssueMilestoned extends AbstractIssueEvent {

    @Nonnull
    private Milestone milestone;


    public IssueMilestoned(@Nonnull UserId userId, long timestamp, @Nonnull Milestone milestone) {
        super(userId, timestamp);
        this.milestone = checkNotNull(milestone);
    }

    @GwtSerializationConstructor
    private IssueMilestoned() {
    }

    @Nonnull
    public Milestone getMilestone() {
        return milestone;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId(), getTimestamp(), getMilestone());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssueMilestoned)) {
            return false;
        }
        IssueMilestoned other = (IssueMilestoned) obj;
        return this.getTimestamp() == other.getTimestamp()
                && this.getUserId().equals(other.getUserId())
                && this.getMilestone().equals(other.getMilestone());
    }


    @Override
    public String toString() {
        return toStringHelper("IssueMilestoned")
                .add("userId", getUserId())
                .add("timestamp", getTimestamp())
                .addValue(getMilestone())
                .toString();
    }
}
