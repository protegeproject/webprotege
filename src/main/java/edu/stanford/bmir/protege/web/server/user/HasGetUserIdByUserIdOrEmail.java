package edu.stanford.bmir.protege.web.server.user;

import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 08/05/15
 */
public interface HasGetUserIdByUserIdOrEmail {
    /**
     * Gets a User by its user id or it's email address.
     * @param userNameOrEmail The user id or email address as a string.  Not {@code null}.
     * @return The User.  An absent value will be returned if there is not such user with the specified id or email
     * address. Not {@code null}.
     */
    Optional<UserId> getUserByUserIdOrEmail(String userNameOrEmail);
}
