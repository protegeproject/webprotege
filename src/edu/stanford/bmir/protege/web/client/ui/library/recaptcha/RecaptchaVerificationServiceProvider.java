package edu.stanford.bmir.protege.web.client.ui.library.recaptcha;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.RecaptchaService;
import edu.stanford.bmir.protege.web.client.rpc.RecaptchaServiceAsync;
import edu.stanford.bmir.protege.web.client.ui.verification.HumanVerificationHandler;
import edu.stanford.bmir.protege.web.client.ui.verification.HumanVerificationServiceProvider;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/07/2013
 */
public class RecaptchaVerificationServiceProvider implements HumanVerificationServiceProvider {

    private static final RecaptchaServiceAsync SERVICE = GWT.create(RecaptchaService.class);

    private static final String ERROR_MESSAGE = "The verification text entered is not correct.  Please try again.";

    private String challenge;

    private String response;

    public RecaptchaVerificationServiceProvider(String challenge, String response) {
        this.challenge = challenge;
        this.response = response;
    }

    @Override
    public void runVerification(final HumanVerificationHandler verificationHandler) {
        SERVICE.isSuccessful(challenge, response, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                verificationHandler.handleVerificationFailure(ERROR_MESSAGE);
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    verificationHandler.handleVerificationSuccess();
                }
                else {
                    verificationHandler.handleVerificationFailure(ERROR_MESSAGE);
                }
            }
        });
    }
}
