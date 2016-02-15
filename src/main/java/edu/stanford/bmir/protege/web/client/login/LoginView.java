package edu.stanford.bmir.protege.web.client.login;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import edu.stanford.bmir.protege.web.client.actionbar.application.SignInRequestHandler;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public interface LoginView extends IsWidget, RequiresResize, ProvidesResize {

    String getUserName();

    void setUserName(String userName);

    String getPassword();

    void setPassword(String password);

    void clearView();

    void setSignInHandler(SignInRequestHandler handler);

    void showLoginFailedErrorMessage();

    void showUserNameRequiredErrorMessage();

    void showPasswordRequiredErrorMessage();

    void hideErrorMessages();

    void setForgotPasswordHandler(ForgotPasswordHandler handler);

}
