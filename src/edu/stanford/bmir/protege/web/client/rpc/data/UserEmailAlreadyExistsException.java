package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/06/2012
 */
public class UserEmailAlreadyExistsException extends UserRegistrationException implements Serializable {

    private String emailAddress;

    public UserEmailAlreadyExistsException(String emailAddress) {
        super("User email address already exists: " + emailAddress);
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
