package edu.stanford.bmir.protege.web.client.rpc;

public interface AsyncHandler<X> {
	
	void handleSuccess(X result);
	
	void handleFailure(Throwable caught);
	
}
