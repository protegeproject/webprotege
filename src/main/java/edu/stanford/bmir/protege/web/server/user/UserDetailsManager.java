package edu.stanford.bmir.protege.web.server.user;

import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface UserDetailsManager extends HasGetUserIdByEmailAddress, HasGetUserIdByUserIdOrEmail {

    List<UserId> getUserIdsContainingIgnoreCase(String userName, int limit);

    /**
     * Gets the {@link UserDetails} for the specified UserId.
     * @param userId The UserId of the user whose details are to be retrieved.
     * @return If the userId is the guest user then the guest user details will be returned, otherwise,
     * the details of the specified userId.  If the userId does not exists then an empty value will
     * be returned.
     */
    Optional<UserDetails> getUserDetails(UserId userId);

    Optional<String> getEmail(UserId userId);

    void setEmail(UserId userId, String email);


}
