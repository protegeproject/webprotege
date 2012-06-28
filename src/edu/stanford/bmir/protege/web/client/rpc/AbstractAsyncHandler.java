package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractAsyncHandler<X> implements AsyncHandler<X>, AsyncCallback<X> {

	public void onSuccess(X result) {
		handleSuccess(result);
	}

	public void onFailure(Throwable caught) {
		handleFailure(caught);
	}

	public abstract void handleFailure(Throwable caught);

	public abstract void handleSuccess(X result);

}
