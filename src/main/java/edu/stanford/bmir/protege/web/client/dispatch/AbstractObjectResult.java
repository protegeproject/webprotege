package edu.stanford.bmir.protege.web.client.dispatch;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public abstract class AbstractObjectResult<T extends Serializable> implements Result {

    private T object;

    /**
     * For serialization only
     */
    protected AbstractObjectResult() {
    }

    public AbstractObjectResult(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }
}
