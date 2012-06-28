package edu.stanford.bmir.protege.web.client.rpc.data;

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

    public static final String NULL_USER_NAME = "";

    public static final UserId NULL_USER_ID = new UserId(NULL_USER_NAME);

    private String userName;

    /**
     * For serialization purposes only.
     */
    private UserId() {
        userName = NULL_USER_NAME;
    }

    /**
     * Determines if this user id does not represent any user.
     * @return <code>true</code> if this user id does not represent a user, otherwise, <code>false</code>.
     */
    public boolean isNull() {
        return NULL_USER_NAME.equals(userName);
    }

    /**
     * Gets a UserId using the userName as the identifier.
     * @param userName The userName.  May be <code>null</code>.
     * @return A UserId with the specified user name.  If userName is <code>null</code> then the distinguished UserId
     * that represents the null user (anyone) will be returned.
     */
    public static UserId getUserId(String userName) {
        if(userName == null) {
            return NULL_USER_ID;
        }
        else {
            return new UserId(userName);
        }
    }

    public static UserId getNull() {
        return new UserId(NULL_USER_NAME);
    }

    /**
     * Constructs a UserId using the userName as the identifier.
     * @param userName The userName.  May be <code>null</code>.
     */
    private UserId(String userName) {
        if(userName == null) {
            this.userName = NULL_USER_NAME;
        }
        else {
            this.userName = userName;    
        }
        
    }


    /**
     * Gets the user name of the user that this id represents.
     * @return A string representing the user name.
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
