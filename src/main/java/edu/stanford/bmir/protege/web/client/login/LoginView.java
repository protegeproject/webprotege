package edu.stanford.bmir.protege.web.client.login;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 *
 * A view that displays a login page.  The page has an area to enter a user name and
 * password.
 */
public interface LoginView extends IsWidget, RequiresResize, ProvidesResize {

    @Nonnull
    String getUserName();

    void setUserName(@Nonnull String userName);

    /**
     * Gets the password as typed into the view.
     * @return The password in clear text.
     */
    @Nonnull
    String getPassword();

    /**
     * Sets the displayed password.
     * @param password The password.
     */
    void setPassword(@Nonnull String password);

    void clearView();

    void showLoginFailedErrorMessage();

    void showUserNameRequiredErrorMessage();

    void showPasswordRequiredErrorMessage();

    void hideErrorMessages();

    void setSignInHandler(@Nonnull SignInRequestHandler handler);

    void setForgotPasswordHandler(@Nonnull ForgotPasswordHandler handler);

    void setSignUpForAccountHandler(@Nonnull SignUpForAccountHandler handler);
}
