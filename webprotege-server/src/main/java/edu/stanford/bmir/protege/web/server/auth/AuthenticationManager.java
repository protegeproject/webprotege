package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserRegistrationException;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface AuthenticationManager {

    UserDetails registerUser(UserId userId, EmailAddress email, SaltedPasswordDigest password, Salt salt) throws UserRegistrationException;

    void setDigestedPassword(UserId userId, SaltedPasswordDigest saltedPasswordDigest, Salt salt);

    Optional<Salt> getSalt(UserId userId);

    Optional<SaltedPasswordDigest> getSaltedPasswordDigest(UserId userId);
}
