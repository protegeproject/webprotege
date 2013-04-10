package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.permissions.GroupId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2013
 */
public class GetCurrentUserInSessionResult implements Result {

    private UserDetails userDetails;

    private Set<GroupId> userGroupIds;

    /**
     * For serialization only
     */
    private GetCurrentUserInSessionResult() {
    }

    public GetCurrentUserInSessionResult(UserDetails userDetails, Set<GroupId> userGroupIds) {
        this.userDetails = checkNotNull(userDetails);
        this.userGroupIds = checkNotNull(userGroupIds);
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public Set<GroupId> getUserGroupIds() {
        return userGroupIds;
    }
}
