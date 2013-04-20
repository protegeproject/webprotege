package edu.stanford.bmir.protege.web.shared.user;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/02/2012
 * <p>
 *     Instances of this class are used to refer to a particular user.  Each user has a user name.
 * </p>
 */
public class UserId implements Serializable, IsSerializable, Comparable<UserId> {

    /**
     * The distinguished name for the guest user.
     */
    private static final String GUEST_USER_NAME = "guest";

    /**
     * The distinguished {@link UserId} for the guest user
     */
    private static final UserId GUEST_USER_ID = new UserId(GUEST_USER_NAME);

    private String userName;

    /**
     * For serialization purposes only.
     */
    private UserId() {
        userName = GUEST_USER_NAME;
    }

    /**
     * Determines if this {@link UserId} represents the guest (non-logged in) user.
     * @return {@code true} if this {@link UserId} represents the guest user. {@code false} if this {@link UserId}
     * does not represent the guest user.
     */
    public boolean isGuest() {
        return GUEST_USER_NAME.endsWith(userName);
    }

    /**
     * Gets a UserId using the userName as the identifier.
     * @param userName The userName.  May be {@code null}.
     * @return A UserId with the specified user name.  If userName is {@code null} then the distinguished UserId
     * that represents the null user (anyone) will be returned.
     */
    public static UserId getUserId(String userName) {
        if(userName == null) {
            return GUEST_USER_ID;
        }
        else {
            return new UserId(userName);
        }
    }

    /**
     * Gets the id of the guest user.  The guest user is a distinguished id which indicates a non-logged in user.
     * @return The {@link UserId} of the guest user.  Not {@code null}.
     */
    public static UserId getGuest() {
        return GUEST_USER_ID;
    }


    /**
     * Constructs a UserId using the userName as the identifier.
     * @param userName The userName.  May be {@code null}.
     */
    private UserId(String userName) {
        if(userName == null) {
            this.userName = GUEST_USER_NAME;
        }
        else {
            this.userName = userName;    
        }
        
    }


    /**
     * Gets the user name of the user that this id represents.
     * @return A string representing the user name.  Not {@code null}.
     */
    public String getUserName() {
        return userName;
    }



    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof UserId)) {
            return false;
        }
        UserId other = (UserId) obj;
        return other.userName.equals(this.userName);
    }

    @Override
    public int hashCode() {
        return userName.hashCode();
    }

    public int compareTo(UserId o) {
        return userName.compareTo(o.getUserName());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserId(");
        sb.append(userName);
        sb.append(")");
        return sb.toString();
    }
}
