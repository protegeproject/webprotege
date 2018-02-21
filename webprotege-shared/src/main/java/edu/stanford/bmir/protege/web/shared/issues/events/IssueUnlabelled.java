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
public class IssueUnlabelled extends AbstractIssueEvent {

    @Nonnull
    private String label;

    public IssueUnlabelled(@Nonnull UserId userId,
                           long timestamp,
                           @Nonnull String label) {
        super(userId, timestamp);
        this.label = checkNotNull(label);
    }

    @GwtSerializationConstructor
    private IssueUnlabelled() {
    }

    @Nonnull
    public String getLabel() {
        return label;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId(), getTimestamp(), label);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssueUnlabelled)) {
            return false;
        }
        IssueUnlabelled other = (IssueUnlabelled) obj;
        return this.getTimestamp() == other.getTimestamp()
                && this.getUserId().equals(other.getUserId())
                && this.label.equals(other.label);
    }


    @Override
    public String toString() {
        return toStringHelper("IssueUnlabelled")
                .add("userId", getUserId())
                .add("timestamp", getTimestamp())
                .add("label", label)
                .toString();
    }
}
