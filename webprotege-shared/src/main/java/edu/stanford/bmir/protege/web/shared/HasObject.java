package edu.stanford.bmir.protege.web.shared;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public interface HasObject<T> {

    /**
     * Gets the object.
     * @return The object.  Not {@code null}.
     */
    T getObject();
}
