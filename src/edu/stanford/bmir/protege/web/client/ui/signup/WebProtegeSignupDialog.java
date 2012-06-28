package edu.stanford.bmir.protege.web.client.ui.signup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.RecaptchaService;
import edu.stanford.bmir.protege.web.client.rpc.RecaptchaServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.SignupInfo;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserNameAlreadyExistsException;
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
    }


    private void doSignup(final SignupInfo data, final WebProtegeDialogCloser closer) {
        RecaptchaServiceAsync serviceAsync = GWT.create(RecaptchaService.class);
        serviceAsync.isSuccessful(data.getVerificationChallenge(), data.getVerificationResponse(), new AsyncCallback<Boolean>() {
            public void onFailure(Throwable caught) {
                Window.alert("The verification text is incorrect.  Please try again.");
            }

            public void onSuccess(Boolean result) {
                if(result) {
                    handleSuccess(data, closer);
                }
                else {
                    Window.alert("The verification text is incorrect.  Please try again.");
                }
            }
        });
    }


    private void handleSuccess(final SignupInfo data, final WebProtegeDialogCloser dialogCloser) {
        final AdminServiceManager adminServiceManager = AdminServiceManager.getInstance();
        adminServiceManager.getNewSalt(new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
                Window.alert("There was a problem registering the specified user account. Please contact admin. (Problem " + caught.getMessage() + ")");
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
                            Window.alert("A user named " + username + " is already registered.  Please choose another name.");
                        }
                        else {
                            Window.alert("There was a problem registering the specified user account.  Please contact administrator.");
                        }
                    }

                    public void onSuccess(UserData result) {
                        Window.alert("You have successfully registered.  Please log in using the button/link on the top right.");
                        dialogCloser.hide();
                    }
                });
            }
        });
        
    }
    
    
}


