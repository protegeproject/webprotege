package edu.stanford.bmir.protege.web.shared.dispatch;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/03/2013
 */
public class DispatchServiceResultContainer<R extends Result> implements Serializable {

    private R result;

    private DispatchServiceResultContainer() {
    }

    public DispatchServiceResultContainer(R result) {
        this.result = result;
    }

    public R getResult() {
        return result;
    }
}
