package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/06/2012
 */
public class UserNameAlreadyExistsException extends UserRegistrationException implements Serializable {

    private String username;

    private UserNameAlreadyExistsException() {
    }

    public UserNameAlreadyExistsException(String username) {
        super("User name already taken: " + username);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
