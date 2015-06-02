package edu.stanford.bmir.protege.web.shared.revision;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.io.Serializable;

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
    

    /**
     * Default constructor for serialization purposes only.
     */
    private RevisionSummary() {
    }

    public RevisionSummary(RevisionNumber revisionNumber, UserId usedId, long timestamp, int changeCount) {
        this.revisionNumber = checkNotNull(revisionNumber);
        this.userId = checkNotNull(usedId);
        this.timestamp = timestamp;
        this.changeCount = changeCount;
    }

    public RevisionNumber getRevisionNumber() {
        return revisionNumber;
    }

    public UserId getUserId() {
        return userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getChangeCount() {
        return changeCount;
    }

    @Override
    public int hashCode() {
        return "RevisionSummary".hashCode() + revisionNumber.hashCode() + userId.hashCode() + ((int) timestamp) + changeCount;
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
        return this.timestamp == other.timestamp && this.revisionNumber.equals(other.revisionNumber) && this.userId.equals(other.userId) && this.changeCount == other.changeCount;
    }

    /**
     * Compares this RevisionSummary with another RevisionSummary.  The comparison if based on the RevisionNumber,
     * the UserId, change count and the timestamp of each revision (in that order).
     * @param o The other revision to compare to.  Not <code>null</code>.
     * @return See {@link Comparable#compareTo(Object)}.
     */
    public int compareTo(RevisionSummary o) {
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
        StringBuilder sb = new StringBuilder();
        sb.append("RevisionSummary(");
        sb.append(revisionNumber);
        sb.append(" ");
        sb.append("ChangeCount(");
        sb.append(changeCount);
        sb.append(") ");
        sb.append(userId);
        sb.append(" ");
        sb.append("Timestamp(");
        sb.append(timestamp);
        sb.append(")");
        sb.append(")");
        return sb.toString();
    }
}
