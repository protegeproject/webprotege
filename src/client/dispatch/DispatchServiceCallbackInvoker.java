package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class DispatchServiceCallbackInvoker<T> implements AsyncCallback<T> {

    private DispatchServiceCallback<T> callback;

    public DispatchServiceCallbackInvoker(DispatchServiceCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void onFailure(Throwable throwable) {
        callback.onFailure(throwable);
    }

    @Override
    public void onSuccess(T t) {
        callback.onSuccess(t);
    }
}
