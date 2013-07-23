package edu.stanford.bmir.protege.web.client.ui.signup;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.SignupInfo;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.verification.*;
import edu.stanford.bmir.protege.web.shared.user.UserEmailAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.user.UserNameAlreadyExistsException;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.login.HashAlgorithm;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/06/2012
 */
public class WebProtegeSignupDialog extends WebProtegeDialog<SignupInfo> {

    public WebProtegeSignupDialog() {
        super(new WebProtegeSignupDialogController());
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<SignupInfo>() {
            public void handleHide(SignupInfo data, WebProtegeDialogCloser closer) {
                doSignup(data, closer);
            }
        });
        setDialogButtonHandler(DialogButton.CANCEL, new WebProtegeDialogButtonHandler<SignupInfo>() {
            @Override
            public void handleHide(SignupInfo data, WebProtegeDialogCloser closer) {
                closer.hide();
            }
        });
    }


    private void doSignup(final SignupInfo data, final WebProtegeDialogCloser closer) {
        HumanVerificationServiceProvider verificationServiceProvider = data.getVerificationServiceProvider();
        verificationServiceProvider.runVerification(new HumanVerificationHandler() {
            @Override
            public void handleVerificationSuccess() {
                handleSuccess(data, closer);
            }

            @Override
            public void handleVerificationFailure(String errorMessage) {
                MessageBox.showAlert(errorMessage);
            }
        });
    }


    private void handleSuccess(final SignupInfo data, final WebProtegeDialogCloser dialogCloser) {
        final AdminServiceManager adminServiceManager = AdminServiceManager.getInstance();
        adminServiceManager.getNewSalt(new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
                MessageBox.showAlert("Error", "There was a problem registering the specified user account. Please contact admin. (Problem " + caught.getMessage() + ")");
            }

            public void onSuccess(String salt) {
                HashAlgorithm hashAlgorithm = new HashAlgorithm();
                String userName = data.getUserName();
                String email = data.getEmailAddress().getEmailAddress();
                String hashedPassword = hashAlgorithm.md5(salt + data.getPassword());
                adminServiceManager.registerUserViaEncrption(userName, hashedPassword, email, new AsyncCallback<UserData>() {
                    public void onFailure(Throwable caught) {
                        if(caught instanceof UserNameAlreadyExistsException) {
                            String username = ((UserNameAlreadyExistsException) caught).getUsername();
                            MessageBox.showAlert("User name already taken", "A user named " + username + " is already registered.  Please choose another name.");
                        }
                        else if(caught instanceof UserEmailAlreadyExistsException) {
                            String email = ((UserEmailAlreadyExistsException) caught).getEmailAddress();
                            MessageBox.showAlert("Email address already taken", "The email address " + email + " is already taken.  Please choose a different email address.");
                        }
                        else {
                            MessageBox.showAlert("Error registering account", "There was a problem registering the specified user account.  Please contact administrator.");
                        }
                    }

                    public void onSuccess(UserData result) {
                        MessageBox.showMessage("Registration complete", "You have successfully registered.  Please log in using the button/link on the top right.");
                        dialogCloser.hide();
                    }
                });
            }
        });
        
    }
    
    
}


