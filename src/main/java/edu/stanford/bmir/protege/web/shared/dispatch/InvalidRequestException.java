package edu.stanford.bmir.protege.web.shared.dispatch;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class InvalidRequestException extends RuntimeException {


    /**
     * For serialization purposes only
     */
    private InvalidRequestException() {
    }

    public InvalidRequestException(String message) {
        super(checkNotNull(message));
    }



}
