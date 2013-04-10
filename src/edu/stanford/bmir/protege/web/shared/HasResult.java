package edu.stanford.bmir.protege.web.shared;

import com.google.common.base.Optional;

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
    T getResult();

}
