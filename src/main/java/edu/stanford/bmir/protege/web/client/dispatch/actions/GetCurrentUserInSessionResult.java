package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2013
 */
public class GetCurrentUserInSessionResult implements Result {

    private UserInSession userInSession;

    /**
     * For serialization only
     */
    private GetCurrentUserInSessionResult() {
    }

    public GetCurrentUserInSessionResult(UserInSession userInSession) {
        this.userInSession = checkNotNull(userInSession);
    }

    public UserInSession getUserInSession() {
        return userInSession;
    }

}
