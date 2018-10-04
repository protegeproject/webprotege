package edu.stanford.bmir.protege.web.client.chgpwd;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordAction;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordData;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResult;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResultCode;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordPresenter {

    @Nonnull
    private final DispatchServiceManager dispatchService;

    @Nonnull
    private final Provider<ResetPasswordDialogController> resetPasswordDialogController;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Inject
    public ResetPasswordPresenter(@Nonnull DispatchServiceManager dispatchService,
                                  @Nonnull Provider<ResetPasswordDialogController> resetPasswordDialogController,
                                  @Nonnull Messages messages,
                                  @Nonnull MessageBox messageBox, @Nonnull DispatchErrorMessageDisplay errorDisplay) {
        this.dispatchService = dispatchService;
        this.resetPasswordDialogController = resetPasswordDialogController;
        this.messages = messages;
        this.messageBox = messageBox;
        this.errorDisplay = errorDisplay;
    }

    public void resetPassword() {
        showDialog();
    }

    private void showDialog() {
        ResetPasswordDialogController controller = resetPasswordDialogController.get();
        controller.setDialogButtonHandler(DialogButton.get(messages.password_resetPassword()), (data, closer) -> {
            closer.hide();
            resetPassword(data);
        });
        WebProtegeDialog<ResetPasswordData> dlg = new WebProtegeDialog<>(controller);
        dlg.setVisible(true);
    }

    private void resetPassword(ResetPasswordData data) {
        dispatchService.execute(new ResetPasswordAction(data), new DispatchServiceCallback<ResetPasswordResult>(errorDisplay) {

            @Override
            public void handleSuccess(ResetPasswordResult result) {
                if (result.getResultCode() == ResetPasswordResultCode.SUCCESS) {
                    messageBox.showMessage(messages.password_reset_success_msg(),
                                           messages.password_reset_success_submsg());
                }
                else if(result.getResultCode() == ResetPasswordResultCode.INVALID_EMAIL_ADDRESS) {
                    messageBox.showAlert(messages.password_reset_error_invalidemail_msg(),
                                         messages.password_reset_error_invalidemail_submsg());
                }
                else {
                    messageBox.showAlert(messages.password_reset_error_generic_msg(),
                                         messages.password_reset_error_generic_submsg());
                }
            }
        });
    }
}
