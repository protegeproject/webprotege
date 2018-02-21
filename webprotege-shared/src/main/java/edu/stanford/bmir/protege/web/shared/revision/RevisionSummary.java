package edu.stanford.bmir.protege.web.shared.revision;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 */
public class RevisionSummary implements Serializable, IsSerializable, Comparable<RevisionSummary> {

    private RevisionNumber revisionNumber;

    private UserId userId;

    private long timestamp;
    
    private int changeCount;

    private String description;
    

    @GwtSerializationConstructor
    private RevisionSummary() {
    }

    public RevisionSummary(@Nonnull RevisionNumber revisionNumber,
                           @Nonnull UserId userId,
                           long timestamp,
                           int changeCount,
                           @Nonnull String description) {
        this.revisionNumber = checkNotNull(revisionNumber);
        this.userId = checkNotNull(userId);
        this.timestamp = timestamp;
        this.changeCount = changeCount;
        this.description = checkNotNull(description);
    }

    @Nonnull
    public RevisionNumber getRevisionNumber() {
        return revisionNumber;
    }

    @Nonnull
    public UserId getUserId() {
        return userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getChangeCount() {
        return changeCount;
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(revisionNumber, userId, timestamp, changeCount, description);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof RevisionSummary)) {
            return false;
        }
        RevisionSummary other = (RevisionSummary) obj;
        return this.timestamp == other.timestamp && this.revisionNumber.equals(other.revisionNumber) && this.userId.equals(other.userId) && this.changeCount == other.changeCount && this.description.equals(other.description);
    }

    /**
     * Compares this RevisionSummary with another RevisionSummary.  The comparison if based on the RevisionNumber,
     * the UserId, change count and the timestamp of each revision (in that order).
     * @param o The other revision to compare to.  Not <code>null</code>.
     * @return See {@link Comparable#compareTo(Object)}.
     */
    public int compareTo(@Nonnull RevisionSummary o) {
        int revNumDiff =  this.revisionNumber.compareTo(o.revisionNumber);
        if(revNumDiff != 0) {
            return revNumDiff;
        }
        int userIdDiff = this.userId.compareTo(o.userId);
        if(userIdDiff != 0) {
            return userIdDiff;
        }
        int changeCountDiff = this.changeCount - o.changeCount;
        if(changeCountDiff != 0) {
            return changeCountDiff;
        }
        if(this.timestamp < o.timestamp) {
            return -1;
        }
        else if(this.timestamp == o.timestamp) {
            return 0;
        }
        else {
            return 1;
        }
    }


    @Override
    public String toString() {
        return toStringHelper("RevisionSummary")
                .addValue(revisionNumber)
                .addValue(userId)
                .add("timestamp", timestamp)
                .add("changeCount", changeCount)
                .add("description", description)
                .toString();
    }
}
