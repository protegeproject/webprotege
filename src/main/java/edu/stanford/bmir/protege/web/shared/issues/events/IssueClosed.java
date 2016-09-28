package edu.stanford.bmir.protege.web.shared.issues.events;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.TypeAlias;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 16
 */
@TypeAlias("IssueClosed")
public class IssueClosed extends AbstractIssueEvent {

    @GwtSerializationConstructor
    private IssueClosed() {
    }

    @PersistenceConstructor
    public IssueClosed(@Nonnull UserId userId, long timestamp) {
        super(userId, timestamp);
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
        if (!(obj instanceof IssueClosed)) {
            return false;
        }
        IssueClosed other = (IssueClosed) obj;
        return this.getUserId().equals(other.getUserId())
                && this.getTimestamp() == other.getTimestamp();
    }


    @Override
    public String toString() {
        return toStringHelper("IssueClosed")
                .addValue(getUserId())
                .add("timestamp", getTimestamp())
                .toString();
    }

}
