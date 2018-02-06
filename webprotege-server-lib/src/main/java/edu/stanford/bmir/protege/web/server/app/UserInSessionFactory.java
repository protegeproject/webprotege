package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ApplicationResource;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.access.Subject.forUser;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22 Dec 2017
 */
public class UserInSessionFactory {

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final UserDetailsManager userDetailsManager;

    @Inject
    public UserInSessionFactory(@Nonnull AccessManager accessManager,
                                @Nonnull UserDetailsManager userDetailsManager) {
        this.accessManager = checkNotNull(accessManager);
        this.userDetailsManager = checkNotNull(userDetailsManager);
    }

    /**
     * Gets the user in session for the specified user id.
     * @param userId The user id.  This can be the id of the guest user.
     */
    @Nonnull
    public UserInSession getUserInSession(@Nonnull UserId userId) {
        UserDetails userDetails = userDetailsManager.getUserDetails(userId)
                                                    .orElse(UserDetails.getGuestUserDetails());
        Set<ActionId> actionClosure = accessManager.getActionClosure(forUser(userId),
                                                                     ApplicationResource.get());
        return new UserInSession(userDetails, actionClosure);
    }
}
