package edu.stanford.bmir.protege.web.shared.app;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.permissions.GroupId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/12/14
 *
 * Represents a user in the current session
 */
public class UserInSession implements IsSerializable {

    private UserDetails userDetails;

    private ImmutableList<GroupId> groups;

    /**
     * For serialization
     */
    private UserInSession() {
    }

    public UserInSession(UserDetails userDetails, ImmutableList<GroupId> groups) {
        this.userDetails = checkNotNull(userDetails);
        this.groups = checkNotNull(groups);
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public ImmutableList<GroupId> getGroups() {
        return groups;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userDetails, groups);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UserInSession)) {
            return false;
        }
        UserInSession other = (UserInSession) obj;
        return userDetails.equals(other.userDetails) && this.groups.equals(other.groups);
    }


    @Override
    public String toString() {
        return toStringHelper("UserInSession")
                .addValue(userDetails)
                .addValue(groups)
                .toString();
    }
}
