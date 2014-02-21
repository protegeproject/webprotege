package edu.stanford.bmir.protege.web.shared.dispatch;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/03/2013
 */
public class DispatchServiceResultContainer implements IsSerializable {

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
