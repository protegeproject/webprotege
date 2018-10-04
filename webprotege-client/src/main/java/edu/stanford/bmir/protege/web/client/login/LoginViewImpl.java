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
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class LoginViewImpl extends Composite implements LoginView {

    @Nonnull
    private final MessageBox messageBox;

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

    private static final Messages MESSAGES = GWT.create(Messages.class);

    @Inject
    public LoginViewImpl(@Nonnull MessageBox messageBox) {
        this.messageBox = checkNotNull(messageBox);
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

    @Nonnull
    @Override
    public String getUserName() {
        return userNameField.getText().trim();
    }

    @Override
    public void setUserName(@Nonnull String userName) {
        userNameField.setText(userName);
    }

    @Nonnull
    @Override
    public String getPassword() {
        return passwordField.getText().trim();
    }

    @Override
    public void setPassword(@Nonnull String password) {
        passwordField.setText(password);
    }

    @Override
    public void clearView() {
        userNameField.setText("");
        passwordField.setText("");
        hideErrorMessages();
    }

    @Override
    public void setSignInHandler(@Nonnull SignInRequestHandler handler) {
        this.signInRequestHandler = handler;
    }

    @Override
    public void showLoginFailedErrorMessage() {
        messageBox.showAlert(MESSAGES.login_error());
    }

    @Override
    public void showUserNameRequiredErrorMessage() {
        messageBox.showAlert(MESSAGES.login_enterUserName());
    }

    @Override
    public void showPasswordRequiredErrorMessage() {
        messageBox.showAlert(MESSAGES.login_enterPassword());
    }

    @Override
    public void hideErrorMessages() {

    }

    @Override
    public void setForgotPasswordHandler(@Nonnull ForgotPasswordHandler handler) {
        this.forgotPasswordHandler = checkNotNull(handler);
    }

    @Override
    public void setSignUpForAccountHandler(@Nonnull SignUpForAccountHandler handler) {
        this.signUpForAccountHandler = handler;
    }

    @Override
    public void setSignUpForAccountVisible(boolean visible) {
        signUpForAccountButton.setVisible(visible);
    }

    @Override
    public void onResize() {

    }
}