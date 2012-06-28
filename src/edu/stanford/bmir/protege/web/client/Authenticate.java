package edu.stanford.bmir.protege.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.ApplicationPropertiesServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.AuthenticateServiceManager;
import edu.stanford.bmir.protege.web.client.ui.ClientApplicationPropertiesCache;
import edu.stanford.bmir.protege.web.client.ui.login.LoginUtil;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;

import java.util.Date;
import java.util.Map;

/**
 * @author z.khan
 * 
 */
public class Authenticate implements EntryPoint {

    public void onModuleLoad() {
        initServletMagagers();
        initializeClientApplicationPropertiesCache();
    }

    private void initializeClientApplicationPropertiesCache() {
        ClientApplicationPropertiesCache.initialize(new AsyncCallback<Map<String, String>>(){
            public void onFailure(Throwable caught) {
                checkAuthenType();
            }

            public void onSuccess(Map<String, String> result) {
                checkAuthenType();
            }
        });
    }

    private void checkAuthenType() {
        String authenType = Window.Location.getParameter(AuthenticationConstants.AUTHEN_TYPE);
        if (authenType.trim().equals(AuthenticationConstants.AUTHEN_TYPE_LOGIN)) {//To open login popup in https window
            final String randomNumber = Window.Location.getParameter(AuthenticationConstants.RANDOM_NUMBER);
            HandlerRegistration windowCloseHandlerRegistration = Window
                    .addWindowClosingHandler(new LoginWindowCloseHandler(randomNumber));
            LoginUtil loginUtil = new LoginUtil();
            loginUtil.setWindowCloseHandlerRegistration(windowCloseHandlerRegistration);
            loginUtil.login(true);
            setWindowSize("405", "408");

        } else if (authenType.trim().equals(AuthenticationConstants.AUTHEN_TYPE_CHANGE_PASSWORD)) {
            String userName = Window.Location.getParameter(AuthenticationConstants.USERNAME);
            Window.addWindowClosingHandler(new WindowCloseHandler(AuthenticationConstants.CHANGE_PASSWORD_RESULT,
                    AuthenticationConstants.CHANGE_PASSWORD_WINDOW_CLOSED));
            LoginUtil loginUtil = new LoginUtil();
            loginUtil.changePassword(userName, true);
        } else if (authenType.trim().equals(AuthenticationConstants.AUTHEN_TYPE_CREATE_USER_TO_ASSOC_OPEN_ID)) {
            Window.addWindowClosingHandler(new WindowCloseHandler(AuthenticationConstants.CREATE_USER_TO_ASSOC_OPENID_RESULT,
                    AuthenticationConstants.CREATE_USER_WINDOW_CLOSED));
            LoginUtil loginUtil = new LoginUtil();
            loginUtil.createNewUserToAssociateOpenId(true);
        } else if (authenType.trim().equals(AuthenticationConstants.AUTHEN_TYPE_LOGIN_TO_ASSOC_OPEN_ID)) {
            Window.addWindowClosingHandler(new WindowCloseHandler(AuthenticationConstants.AUTHEN_USER_TO_ASSOC_OPENID_RESULT,
                    AuthenticationConstants.AUTHEN_USER_WINDOW_CLOSED));
            LoginUtil loginUtil = new LoginUtil();
            loginUtil.loginToAssociateOpenId(true);
        } else if (authenType.trim().equals(AuthenticationConstants.AUTHEN_TYPE_CREATE_USER)) {
            LoginUtil loginUtil = new LoginUtil();
            loginUtil.createNewUser(true);
        }
    }

    protected void setWindowClosedCookie(String randomNumber) {
        Date expireDate = new Date();
        long nowLong = expireDate.getTime();
        nowLong = nowLong + (1000 * 60 * 30);//30 minutes  
        expireDate.setTime(nowLong);
        Cookies.setCookie(AuthenticationConstants.HTTPS_WINDOW_CLOSED_COOKIE + "." + randomNumber, "", expireDate);
    }

    /**
     * Force Servlet initialization - needed when running in browsers.
     */
    protected void initServletMagagers() {
        AuthenticateServiceManager.getInstance();
        ApplicationPropertiesServiceManager.getInstance();
    }

    native public void closeWindow()/*-{
        $wnd.close();
    }-*/;

    public static native void setWindowSize(String width, String height)/*-{
        $wnd.resizeTo(width, height);
    }-*/;

    protected class LoginWindowCloseHandler implements ClosingHandler {
        private final String randomNumber;

        protected LoginWindowCloseHandler(String randomNumber) {
            this.randomNumber = randomNumber;
        }

        public void onWindowClosing(ClosingEvent event) {
            setWindowClosedCookie(randomNumber);

        }
    }

    protected class WindowCloseHandler implements ClosingHandler {
        private final String cookieName;
        private final String cookieValue;

        protected WindowCloseHandler(String cookieName, String cookieValue) {
            this.cookieName = cookieName;
            this.cookieValue = cookieValue;
        }

        public void onWindowClosing(ClosingEvent event) {

            if (Cookies.getCookie(cookieName) == null) {
                Date expireDate = new Date();
                long nowLong = expireDate.getTime();
                nowLong = nowLong + (1000 * 60 * 5);//5 minutes  
                expireDate.setTime(nowLong);
                Cookies.setCookie(cookieName, cookieValue, expireDate);
            }

        }
    }
}
