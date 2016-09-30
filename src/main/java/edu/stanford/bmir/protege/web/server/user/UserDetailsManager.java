package edu.stanford.bmir.protege.web.server.user;

import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface UserDetailsManager extends HasGetUserIdByEmailAddress, HasGetUserIdByUserIdOrEmail {

    List<UserId> getUserIdsContainingIgnoreCase(String userName, int limit);

    Optional<UserDetails> getUserDetails(UserId userId);

    Optional<String> getEmail(UserId userId);

    void setEmail(UserId userId, String email);


}
