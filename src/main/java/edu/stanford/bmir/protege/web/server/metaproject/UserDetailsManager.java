package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.user.HasUserIds;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.User;

import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface UserDetailsManager extends HasUserIds {

    @Override
    Collection<UserId> getUserIds();

    /**
     * Gets a User by its user id or it's email address.
     * @param userNameOrEmail The user id or email address as a string.  Not {@code null}.
     * @return The User.  An absent value will be returned if there is not such user with the specified id or email
     * address. Not {@code null}.
     */
    Optional<User> getUserByUserIdOrEmail(String userNameOrEmail);

    UserDetails getUserDetails(UserId userId);

    Optional<String> getEmail(UserId userId);

    void setEmail(UserId userId, String email);


}
