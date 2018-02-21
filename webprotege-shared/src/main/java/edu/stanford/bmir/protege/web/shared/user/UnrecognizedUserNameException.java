package edu.stanford.bmir.protege.web.shared.user;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/05/2013
 */
public class UnrecognizedUserNameException extends RuntimeException implements Serializable {

    private String userName;

    private UnrecognizedUserNameException() {
    }

    /**
     * Constructs an {@link UnrecognizedUserNameException}.
     * @param userName The invalid user name.  Not {@code null}.
     * @throws NullPointerException if {@code userName} is {@code null}.
     */
    public UnrecognizedUserNameException(String userName) {
        this.userName = checkNotNull(userName);
    }

    public String getUserName() {
        return userName;
    }
}
