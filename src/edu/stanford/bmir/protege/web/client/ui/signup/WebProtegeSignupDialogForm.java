package edu.stanford.bmir.protege.web.client.ui.signup;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.client.rpc.data.SignupInfo;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.ValidationState;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogForm;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogValidator;
import edu.stanford.bmir.protege.web.client.ui.library.recaptcha.RecaptchaWidget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/06/2012
 */
public class WebProtegeSignupDialogForm extends WebProtegeDialogForm implements WebProtegeDialogValidator {

    public static final int PASSWORD_MIN_LENGTH = 1;

    private final TextBox emailField;

    private final PasswordTextBox passwordField;

    private final PasswordTextBox confirmPasswordField;
    
    private String validationMessage = "";

    private RegExp EMAIL_PATTERN = RegExp.compile("([^@]+)@([^@]+)");

    private final TextBox userNameField;

    private final RecaptchaWidget recaptureWidget;


    public WebProtegeSignupDialogForm() {
        emailField = new TextBox();
        emailField.setVisibleLength(50);
        addWidget("Email address", emailField);
        addVerticalSpacer();

        userNameField = new TextBox();
        userNameField.setVisibleLength(50);
        userNameField.getElement().setAttribute("placeHolder", "Choose a user name (e.g. Harry Potter)");
        addWidget("User name", userNameField);

        addVerticalSpacer();
        passwordField = new PasswordTextBox();
        addWidget("Password", passwordField);
        confirmPasswordField = new PasswordTextBox();
        addWidget("Confirm Password", confirmPasswordField);

        addVerticalSpacer();
        recaptureWidget = new RecaptchaWidget();
        addWidget("Verification", recaptureWidget);
    }



    public Focusable getInitialFocusable() {
        return emailField;
    }
    
    public EmailAddress getEmailAddress() {
        String emailAddress = emailField.getText().trim();
        return new EmailAddress(emailAddress);
    }

    public String getUserName() {
        return userNameField.getText().trim();
    }
    
//    public String getLastName() {
//        return userNameField.getText().trim();
//    }
    
    public boolean isPasswordConfirmed() {
        String password = getPassword();
        String confirmPassword = getConfirmPassword();
        return password.equals(confirmPassword);
    }
    
    private String getPassword() {
        return passwordField.getText().trim();
    }
    
    private String getConfirmPassword() {
        return confirmPasswordField.getText().trim();
    }

    public SignupInfo getData() {
        EmailAddress emailAddress = getEmailAddress();
        String challenge = recaptureWidget.getChallenge();
        String response = recaptureWidget.getResponse();
        return new SignupInfo(emailAddress, getUserName(), getPassword(), challenge, response);
    }

    public RecaptchaWidget getRecaptureWidget() {
        return recaptureWidget;
    }

    public WebProtegeDialogValidator getValidator() {
        return this;
    }

    public ValidationState getValidationState() {
        EmailAddress emailAddress = getEmailAddress();
        if(emailAddress.isEmpty()) {
            validationMessage = "Please enter an email address";
            return ValidationState.INVALID;
        }
//        if(EMAIL_PATTERN.test(emailAddress.getEmailAddress())) {
//            validationMessage = "Please enter a valid email address";
//            return ValidationState.INVALID;
//        }

        if(getUserName().isEmpty()) {
            validationMessage = "Please enter a user name";
            return ValidationState.INVALID;
        }

        String password = getPassword();
        if(password.length() < PASSWORD_MIN_LENGTH) {
            validationMessage = "Please enter a password that is at least " + PASSWORD_MIN_LENGTH + " characters in length";
            return ValidationState.INVALID;
        }
        String confirmPassword = getConfirmPassword();
        if(!confirmPassword.equals(password)) {
            validationMessage = "Passwords do not match.  Please check you enter your password correctly";
            return ValidationState.INVALID;
        }


        return ValidationState.VALID;
    }

    public String getValidationMessage() {
        return validationMessage;
    }
}
