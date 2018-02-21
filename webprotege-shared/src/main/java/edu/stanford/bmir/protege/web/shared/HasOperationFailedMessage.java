package edu.stanford.bmir.protege.web.shared;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 * <p>
 *     An interface to an object that can provide a message to indicate that some operation failed.
 * </p>
 */
public interface HasOperationFailedMessage {

    /**
     * Gets a message that describes the operation that failed.  This message should be written in a passive voice, for
     * example, "The class was not added as a subclass of the selected class", or "The property was not deleted", or
     * "The user permissions were not updated".
     * @return A {@link String} representing the message. Not {@code null}.
     */
    String getOperationFailedMessage();
}
