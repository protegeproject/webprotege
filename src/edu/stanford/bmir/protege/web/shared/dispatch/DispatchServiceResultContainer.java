package edu.stanford.bmir.protege.web.shared.dispatch;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/03/2013
 */
public class DispatchServiceResultContainer implements Serializable {

    private Result result;

    private DispatchServiceResultContainer() {
    }

    public DispatchServiceResultContainer(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }
}
