package edu.stanford.bmir.protege.web.client.ui.signup;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.SignupInfo;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.verification.HumanVerificationHandler;
import edu.stanford.bmir.protege.web.client.ui.verification.HumanVerificationServiceProvider;
import edu.stanford.bmir.protege.web.shared.auth.Md5DigestAlgorithmProvider;
import edu.stanford.bmir.protege.web.shared.auth.PasswordDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.auth.SaltProvider;
import edu.stanford.bmir.protege.web.shared.user.*;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/06/2012
 */
public class WebProtegeSignupDialogController extends WebProtegeOKCancelDialogController<SignupInfo> {

    public static final String TITLE = "Create an Account";

    private final WebProtegeSignupDialogForm form;

    private final DispatchServiceManager dispatchServiceManager;

    public WebProtegeSignupDialogController(DispatchServiceManager dispatchServiceManager) {
        super(TITLE);
        form = new WebProtegeSignupDialogForm();
        addDialogValidator(form);

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
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public Widget getWidget() {
        return form;
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return form.getInitialFocusable();
    }

    @Override
    public SignupInfo getData() {
        return form.getData();
    }


    ////////////////////

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
        CreateUserAccountExecutor executor = new CreateUserAccountExecutor(
                dispatchServiceManager,
                new PasswordDigestAlgorithm(new Md5DigestAlgorithmProvider()),
                new SaltProvider()
        );

        UserId userId = UserId.getUserId(data.getUserName());
        executor.execute(userId, data.getEmailAddress(), data.getPassword(), new DispatchServiceCallback<CreateUserAccountResult>() {
            @Override
            public void handleSuccess(CreateUserAccountResult createUserAccountResult) {
                MessageBox.showMessage("Registration complete",
                        "You have successfully registered.  " +
                                "Please log in using the button/link on the top right.");
                dialogCloser.hide();
            }

            @Override
            public void handleExecutionException(Throwable cause) {
                if (cause instanceof UserNameAlreadyExistsException) {
                    String username = ((UserNameAlreadyExistsException) cause).getUsername();
                    MessageBox.showAlert("User name already taken", "A user named "
                            + username
                            + " is already registered.  Please choose another name.");
                }
                else if (cause instanceof UserEmailAlreadyExistsException) {
                    String email = ((UserEmailAlreadyExistsException) cause).getEmailAddress();
                    MessageBox.showAlert("Email address already taken", "The email address "
                            + email
                            + " is already taken.  Please choose a different email address.");
                }
                else if (cause instanceof UserRegistrationException) {
                    MessageBox.showAlert(cause.getMessage());
                }
                else {
                    MessageBox.showAlert("Error registering account",
                            "There was a problem registering the specified user account.  " +
                                    "Please contact administrator.");
                }
            }
        });
    }
}
