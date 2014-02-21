package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RecaptchaServiceAsync {

    void isSuccessful(String challenge, String response, AsyncCallback<Boolean> async);
}
