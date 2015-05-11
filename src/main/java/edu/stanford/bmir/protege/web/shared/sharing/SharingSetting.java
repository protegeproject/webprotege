package edu.stanford.bmir.protege.web.shared.sharing;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/02/2012
 * <p>
 *     Represents a SharingSetting for a particular UserId.
 * </p>
 */
public class SharingSetting implements Comparable<SharingSetting>, Serializable {

    private UserId userId;
    
    private SharingPermission sharingPermission;

    private SharingSetting() {
    }

    /**
     * Constructs a UserSharingSetting which specifies the SharingSetting for a particular UserId.
     * @param userId The UserId which the SharingSetting pertains to.  Must not be <code>null</code>.
     * @param sharingPermission The SharingPermission which pertains the the UserId.  Must not be <code>null</code>.
     * @throws NullPointerException if the userId parameter is <code>null</code> or the sharingPermission parameter
     * is <code>null</code>.
     */
    public SharingSetting(UserId userId, SharingPermission sharingPermission) {
        this.userId = checkNotNull(userId);
        this.sharingPermission = checkNotNull(sharingPermission);
    }

    /**
     * Gets the UserId for this UserSharingSetting.
     * @return The UserId. Not <code>null</code>.
     */
    public UserId getUserId() {
        return userId;
    }

    /**
     * Gets the SharingSetting for this particular UserSharingSetting.
     * @return The SharingSetting.  Not <code>null</code>.
     */
    public SharingPermission getSharingPermission() {
        return sharingPermission;
    }

    public int compareTo(SharingSetting o) {
        return userId.compareTo(o.getUserId());
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("UserSharingSetting")
                .addValue(userId)
                .addValue(sharingPermission)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId, sharingPermission);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SharingSetting)) {
            return false;
        }
        SharingSetting other = (SharingSetting) obj;
        return this.userId.equals(other.userId) && this.sharingPermission.equals(other.sharingPermission);
    }
}
