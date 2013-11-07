package edu.stanford.bmir.protege.web.client.actionbar.application;

import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.chgpwd.ChangePasswordPresenter;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class ChangePasswordHandlerImpl implements ChangePasswordHandler {

    // Old Optra implementation.  Needs replacing.

    @Override
    public void handleChangePassword() {
        ChangePasswordPresenter changePasswordPresenter = new ChangePasswordPresenter(Application.get().getUserId());
        changePasswordPresenter.changePassword();

//        final LoginUtil loginUtil = new LoginUtil();
//        Boolean isLoginWithHttps = Application.get().getClientApplicationProperty(WebProtegePropertyName.HTTPS_ENABLED, false);
//        if (isLoginWithHttps) {
//            changePasswordWithHttps(loginUtil);
//        }
//        else {
//            loginUtil.changePassword(Application.get().getUserId(), isLoginWithHttps);
//        }
    }

//    private void changePasswordWithHttps(final LoginUtil loginUtil) {
//        String httsPort = Application.get().getClientApplicationProperty(WebProtegePropertyName.HTTPS_PORT).orNull();
//        Cookies.removeCookie(AuthenticationConstants.CHANGE_PASSWORD_RESULT);
//        notifyIfPasswordChanged();
//        String authUrl = loginUtil.getAuthenticateWindowUrl(AuthenticationConstants.AUTHEN_TYPE_CHANGE_PASSWORD, httsPort);
//        authUrl = authUrl + "&" + AuthenticationConstants.USERNAME + "=" + Application.get().getUserId().getUserName();
//        loginUtil.openNewWindow(authUrl, "440", "260", "0");
//    }

//    protected void notifyIfPasswordChanged() {
//        final Integer timeout = 5; // minutes
//        final long initTime = System.currentTimeMillis();
//        final Timer checkSessionTimer = new Timer() {
//            @Override
//            public void run() {
//                final Timer timer = this;
//                long curTime = System.currentTimeMillis();
//                long maxTime = 1000 * 60 * timeout;
//                if (curTime - initTime > maxTime) {
//                    timer.cancel();
//                }
//                String passwordChangedCookie = Cookies.getCookie(AuthenticationConstants.CHANGE_PASSWORD_RESULT);
//                if (passwordChangedCookie != null) {
//                    timer.cancel();
//
//                    if (passwordChangedCookie.equalsIgnoreCase(AuthenticationConstants.CHANGE_PASSWORD_SUCCESS)) {
//                        MessageBox.alert("Password Changed successfully");
//                    }
//                    Cookies.removeCookie(AuthenticationConstants.CHANGE_PASSWORD_RESULT);
//                }
//            }
//        };
//        checkSessionTimer.scheduleRepeating(2000);
//    }
}
