package edu.stanford.bmir.protege.web.client.rpc.data;

import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/02/2012
 * <p>
 *     Represents a SharingSetting for a particular UserId.
 * </p>
 */
public class UserSharingSetting implements Comparable<UserSharingSetting>, Serializable {

    private UserId userId;
    
    private SharingSetting sharingSetting;

    private UserSharingSetting() {
    }

    /**
     * Constructs a UserSharingSetting which specifies the SharingSetting for a particular UserId.
     * @param userId The UserId which the SharingSetting pertains to.  Must not be <code>null</code>.
     * @param sharingSetting The SharingSetting which pertains the the UserId.  Must not be <code>null</code>.
     * @throws NullPointerException if the userId parameter is <code>null</code> or the sharingSetting parameter
     * is <code>null</code>.
     */
    public UserSharingSetting(UserId userId, SharingSetting sharingSetting) {
        if(userId == null) {
            throw new NullPointerException("The userId parameter must not be null.");
        }
        if(sharingSetting == null) {
            throw new NullPointerException("The sharingSetting parameter must not be null.");
        }
        this.userId = userId;
        this.sharingSetting = sharingSetting;
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
    public SharingSetting getSharingSetting() {
        return sharingSetting;
    }

    public int compareTo(UserSharingSetting o) {
        return userId.compareTo(o.getUserId());
    }
}
