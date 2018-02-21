package edu.stanford.bmir.protege.web.client.signup;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/16
 */
public interface SignUpView extends IsWidget {

    void clear();

    void clearErrorMessages();

    String getUserName();

    void displayEnterUserNameErrorMessage();

    String getEmailAddress();

    void displayEnterEmailAddressErrorMessage();

    String getPassword();

    void displayEnterPasswordErrorMessage();

    String getConfirmPassword();

    void displayConfirmPasswordDoesNotMatchErrorMessage();

    void setCancelHandler(ClickHandler clickHandler);

    void setSignUpHandler(ClickHandler clickHandler);
}
