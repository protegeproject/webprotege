package edu.stanford.bmir.protege.web.server.session;

import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
public interface WebProtegeSession {

    /**
     * Gets the user in this session.
     * @return The {@link UserId} of the user in this session.
     * Not {@code null}.  If there is not a specific user in this session then the {@link UserId}
     * of the guest user will be returned.
     */
    UserId getUserInSession();

    /**
     *  Sets the guest user as the user in this session.
     */
    void clearUserInSession();

}
