package edu.stanford.bmir.protege.web.shared.sharing;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

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

    private PersonId personId;
    
    private SharingPermission sharingPermission;

    private SharingSetting() {
    }

    /**
     * Constructs a UserSharingSetting which specifies the SharingSetting for a particular person.  The person may, or
     * may not be a registered WebProtege user.
     * @param personId The PersonId which the SharingSetting pertains to.  Must not be <code>null</code>.
     * @param sharingPermission The SharingPermission which pertains the the UserId.  Must not be <code>null</code>.
     * @throws NullPointerException if the userId parameter is <code>null</code> or the sharingPermission parameter
     * is <code>null</code>.
     */
    public SharingSetting(PersonId personId, SharingPermission sharingPermission) {
        this.personId = checkNotNull(personId);
        this.sharingPermission = checkNotNull(sharingPermission);
    }

    /**
     * Gets the PersonId for this SharingSetting.
     * @return The PersonId. Not <code>null</code>.
     */
    public PersonId getPersonId() {
        return personId;
    }

    /**
     * Gets the SharingSetting for this particular UserSharingSetting.
     * @return The SharingSetting.  Not <code>null</code>.
     */
    public SharingPermission getSharingPermission() {
        return sharingPermission;
    }

    public int compareTo(SharingSetting o) {
        return personId.compareTo(o.personId);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("UserSharingSetting")
                          .addValue(personId)
                          .addValue(sharingPermission)
                          .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(personId, sharingPermission);
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
        return this.personId.equals(other.personId) && this.sharingPermission.equals(other.sharingPermission);
    }
}
