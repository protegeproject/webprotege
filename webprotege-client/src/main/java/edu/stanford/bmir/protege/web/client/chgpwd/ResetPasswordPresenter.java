package edu.stanford.bmir.protege.web.client.chgpwd;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordAction;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordData;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResult;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResultCode;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordPresenter {

    @Nonnull
    private final ResetPasswordView view;

    @Nonnull
    private final DispatchServiceManager dispatchService;

    @Nonnull
    private final ModalManager modalManager;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Inject
    public ResetPasswordPresenter(@Nonnull ResetPasswordView view,
                                  @Nonnull DispatchServiceManager dispatchService,
                                  @Nonnull Messages messages,
                                  @Nonnull MessageBox messageBox,
                                  @Nonnull DispatchErrorMessageDisplay errorDisplay,
                                  @Nonnull ModalManager modalManager) {
        this.view = view;
        this.dispatchService = dispatchService;
        this.modalManager = modalManager;
        this.messages = messages;
        this.messageBox = messageBox;
        this.errorDisplay = errorDisplay;
    }

    public void resetPassword() {
        showDialog();
    }

    private void showDialog() {
        view.clearValue();
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle(messages.password_resetPassword());
        modalPresenter.setView(view);
        DialogButton resetPwdBtn = DialogButton.get(messages.password_resetPassword());
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setPrimaryButton(resetPwdBtn);
        modalPresenter.setButtonHandler(resetPwdBtn, closer -> {
            view.getValue().ifPresent(data -> {
                closer.closeModal();
                resetPassword(data);
            });
        });
        modalManager.showModal(modalPresenter);
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
