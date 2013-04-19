package edu.stanford.bmir.protege.web.client.rpc;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class EmptySuccessWebProtegeCallback<T> extends AbstractWebProtegeAsyncCallback<T> {

    @Override
    public void onSuccess(T result) {
    }
}
