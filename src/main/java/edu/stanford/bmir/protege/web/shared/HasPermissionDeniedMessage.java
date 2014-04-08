package edu.stanford.bmir.protege.web.shared;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public interface HasPermissionDeniedMessage {

    /**
     * Gets the permission denied message.
     * @return A {@link String} representing the permission denied message.  Not {@code null}.
     */
    String getPermissionDeniedMessage();
}
