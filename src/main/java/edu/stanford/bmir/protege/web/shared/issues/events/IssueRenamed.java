package edu.stanford.bmir.protege.web.shared.issues.events;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.annotation.TypeAlias;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 16
 */
@TypeAlias("IssueRenamed")
public class IssueRenamed extends AbstractIssueEvent {

    @Nonnull
    private String from;

    @Nonnull
    private String to;

    public IssueRenamed(@Nonnull UserId userId, long timestamp, @Nonnull String from, @Nonnull String to) {
        super(userId, timestamp);
        this.from = checkNotNull(from);
        this.to = checkNotNull(to);
    }

    @GwtSerializationConstructor
    private IssueRenamed() {
    }

    @Nonnull
    public String getFrom() {
        return from;
    }

    @Nonnull
    public String getTo() {
        return to;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId(), getTimestamp(), from, to);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssueRenamed)) {
            return false;
        }
        IssueRenamed other = (IssueRenamed) obj;
        return this.getTimestamp() == other.getTimestamp()
                && this.getUserId().equals(other.getUserId())
                && this.from.equals(other.from)
                && this.to.equals(other.to);
    }


    @Override
    public String toString() {
        return toStringHelper("IssueRenamed")
                .add("userId", getUserId())
                .add("timestamp", getTimestamp())
                .add("from", from)
                .add("to", to)
                .toString();
    }
}
