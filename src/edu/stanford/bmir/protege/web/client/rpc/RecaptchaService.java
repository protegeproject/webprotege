package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/06/2012
 */
@RemoteServiceRelativePath("recaptchaservice")
public interface RecaptchaService extends RemoteService {

    public boolean isSuccessful(String challenge, String response);
}
