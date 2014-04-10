package edu.stanford.bmir.protege.web.client.rpc.data;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/05/2012
 */
public class NotSignedInException extends RuntimeException {

    public NotSignedInException() {
        super("Not signed in");
    }

    public NotSignedInException(String message) {
        super(message);
    }
}
