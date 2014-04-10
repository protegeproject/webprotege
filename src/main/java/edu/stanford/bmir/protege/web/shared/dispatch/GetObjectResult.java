package edu.stanford.bmir.protege.web.shared.dispatch;

import edu.stanford.bmir.protege.web.shared.HasObject;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class GetObjectResult<T> implements Result, HasObject<T>, Serializable {

    private T object;

    /**
     * For serialization purposes only.
     */
    protected GetObjectResult() {

    }

    public GetObjectResult(T object) {
        this.object = object;
    }

    /**
     * Gets the object.
     * @return The object.  Not {@code null}.
     */
    @Override
    public T getObject() {
        return object;
    }
}
