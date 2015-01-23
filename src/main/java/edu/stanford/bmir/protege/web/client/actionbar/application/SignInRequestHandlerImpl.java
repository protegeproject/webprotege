package edu.stanford.bmir.protege.web.client.actionbar.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Random;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.client.ui.login.LoginUtil;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class SignInRequestHandlerImpl implements SignInRequestHandler {

    @Override
    public void handleSignInRequest() {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess() {
                final LoginUtil loginUtil = new LoginUtil();
                UserId userId = Application.get().getUserId();
                if (userId.isGuest()) {
                    Boolean isLoginWithHttps = Application.get().getClientApplicationProperty(WebProtegePropertyName.HTTPS_ENABLED, false);
                    if (isLoginWithHttps) {
                        String httpsPort = Application.get().getClientApplicationProperty(WebProtegePropertyName.HTTPS_PORT).orNull();
                        String authenUrl = loginUtil.getAuthenticateWindowUrl(AuthenticationConstants.AUTHEN_TYPE_LOGIN, httpsPort);
                        authenUrl = authenUrl + "&" + AuthenticationConstants.PROTOCOL + "=" + com.google.gwt.user.client.Window.Location.getProtocol();
                        authenUrl = authenUrl + "&" + AuthenticationConstants.DOMAIN_NAME_AND_PORT + "=" + com.google.gwt.user.client.Window.Location.getHost();
                        int randomNumber = Random.nextInt(10000);
                        authenUrl = authenUrl + "&" + AuthenticationConstants.RANDOM_NUMBER + "=" + randomNumber;
                        AdminServiceManager.getInstance().clearPreviousLoginAuthenticationData(new ClearLoginAuthDataHandler(authenUrl, loginUtil, randomNumber));
                    }
                    else {
                        loginUtil.login(isLoginWithHttps);
                    }
                }
                else {
                    GWT.log("User is already signed in");
                }
            }
        });
    }


    class ClearLoginAuthDataHandler extends AbstractAsyncHandler<Void> {

        private final String athnUrl;

        private final LoginUtil loginUtil;

        private final int randomNumber;

        public ClearLoginAuthDataHandler(String athnUrl, LoginUtil loginUtil, int randomNumber) {
            this.athnUrl = athnUrl;
            this.loginUtil = loginUtil;
            this.randomNumber = randomNumber;
        }

        @Override
        public void onFailure(Throwable caught) {
            MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
        }

        @Override
        public void onSuccess(Void result) {
            loginUtil.openNewWindow(athnUrl, "390", "325", "0");
            loginUtil.getTimeoutAndCheckUserLoggedInMethod(loginUtil, "" + randomNumber);
        }

    }
}
