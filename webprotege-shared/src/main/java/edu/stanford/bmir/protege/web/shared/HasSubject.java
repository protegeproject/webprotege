package edu.stanford.bmir.protege.web.shared;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 * <p>
 *     An interface to objects which have a subject.
 * </p>
 */
public interface HasSubject<T> {

    /**
     * Gets the subject of this object.
     * @return The subject.  Not {@code null}.
     */
    T getSubject();
}
