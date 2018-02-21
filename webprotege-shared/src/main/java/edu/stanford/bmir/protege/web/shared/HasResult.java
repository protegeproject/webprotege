package edu.stanford.bmir.protege.web.shared;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 * <p>
 *     An interface to objects that can produce a result.
 * </p>
 */
public interface HasResult<T> {

    /**
     * Gets the result.
     * @return The result.  Not {@code null}.
     */
    @Nonnull
    T getResult();

}
