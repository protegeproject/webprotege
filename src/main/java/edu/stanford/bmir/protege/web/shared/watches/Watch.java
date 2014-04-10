package edu.stanford.bmir.protege.web.shared.watches;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public interface Watch<T> extends Serializable {

    T getWatchedObject();
}
