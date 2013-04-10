package edu.stanford.bmir.protege.web.client.dispatch;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class VoidResult implements Result {

    private static final VoidResult VOID_RESULT = new VoidResult();

    protected VoidResult() {
    }

    public static VoidResult get() {
        return VOID_RESULT;
    }


}
