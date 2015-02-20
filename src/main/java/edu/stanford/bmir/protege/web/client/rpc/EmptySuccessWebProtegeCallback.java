package edu.stanford.bmir.protege.web.client.rpc;


import edu.stanford.bmir.protege.web.client.dispatch.AbstractDispatchServiceCallback;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class EmptySuccessWebProtegeCallback<T> extends AbstractDispatchServiceCallback<T> {

    @Override
    public void handleSuccess(T result) {
    }
}
