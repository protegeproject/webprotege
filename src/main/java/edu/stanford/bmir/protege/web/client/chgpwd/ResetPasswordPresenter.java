package edu.stanford.bmir.protege.web.client.chgpwd;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordAction;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordData;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResult;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResultCode;

import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordPresenter {

    private DispatchServiceManager dispatchService;

    private ResetPasswordView view;

    @Inject
    public ResetPasswordPresenter(
            DispatchServiceManager dispatchService, ResetPasswordView view) {
        this.dispatchService = dispatchService;
        this.view = view;
    }

    public void resetPassword() {
        showDialog();
    }

    private void showDialog() {
        ResetPasswordDialogController controller = new ResetPasswordDialogController(view);
        controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<ResetPasswordData>() {
            @Override
            public void handleHide(ResetPasswordData data, WebProtegeDialogCloser closer) {
                closer.hide();
                resetPassword(data);
            }
        });
        WebProtegeDialog<ResetPasswordData> dlg = new WebProtegeDialog<ResetPasswordData>(controller);
        dlg.setVisible(true);
    }

    private void resetPassword(ResetPasswordData data) {
        dispatchService.execute(new ResetPasswordAction(data), new DispatchServiceCallback<ResetPasswordResult>() {

            @Override
            public void handleSuccess(ResetPasswordResult result) {
                if (result.getResultCode() == ResetPasswordResultCode.SUCCESS) {
                    MessageBox.showMessage("Your password has been reset.  " +
                                                   "A temporary password has been sent your email address.");
                }
                else if(result.getResultCode() == ResetPasswordResultCode.INVALID_EMAIL_ADDRESS) {
                    MessageBox.showAlert("Invalid email address.  " +
                                                 "The email address that you supplied is not valid.  " +
                                                 "Your password has not been reset.");
                }
                else {
                    MessageBox.showAlert("Password reset error",
                                         "An error occurred and your password could not be reset.  " +
                                                 "Please contact the administrator.");
                }
            }
        });
    }
}
