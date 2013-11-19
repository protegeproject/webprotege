package edu.stanford.bmir.protege.web.shared;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 * <p>
 *     An interface to object which have (or can generate) a UI description for display to end users.
 * </p>
 */
public interface HasUIDescription {

    /**
     * Gets the UI description for this object.
     * @return A {@link String} representing the UI description of this object.  Not {@code null}.
     */
    String getUIDescription();
}
