package edu.stanford.bmir.protege.web.shared.issues.events;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
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
public class IssueLabelled extends AbstractIssueEvent {

    @Nonnull
    private String label;

    @GwtSerializationConstructor
    private IssueLabelled() {
    }

    public IssueLabelled(@Nonnull UserId userId, long timestamp, @Nonnull String label) {
        super(userId, timestamp);
        this.label = checkNotNull(label);
    }

    @Nonnull
    public String getLabel() {
        return label;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId(), getTimestamp(), getLabel());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssueLabelled)) {
            return false;
        }
        IssueLabelled other = (IssueLabelled) obj;
        return this.getTimestamp() == other.getTimestamp()
                && this.getUserId().equals(other.getUserId())
                && this.getLabel().equals(other.getLabel());
    }


    @Override
    public String toString() {
        return toStringHelper("IssueLabelled")
                .add("userId", getUserId())
                .add("timestamp", getTimestamp())
                .add("label", label)
                .toString();
    }
}
