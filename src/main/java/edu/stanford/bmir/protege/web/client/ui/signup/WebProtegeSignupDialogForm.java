package edu.stanford.bmir.protege.web.client.ui.signup;

import com.google.common.base.Optional;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import edu.stanford.bmir.protege.web.client.signup.SignupInfo;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.*;
import edu.stanford.bmir.protege.web.client.ui.verification.HumanVerificationServiceProvider;
import edu.stanford.bmir.protege.web.client.ui.verification.HumanVerificationWidget;
import edu.stanford.bmir.protege.web.client.ui.verification.NullHumanVerificationWidget;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/06/2012
 */
public class WebProtegeSignupDialogForm extends WebProtegeDialogForm implements WebProtegeDialogValidator {

    public static final int PASSWORD_MIN_LENGTH = 1;

    private static final int VISIBLE_LENGTH = 60;

    private final TextBox emailField;

    private final PasswordTextBox passwordField;

    private final PasswordTextBox confirmPasswordField;
    
    private String validationMessage = "";

    private RegExp EMAIL_PATTERN = RegExp.compile("([^@]+)@.+");

    private final TextBox userNameField;

    private final HumanVerificationWidget verificationWidget;


    public WebProtegeSignupDialogForm() {

        emailField = addTextBox("Email address", "Enter your email address", "", new WebProtegeDialogInlineValidator<ValueBoxBase<String>>() {

            private InlineValidationResult lastValidationResult = InlineValidationResult.getValid();

            @Override
            public InlineValidationResult getValidation(ValueBoxBase<String> widget) {
                if(widget.getText().trim().isEmpty()) {
                    return lastValidationResult = InlineValidationResult.getInvalid("Please enter an email address");
                }

                MatchResult matchResult = EMAIL_PATTERN.exec(widget.getText().trim());
                if(matchResult != null) {
                    return lastValidationResult = InlineValidationResult.getValid();
                }
                else {
                    return lastValidationResult = InlineValidationResult.getInvalid("Invalid email address");
                }
            }

            @Override
            public boolean shouldCheckOnKeyUp() {
                return lastValidationResult.isInvalid();
            }

            @Override
            public boolean shouldCheckOnValueChange() {
                return true;
            }
        });
        emailField.setVisibleLength(VISIBLE_LENGTH);

        addVerticalSpacer();

        userNameField = addTextBox("User name", "Choose a user name (e.g. Harry Potter)", "", new WebProtegeDialogInlineValidator<ValueBoxBase<String>>() {
            @Override
            public InlineValidationResult getValidation(ValueBoxBase<String> widget) {
                final String enteredUserName = widget.getText().trim();
                if(enteredUserName.equalsIgnoreCase(UserId.getGuest().getUserName())) {
                    return InlineValidationResult.getInvalid("You cannot choose the user name '" + enteredUserName + "'.  Please choose a different user name.");
                }
                else {
                    return InlineValidationResult.getValid();
                }
            }

            @Override
            public boolean shouldCheckOnKeyUp() {
                return true;
            }

            @Override
            public boolean shouldCheckOnValueChange() {
                return true;
            }
        });
        userNameField.setVisibleLength(VISIBLE_LENGTH);

        addVerticalSpacer();
        passwordField = new PasswordTextBox();
        addWidget("Password", passwordField);
        confirmPasswordField = new PasswordTextBox();
        addWidget("Confirm Password", confirmPasswordField);

        addVerticalSpacer();
        verificationWidget = new NullHumanVerificationWidget();
//        verificationWidget = new RecaptchaWidget();
        addWidget("", verificationWidget.asWidget());
    }



    public Optional<Focusable> getInitialFocusable() {
        return Optional.<Focusable>of(emailField);
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
        HumanVerificationServiceProvider verificationServiceProvider = verificationWidget.getVerificationServiceProvider();

        return new SignupInfo(emailAddress, getUserName(), getPassword(), verificationServiceProvider);
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

        InlineValidationResult inlineValidationResult = runTextFieldValidation();
        if(inlineValidationResult.isInvalid()) {
            validationMessage = inlineValidationResult.getMessage();
            return ValidationState.INVALID;
        }
        return ValidationState.VALID;
    }

    public String getValidationMessage() {
        return validationMessage;
    }
}
