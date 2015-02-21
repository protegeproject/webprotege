package edu.stanford.bmir.protege.web.client.rpc;


import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class EmptySuccessWebProtegeCallback<T> extends DispatchServiceCallback<T> {

    @Override
    public void handleSuccess(T result) {
    }
}
