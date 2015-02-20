package edu.stanford.bmir.protege.web.client.chgpwd;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.auth.SaltProvider;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.chgpwd.ChangePasswordData;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2013
 */
public class ChangePasswordPresenter {

    private ChangePasswordView changePasswordView;

    private UserId userId;

    public ChangePasswordPresenter(ChangePasswordView changePasswordView, UserId userId) {
        this.changePasswordView = changePasswordView;
        this.userId = checkNotNull(userId);
    }

    public ChangePasswordPresenter(UserId userId) {
        this(new ChangePasswordViewImpl(), userId);
    }

    public void changePassword() {
        if (userId.isGuest()) {
            MessageBox.showAlert("The password of the guest user cannot be changed");
            return;
        }
        showDialog();
    }

    private void showDialog() {
        ChangePasswordDialogController controller = new ChangePasswordDialogController(changePasswordView);
        controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<ChangePasswordData>() {
            @Override
            public void handleHide(final ChangePasswordData data, WebProtegeDialogCloser closer) {
                if (data.getNewPassword().isEmpty()) {
                    MessageBox.showAlert("Please specify a new password");
                } else if (!isPasswordConfirmationCorrect(data)) {
                    handleIncorrectPasswordConfirmation();
                } else {
                    executeChangePassword(data, closer);
                }
            }
        });
        WebProtegeDialog<ChangePasswordData> dlg = new WebProtegeDialog<ChangePasswordData>(controller);
        dlg.setVisible(true);
    }

    private static boolean isPasswordConfirmationCorrect(ChangePasswordData data) {
        String newPassword = data.getNewPassword();
        String newPasswordConfirmation = data.getNewPasswordConfirmation();
        return newPassword.equals(newPasswordConfirmation);
    }

    private void handleSuccess() {
        MessageBox.showMessage("Your password has been changed");
    }

    private void showFailureMessage() {
        MessageBox.showAlert("There was a problem changing your password. Please try again");
    }

    private void handleIncorrectPasswordConfirmation() {
        MessageBox.showAlert("Passwords do not match", "Please re-enter the new password and confirmation.");
    }

    private void handleIncorrectCurrentPassword() {
        MessageBox.showAlert("Current password is incorrect", "The password that you specified as your current password is incorrect. Please re-enter your current password and try again.");
    }


    private void executeChangePassword(final ChangePasswordData data, final WebProtegeDialogCloser closer) {
        AuthenticatedActionExecutor executor = new AuthenticatedActionExecutor(DispatchServiceManager.get(), new PasswordDigestAlgorithm(new Md5DigestAlgorithmProvider()), new ChapResponseDigestAlgorithm(new Md5DigestAlgorithmProvider()));
        String currentPassword = data.getOldPassword();
        String newPassword = data.getNewPassword();
        ChangePasswordActionFactory actionFactory = new ChangePasswordActionFactory(newPassword, new SaltProvider());
        executor.execute(userId, currentPassword, actionFactory, new AsyncCallback<AuthenticationResponse>() {
            @Override
            public void onSuccess(AuthenticationResponse response) {
                if(response == AuthenticationResponse.SUCCESS) {
                    handleSuccess();
                    closer.hide();
                }
                else {
                    handleIncorrectCurrentPassword();
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                showFailureMessage();
            }
        });
    }
}
