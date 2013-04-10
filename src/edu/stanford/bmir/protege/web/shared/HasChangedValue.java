package edu.stanford.bmir.protege.web.shared;

import com.google.common.base.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public interface HasChangedValue<T> {

    Optional<T> getOldValue();

    Optional<T> getNewValue();
}
