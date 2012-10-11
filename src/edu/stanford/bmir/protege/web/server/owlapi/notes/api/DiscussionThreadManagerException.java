package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
public class DiscussionThreadManagerException extends RuntimeException {

    public DiscussionThreadManagerException() {
    }

    public DiscussionThreadManagerException(String message) {
        super(message);
    }

    public DiscussionThreadManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DiscussionThreadManagerException(Throwable cause) {
        super(cause);
    }
}
