package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/09/15
 */
public interface HasGetUserIdByEmailAddress {

    /**
     * Retrieves a UserId by their email address.
     * @param emailAddress The email address.  Not {@code null}.
     * @return The UserId that has the specified email address.  An absent value will be returned if there is
     * no user with the specified email address.
     */
    Optional<UserId> getUserIdByEmailAddress(EmailAddress emailAddress);
}
