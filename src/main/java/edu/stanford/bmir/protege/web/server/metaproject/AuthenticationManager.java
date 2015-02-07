package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserRegistrationException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface AuthenticationManager {

    UserData registerUser(String userName, String email, String password) throws UserRegistrationException;

    void changePassword(String userName, String password);

    void setDigestedPassword(UserId userId, String encryptedPassword, String salt);
}
