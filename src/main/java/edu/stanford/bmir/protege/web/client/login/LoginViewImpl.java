package edu.stanford.bmir.protege.web.client.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import javax.inject.Inject;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class LoginViewImpl extends Composite implements LoginView {

    interface LoginViewImplUiBinder extends UiBinder<HTMLPanel, LoginViewImpl> {

    }

    private static LoginViewImplUiBinder ourUiBinder = GWT.create(LoginViewImplUiBinder.class);

    @UiField
    protected TextBox userNameField;

    @UiField
    protected PasswordTextBox passwordField;

    @UiField
    protected Button signInButton;

    @UiField
    protected HasClickHandlers forgotPasswordButton;

    @UiField
    protected Button signUpForAccountButton;


    private SignInRequestHandler signInRequestHandler = () -> {};

    private ForgotPasswordHandler forgotPasswordHandler = () -> {};

    private SignUpForAccountHandler signUpForAccountHandler = () -> {};

    @Inject
    public LoginViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("signInButton")
    protected void handleSignInButtonClicked(ClickEvent event) {
        signInRequestHandler.handleSignInRequest();
    }

    @UiHandler("forgotPasswordButton")
    protected void handleForgotPasswordButtonClicked(ClickEvent event) {
        forgotPasswordHandler.handleForgotPassword();
    }

    @UiHandler("signUpForAccountButton")
    protected void handleSignUpForAccountButtonClicked(ClickEvent event) {
        signUpForAccountHandler.handleSignUpForAccount();
    }

    @UiHandler("passwordField")
    protected void handlePasswordFieldKeyPress(KeyPressEvent event) {
        handlePossibleSignInKeyPress(event);
    }

    @UiHandler("userNameField")
    protected void handleUserNameFieldKeyPress(KeyPressEvent event) {
        handlePossibleSignInKeyPress(event);
    }

    private void handlePossibleSignInKeyPress(KeyPressEvent event) {
        if(event.getCharCode() == KeyCodes.KEY_ENTER) {
            signInRequestHandler.handleSignInRequest();
        }
    }

    @Override
    public String getUserName() {
        return userNameField.getText().trim();
    }

    @Override
    public void setUserName(String userName) {
        userNameField.setText(userName);
    }

    @Override
    public String getPassword() {
        return passwordField.getText().trim();
    }

    @Override
    public void setPassword(String password) {
        passwordField.setText(password);
    }

    @Override
    public void clearView() {
        userNameField.setText("");
        passwordField.setText("");
        hideErrorMessages();
    }

    @Override
    public void setSignInHandler(SignInRequestHandler handler) {
        this.signInRequestHandler = handler;
    }

    @Override
    public void showLoginFailedErrorMessage() {
        MessageBox.showAlert("Unable to log in.  User name or password is incorrect.");
    }

    @Override
    public void showUserNameRequiredErrorMessage() {
        MessageBox.showAlert("Please enter your user name");
    }

    @Override
    public void showPasswordRequiredErrorMessage() {
        MessageBox.showAlert("Please enter your password");
    }

    @Override
    public void hideErrorMessages() {

    }

    @Override
    public void setForgotPasswordHandler(ForgotPasswordHandler handler) {
        this.forgotPasswordHandler = checkNotNull(handler);
    }

    @Override
    public void setSignUpForAccountHandler(SignUpForAccountHandler handler) {
        this.signUpForAccountHandler = handler;
    }

    @Override
    public void onResize() {

    }
}