package edu.stanford.bmir.protege.web.shared.issues.events;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.annotation.TypeAlias;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 16
 */
@TypeAlias("IssueUnlocked")
public class IssueUnlocked extends AbstractIssueEvent {

    public IssueUnlocked(@Nonnull UserId userId, long timestamp) {
        super(userId, timestamp);
    }

    @GwtSerializationConstructor
    private IssueUnlocked() {
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId(), getTimestamp());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssueUnlocked)) {
            return false;
        }
        IssueUnlocked other = (IssueUnlocked) obj;
        return this.getTimestamp() == other.getTimestamp()
                && this.getUserId().equals(other.getUserId());
    }


    @Override
    public String toString() {
        return toStringHelper("IssueUnlocked")
                .add("userId", getUserId())
                .add("timestamp", getTimestamp())
                .toString();
    }
}
