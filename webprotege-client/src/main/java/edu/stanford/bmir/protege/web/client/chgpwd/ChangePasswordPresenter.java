package edu.stanford.bmir.protege.web.client.chgpwd;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.auth.AuthenticatedActionExecutor;
import edu.stanford.bmir.protege.web.client.auth.AuthenticatedDispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.library.modal.ModalCloser;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.auth.SaltProvider;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.chgpwd.ChangePasswordData;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2013
 */
public class ChangePasswordPresenter {

    private final DispatchServiceManager dispatchServiceManager;

    private final MessageBox messageBox;

    private final ChangePasswordView changePasswordView;

    private final UserId userId;

    private final DispatchErrorMessageDisplay errorDisplay;

    private final ProgressDisplay progressDisplay;

    private final ModalPresenter modalPresenter;

    private final ModalManager modalManager;

    private final Messages messages;

    @AutoFactory
    @Inject
    public ChangePasswordPresenter(@Provided ChangePasswordView changePasswordView,
                                   UserId userId,
                                   @Provided DispatchServiceManager dispatchServiceManager,
                                   @Provided MessageBox messageBox,
                                   @Provided DispatchErrorMessageDisplay errorDisplay,
                                   @Provided ProgressDisplay progressDisplay,
                                   @Provided ModalPresenter modalPresenter,
                                   @Provided ModalManager modalManager,
                                   @Provided Messages messages) {
        this.changePasswordView = changePasswordView;
        this.userId = checkNotNull(userId);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.messageBox = checkNotNull(messageBox);
        this.errorDisplay = errorDisplay;
        this.progressDisplay = progressDisplay;
        this.modalPresenter = modalPresenter;
        this.modalManager = modalManager;
        this.messages = messages;
    }

    public void changePassword() {
        if (userId.isGuest()) {
            messageBox.showAlert("The password of the guest user cannot be changed");
            return;
        }
        showDialog();
    }

    private void showDialog() {
        modalPresenter.setTitle(messages.changePassword());
        modalPresenter.setContent(changePasswordView);
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setPrimaryButton(DialogButton.OK);
        modalPresenter.setButtonHandler(DialogButton.OK, closer -> {
            if (changePasswordView.getNewPassword().isEmpty()) {
                messageBox.showAlert("Please specify a new password");
            } else if (!isPasswordConfirmationCorrect()) {
                handleIncorrectPasswordConfirmation();
            } else {
                executeChangePassword(closer);
            }
        });
        modalManager.showModal(modalPresenter);
    }

    private boolean isPasswordConfirmationCorrect() {
        String newPassword = changePasswordView.getNewPassword();
        String newPasswordConfirmation = changePasswordView.getNewPasswordConfirmation();
        return newPassword.equals(newPasswordConfirmation);
    }


    private void handleIncorrectPasswordConfirmation() {
        messageBox.showAlert("Passwords do not match", "Please re-enter the new password and confirmation.");
    }

    private void handleIncorrectCurrentPassword() {
        messageBox.showAlert("Current password is incorrect", "The password that you specified as your current password is incorrect. Please re-enter your current password and try again.");
    }


    private void executeChangePassword(final ModalCloser closer) {
        AuthenticatedActionExecutor executor = new AuthenticatedActionExecutor(dispatchServiceManager, new PasswordDigestAlgorithm(new Md5DigestAlgorithmProvider()), new ChapResponseDigestAlgorithm(new Md5DigestAlgorithmProvider()), errorDisplay);
        String currentPassword = changePasswordView.getOldPassword();
        String newPassword = changePasswordView.getNewPassword();
        ChangePasswordActionFactory actionFactory = new ChangePasswordActionFactory(newPassword, new SaltProvider());
        executor.execute(userId, currentPassword, actionFactory, new AuthenticatedDispatchServiceCallback<ChangePasswordResult>(errorDisplay, progressDisplay) {

            @Override
            public void handleAuthenticationResponse(@Nonnull AuthenticationResponse response) {
                if(response == AuthenticationResponse.SUCCESS) {
                    messageBox.showMessage("Your password has been changed");
                    closer.closeModal();
                }
                else {
                    handleIncorrectCurrentPassword();
                }
            }

            @Override
            public String getProgressDisplayTitle() {
                return "Changing password";
            }

            @Override
            public String getProgressDisplayMessage() {
                return "Please wait.";
            }
        });
    }
}
