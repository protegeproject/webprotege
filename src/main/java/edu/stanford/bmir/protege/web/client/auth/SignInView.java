package edu.stanford.bmir.protege.web.client.auth;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
public interface SignInView extends IsWidget, HasInitialFocusable {

    /**
     * Clears the user name and password.
     */
    void clear();

    /**
     * Gets the user name.
     * @return The user name.  Not {@code null}.  May be empty.
     */
    String getUserName();

    /**
     * Sets the user name.
     * @param userName The user name. Not {@code null}.
     * @throws java.lang.NullPointerException if {@code userName} is {@code null}.
     */
    void setUserName(String userName);

    /**
     * Gets the password.
     * @return The password.  Not {@code null}.  May be empty.
     */
    String getPassword();

    /**
     * Sets the ForgotPasswordHandler.
     * @param handler The handler.  Not {@code null}.
     */
    void setForgotPasswordHandler(ForgotPasswordHandler handler);


}
