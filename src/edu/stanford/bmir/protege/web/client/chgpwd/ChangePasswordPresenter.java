package edu.stanford.bmir.protege.web.client.chgpwd;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.LoginChallengeData;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.login.HashAlgorithm;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
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
                    doGetUserSaltAndChallenge(data, closer);
                }
            }
        });
        WebProtegeDialog<ChangePasswordData> dlg = new WebProtegeDialog<ChangePasswordData>(controller);
        dlg.setVisible(true);
    }

    private void doGetUserSaltAndChallenge(final ChangePasswordData data, final WebProtegeDialogCloser closer) {
        AdminServiceManager.getInstance().getUserSaltAndChallenge(userId, new AsyncCallback<LoginChallengeData>() {
            public void onSuccess(LoginChallengeData result) {
                if (result != null) {
                    doCurrentPasswordValidation(result, data, closer);
                } else {
                    handleIncorrectCurrentPassword();
                }
            }

            public void onFailure(Throwable caught) {
                showFailureMessage();
            }
        });
    }

    private static boolean isPasswordConfirmationCorrect(ChangePasswordData data) {
        String newPassword = data.getNewPassword();
        String newPasswordConfirmation = data.getNewPasswordConfirmation();
        return newPassword.equals(newPasswordConfirmation);
    }

    protected void doCurrentPasswordValidation(LoginChallengeData result, final ChangePasswordData data, final WebProtegeDialogCloser closer) {
        final HashAlgorithm hashAlgorithm = new HashAlgorithm();
        final String saltedHashedPass = hashAlgorithm.md5(result.getSalt() + data.getOldPassword());
        final String response = hashAlgorithm.md5(result.getChallenge() + saltedHashedPass);
        AdminServiceManager.getInstance().authenticateToLogin(userId, response, new AsyncCallback<UserId>() {
            public void onSuccess(UserId userData) {
                if (!userData.isGuest()) {
                    doGetSaltForNewPassword(data, closer);
                } else {
                    handleIncorrectCurrentPassword();
                }
            }

            public void onFailure(Throwable caught) {
                MessageBox.showAlert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
            }
        });
    }

    private void doGetSaltForNewPassword(final ChangePasswordData data, final WebProtegeDialogCloser closer) {
        AdminServiceManager.getInstance().getNewSalt(new AsyncCallback<String>() {
            public void onSuccess(String salt) {
                doChangePassword(salt, data, closer);
            }

            public void onFailure(Throwable caught) {
                MessageBox.showAlert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
            }
        });
    }

    private void doChangePassword(String salt, ChangePasswordData data, final WebProtegeDialogCloser closer) {
        final HashAlgorithm hashAlgorithm = new HashAlgorithm();
        String saltedHashedNewPass = hashAlgorithm.md5(salt + data.getNewPassword());
        AdminServiceManager.getInstance().changePasswordEncrypted(userId, saltedHashedNewPass, salt, new AsyncCallback<Boolean>() {
            public void onSuccess(Boolean result) {
                if (result) {
                    closer.hide();
                    handleSuccess();
                } else {
                    showFailureMessage();
                }
            }

            public void onFailure(Throwable caught) {
                showFailureMessage();
            }
        });
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
}
