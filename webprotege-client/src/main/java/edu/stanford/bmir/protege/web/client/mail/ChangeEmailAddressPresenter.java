package edu.stanford.bmir.protege.web.client.mail;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalCloser;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.progress.ProgressMonitor;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressResult.Result.ADDRESS_ALREADY_EXISTS;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 06/11/2013
 */
public class ChangeEmailAddressPresenter {

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final ModalManager modalManager;

    @Nonnull
    private final EmailAddressEditor view;

    @Nonnull
    private final Messages messages;

    @Inject
    public ChangeEmailAddressPresenter(@Nonnull DispatchServiceManager dispatchServiceManager,
                                       @Nonnull LoggedInUserProvider loggedInUserProvider,
                                       @Nonnull MessageBox messageBox,
                                       @Nonnull ModalManager modalManager,
                                       @Nonnull EmailAddressEditor view, @Nonnull Messages messages) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
        this.messageBox = messageBox;
        this.modalManager = modalManager;
        this.view = view;
        this.messages = messages;
    }

    public void changeEmail() {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        if (userId.isGuest()) {
            messageBox.showAlert("You must be logged in to change your email address");
            return;
        }
        ProgressMonitor.get().showProgressMonitor("Retrieving email address", "Please wait.");

        dispatchServiceManager.execute(new GetEmailAddressAction(userId), result -> {
            showDialog(result.getEmailAddress());
            ProgressMonitor.get().hideProgressMonitor();
        });
    }

    private void showDialog(Optional<EmailAddress> emailAddress) {
        emailAddress.ifPresent(view::setValue);
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        ModalPresenter presenter = modalManager.createPresenter();
        presenter.setTitle(messages.changeEmailAddress());
        presenter.setView(view);
        presenter.setEscapeButton(DialogButton.CANCEL);
        presenter.setPrimaryButton(DialogButton.OK);
        presenter.setButtonHandler(DialogButton.OK, closer -> {
            view.getValue().ifPresent(address -> changeEmailAddress(userId, address, closer));
            if(!view.getValue().isPresent()) {
                messageBox.showAlert("The specified email addresses do not match.");
            }
        });
        modalManager.showModal(presenter);
    }

    private void changeEmailAddress(UserId userId, EmailAddress address, ModalCloser closer) {
        dispatchServiceManager.execute(new SetEmailAddressAction(userId, address.getEmailAddress()),
                                       result -> {
                                           if (result.getResult() == ADDRESS_ALREADY_EXISTS) {
                                               messageBox.showMessage("Address already taken",
                                                                      "The email address that you have specified is taken by another user.  " +
                                                                              "Please specify a different email address.");
                                           }
                                           else {
                                               closer.closeModal();
                                           }
                                       });
    }

}
