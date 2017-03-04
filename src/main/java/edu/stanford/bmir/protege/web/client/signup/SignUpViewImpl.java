package edu.stanford.bmir.protege.web.client.signup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.text.PlaceholderTextBox;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/16
 */
public class SignUpViewImpl extends Composite implements SignUpView {

    interface SignUpViewImplUiBinder extends UiBinder<HTMLPanel, SignUpViewImpl> {

    }

    private static SignUpViewImplUiBinder ourUiBinder = GWT.create(SignUpViewImplUiBinder.class);

    @UiField
    PlaceholderTextBox userNameField;

    @UiField
    TextBox emailAddressField;

    @UiField
    PasswordTextBox passwordField;

    @UiField
    PasswordTextBox confirmPasswordField;

    @UiField
    Button cancelButton;

    @UiField
    Button signInButton;

    @UiField
    Label userNameErrorLabel;

    @UiField
    Label passwordErrorLabel;

    @UiField
    Label confirmPasswordErrorLabel;

    @UiField
    Label emailAddressErrorLabel;

    @Inject
    public SignUpViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clear() {
        userNameField.setText("");
        emailAddressField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        clearErrorMessages();
    }

    @Override
    public void clearErrorMessages() {
        displayErrorLabel(userNameErrorLabel, false);
        displayErrorLabel(emailAddressErrorLabel, false);
        displayErrorLabel(passwordErrorLabel, false);
        displayErrorLabel(confirmPasswordErrorLabel, false);
    }

    @Override
    public String getUserName() {
        return userNameField.getText().trim();
    }

    @Override
    public void displayEnterUserNameErrorMessage() {
        displayErrorLabel(userNameErrorLabel, true);
    }

    @Override
    public String getEmailAddress() {
        return emailAddressField.getText().trim();
    }

    @Override
    public void displayEnterEmailAddressErrorMessage() {
        displayErrorLabel(emailAddressErrorLabel, true);
    }

    @Override
    public String getPassword() {
        return passwordField.getText().trim();
    }

    @Override
    public void displayEnterPasswordErrorMessage() {
        displayErrorLabel(passwordErrorLabel, true);
    }

    @Override
    public String getConfirmPassword() {
        return confirmPasswordField.getText().trim();
    }

    @Override
    public void displayConfirmPasswordDoesNotMatchErrorMessage() {
        displayErrorLabel(confirmPasswordErrorLabel, true);
    }

    @Override
    public void setCancelHandler(ClickHandler clickHandler) {
        cancelButton.addClickHandler(clickHandler);
    }

    @Override
    public void setSignUpHandler(ClickHandler clickHandler) {
        signInButton.addClickHandler(clickHandler);
    }


    private void displayErrorLabel(IsWidget errorLabel, boolean display) {
        Style.Display disp;
        if(display) {
            disp = Style.Display.BLOCK;
        }
        else {
            disp = Style.Display.NONE;
        }
        errorLabel.asWidget().getElement().getStyle().setDisplay(disp);
    }
}