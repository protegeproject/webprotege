package edu.stanford.bmir.protege.web.shared.user;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/06/2012
 */
public class UserEmailAlreadyExistsException extends UserRegistrationException implements Serializable {

    private String emailAddress;

    private UserEmailAlreadyExistsException() {
    }

    public UserEmailAlreadyExistsException(String emailAddress) {
        super("User email address already exists: " + emailAddress);
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
