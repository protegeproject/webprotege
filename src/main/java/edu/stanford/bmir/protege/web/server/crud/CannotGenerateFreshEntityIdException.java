package edu.stanford.bmir.protege.web.server.crud;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 10/09/2014
 */
public class CannotGenerateFreshEntityIdException extends RuntimeException {

    public CannotGenerateFreshEntityIdException() {
    }

    public CannotGenerateFreshEntityIdException(String message) {
        super(message);
    }

    public CannotGenerateFreshEntityIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotGenerateFreshEntityIdException(Throwable cause) {
        super(cause);
    }

    public CannotGenerateFreshEntityIdException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
