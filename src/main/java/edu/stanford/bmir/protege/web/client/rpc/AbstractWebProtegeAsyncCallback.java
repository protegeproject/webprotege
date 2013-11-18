package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/12/2012
 */
public abstract class AbstractWebProtegeAsyncCallback<T> implements AsyncCallback<T> {

    /**
     * Called when an asynchronous call fails to complete normally.
     * {@link com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException}s, {@link
     * com.google.gwt.user.client.rpc.InvocationException}s,
     * or checked exceptions thrown by the service method are examples of the type
     * of failures that can be passed to this method.
     *
     * <p>
     * If <code>caught</code> is an instance of an
     * {@link com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException} the application should try to
     * get into a state where a browser refresh can be safely done.
     * </p>
     * @param caught failure encountered while executing a remote procedure call
     */
    @Override
    public void onFailure(Throwable caught) {
        GWT.log(caught.getMessage(), caught);
    }
}
