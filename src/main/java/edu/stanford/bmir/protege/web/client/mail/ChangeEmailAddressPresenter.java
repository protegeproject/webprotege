package edu.stanford.bmir.protege.web.client.mail;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.library.progress.ProgressMonitor;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class ChangeEmailAddressPresenter {

    public void changeEmail() {
        final UserId userId = Application.get().getUserId();
        if(userId.isGuest()) {
            MessageBox.showAlert("You must be logged in to change your email address");
            return;
        }
        ProgressMonitor.get().showProgressMonitor("Retrieving email address", "Please wait.");

        DispatchServiceManager.get().execute(new GetEmailAddressAction(userId), new DispatchServiceCallback<GetEmailAddressResult>() {
            @Override
            public void handleSuccess(GetEmailAddressResult result) {
                showDialog(result.getEmailAddress());
                ProgressMonitor.get().hideProgressMonitor();
            }

            @Override
            public void handleFinally() {
                ProgressMonitor.get().hideProgressMonitor();
            }
        });
    }

    private void showDialog(Optional<EmailAddress> emailAddress) {
        final UserId userId = Application.get().getUserId();
        ChangeEmailAddressDialogController controller = new ChangeEmailAddressDialogController();
        if (emailAddress.isPresent()) {
            controller.setValue(emailAddress.get());
        }
        controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<Optional<EmailAddress>>() {
            @Override
            public void handleHide(Optional<EmailAddress> data, final WebProtegeDialogCloser closer) {
                if(data.isPresent()) {
                    DispatchServiceManager.get().execute(new SetEmailAddressAction(userId, data.get().getEmailAddress()), new DispatchServiceCallback<SetEmailAddressResult>() {
                        @Override
                        public void handleSuccess(SetEmailAddressResult result) {
                            closer.hide();
                        }
                    });
                }
                else {
                    MessageBox.showAlert("The specified email addresses do not match.");
                }
            }
        });
        WebProtegeDialog<Optional<EmailAddress>> dlg = new WebProtegeDialog<Optional<EmailAddress>>(controller);
        dlg.setVisible(true);
    }

}
