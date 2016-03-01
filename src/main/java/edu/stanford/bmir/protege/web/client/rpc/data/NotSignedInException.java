package edu.stanford.bmir.protege.web.client.rpc.data;

import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/05/2012
 */
public class NotSignedInException extends PermissionDeniedException {

    public NotSignedInException() {
        super("You are not signed in.  Please sign in.");
    }

    public NotSignedInException(String message) {
        super(message);
    }
}
