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
public class IssueReferenced extends AbstractIssueEvent {

    private int issueNumber;

    private int referencedByIssueNumber;

    @PersistenceConstructor
    public IssueReferenced(@Nonnull UserId userId,
                           long timestamp,
                           int issueNumber, int referencedByIssueNumber) {
        super(userId, timestamp);
        this.issueNumber = issueNumber;
        this.referencedByIssueNumber = referencedByIssueNumber;
    }

    @GwtSerializationConstructor
    private IssueReferenced() {
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    public int getReferencedByIssueNumber() {
        return referencedByIssueNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId(), getTimestamp(), issueNumber, referencedByIssueNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssueReferenced)) {
            return false;
        }
        IssueReferenced other = (IssueReferenced) obj;
        return this.getTimestamp() == other.getTimestamp()
                && this.getIssueNumber() == other.getIssueNumber()
                && this.getReferencedByIssueNumber() == other.getReferencedByIssueNumber()
                && this.getUserId().equals(other.getUserId());
    }


    @Override
    public String toString() {
        return toStringHelper("IssueReferenced")
                .add("userId", getUserId())
                .add("timestamp", getTimestamp())
                .add("issueNumber", issueNumber)
                .add("referencedByIssueNumber", referencedByIssueNumber)
                .toString();
    }
}
