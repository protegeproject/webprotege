package edu.stanford.bmir.protege.web.shared.revision;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 * <p>
 *     Represents a project revision number.  There is the notion of the "head" revision.  This does not represet a fixed
 *     revision, but the latest possible revision of a project (see the {@link #getHeadRevisionNumber()} for details).
 * </p>
 */
public class RevisionNumber implements Serializable, IsSerializable, Comparable<RevisionNumber> {

    
    private static final long HEAD = Long.MAX_VALUE;

    private static final RevisionNumber HEAD_REVISION_NUMBER = new RevisionNumber(HEAD);

    private long value;

    /**
     * Default constructor for serialization purposes only.
     */
    private RevisionNumber() {
    }

    /**
     * Constructs a revision with the specified value.
     * @param value The value of the revision.
     */
    private RevisionNumber(long value) {
        this.value = value;
    }

    /**
     * Get the revision number that denotes the head revision (i.e. the latest possible revision).
     * @return The head RevisionNumber.  Not <code>null</code>.
     */
    public static RevisionNumber getHeadRevisionNumber() {
        return HEAD_REVISION_NUMBER;
    }

    /**
     * Gets the revision number for the specified long revision number value.
     * @param revisionNumber The revision number to get.
     * @return The RevisionNumber that has the specified long value for the revision number.  Not <code>null</code>.
     */
    public static RevisionNumber getRevisionNumber(long revisionNumber) {
        if(revisionNumber == HEAD) {
            return HEAD_REVISION_NUMBER;
        }
        else {
            return new RevisionNumber(revisionNumber);
        }
    }

    /**
     * Gets the long value of the this revision number.
     * @return The long value of this revision number
     */
    public long getValue() {
        return value;
    }
    public int getValueAsInt() {
        if(value == HEAD) {
            return Integer.MAX_VALUE;
        }
        else {
            return (int) value;
        }
    }
    

    /**
     * Determines if this revision number means the head revision (i.e. the latest possible revision).
     * @return <code>true</code> if this revision number should mean the head revision, otherwise <code>false</code>.
     */
    public boolean isHead() {
        return value == HEAD;
    }

    public RevisionNumber getNextRevisionNumber() {
        return RevisionNumber.getRevisionNumber(value + 1);
    }

    @Override
    public int hashCode() {
        return "RevisionNumber".hashCode() + (int) value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof RevisionNumber)) {
            return false;
        }
        RevisionNumber other = (RevisionNumber) obj;
        return this.value == other.value;
    }

    /**
     * Compares this RevisionNumber to another RevisionNumber.  The comparison is based on the long value of each
     * RevisionNumber i.e. the value returned by the {@link #getValue()} method.
     * @param o The revision number.  Not <code>null</code>
     * @return -1 if this revision comes before <code>o</code>, 0 if this revision is the same as <code>o</code>,
     * and +1 if this revision comes after <code>o</code>.
     */
    public int compareTo(RevisionNumber o) {
        if(this.value < o.value) {
            return -1;
        }
        else if(this.value > o.value) {
            return 1;
        }
        else {
            return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RevisionNumber(");
        if(isHead()) {
            sb.append("HEAD");
        }
        else {
            sb.append("REV=");
            sb.append(value);
        }
        sb.append(")");
        return sb.toString();
    }
}
