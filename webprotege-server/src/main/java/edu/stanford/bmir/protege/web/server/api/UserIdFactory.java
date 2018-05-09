package edu.stanford.bmir.protege.web.server.api;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.glassfish.hk2.api.Factory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.api.AuthenticationConstants.AUTHENTICATED_USER_ID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Apr 2018
 *
 * A factory for creating UserId objects from the user id that is set by the
 * {@link AuthenticationFilter}.  This works in tandem with {@link AuthenticationFilter}.
 */
public class UserIdFactory implements Factory<UserId> {

    @Context
    private HttpServletRequest request;

    @Inject
    public UserIdFactory(@Nonnull HttpServletRequest request) {
        this.request = checkNotNull(request);
    }

    @Override
    public UserId provide() {
        // Provide the user id from the AUTHENTICATED_USER_ID attribute that should have
        // been set by the AuthenticationFilter.
        UserId userId = (UserId) request.getAttribute(AUTHENTICATED_USER_ID);
        if(userId == null) {
            throw new IllegalStateException(AUTHENTICATED_USER_ID + " attribute is not set");
        }
        return userId;
    }

    @Override
    public void dispose(UserId userId) {

    }
}
