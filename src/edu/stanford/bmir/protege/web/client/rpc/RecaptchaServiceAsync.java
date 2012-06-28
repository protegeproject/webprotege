package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

public interface RecaptchaServiceAsync {

    void isSuccessful(String challenge, String response, AsyncCallback<Boolean> async);
}
