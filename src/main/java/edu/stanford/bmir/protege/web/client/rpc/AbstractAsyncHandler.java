package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractAsyncHandler<X> implements AsyncCallback<X> {

	public abstract void onFailure(Throwable caught);

	public abstract void onSuccess(X result);

}
