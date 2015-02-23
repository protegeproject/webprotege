package edu.stanford.bmir.protege.web.client.auth;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
public class SignInViewImpl extends Composite implements SignInView {

    interface SignInViewImplUiBinder extends UiBinder<HTMLPanel, SignInViewImpl> {
    }

    private static SignInViewImplUiBinder ourUiBinder = GWT.create(SignInViewImplUiBinder.class);

    public SignInViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    private ForgotPasswordHandler forgotPasswordHandler = new ForgotPasswordHandler() {
        @Override
        public void handleForgotPassword() {
        }
    };

    @UiField
    protected TextBox userNameField;

    @UiField
    protected PasswordTextBox passwordField;

    @UiField
    protected HasClickHandlers forgotPasswordButton;

    @UiHandler("forgotPasswordButton")
    protected void handleForgotPassword(ClickEvent event) {
        forgotPasswordHandler.handleForgotPassword();
    }

    @Override
    public void clear() {
        userNameField.setText("");
        passwordField.setText("");
    }

    @Override
    public String getUserName() {
        return userNameField.getText().trim();
    }

    @Override
    public void setUserName(String userName) {
        userNameField.setText(userName.trim());
    }

    @Override
    public String getPassword() {
        return passwordField.getText();
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return Optional.<Focusable>of(userNameField);
    }

    @Override
    public void setForgotPasswordHandler(ForgotPasswordHandler handler) {
        this.forgotPasswordHandler = checkNotNull(handler);
    }
}