package edu.stanford.bmir.protege.web.client.library.msgbox;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.library.modal.ModalViewImpl;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/07/2013
 */
public class MessageBox {

    private static final Void RETURN = null;

    private static final String DLG_TITLE = "";

    private static final String DEFAULT_SUB_MESSAGE = "";

    @Nonnull
    private final ModalManager modalManager;

    @Inject
    public MessageBox(@Nonnull ModalManager modalManager) {
        this.modalManager = checkNotNull(modalManager);
    }

    private static MessageBoxView createMessageBox(MessageStyle messageStyle, String mainMessage, String subMessage) {
        final MessageBoxView messageBoxView = new MessageBoxViewImpl();
        messageBoxView.setMainMessage(mainMessage);
        messageBoxView.setSubMessage(subMessage);
        messageBoxView.setMessageStyle(messageStyle);
        return messageBoxView;
    }

    /**
     * Shows a {@link MessageBox} that displays a single message.
     * The message box will have a "message" icon.
     *
     * @param mainMessage The message to be displayed.
     */
    public void showMessage(@Nonnull String mainMessage) {
        showMessage(mainMessage, DEFAULT_SUB_MESSAGE);
    }

    /**
     * Shows a {@link MessageBox} that displays a main message (like a title) along with a sub-message.
     * The message box will have a "message" icon.
     *
     * @param mainMessage The main message.
     * @param subMessage  The sub-message.
     */
    public void showMessage(@Nonnull String mainMessage,
                            @Nonnull String subMessage) {
        showMessageBox(MessageStyle.MESSAGE, mainMessage, subMessage, () -> {});
    }

    /**
     * Shows a {@link MessageBox} that displays a main message (like a title) along with a sub-message.
     * The message box will have a "message" icon.
     *
     * @param mainMessage    The main message.
     * @param subMessage     The sub-message.
     * @param closedCallback A callback that will be run when the user dismisses the message box.
     */
    public void showMessage(@Nonnull String mainMessage,
                            @Nonnull String subMessage,
                            @Nonnull Runnable closedCallback) {
        showMessageBox(MessageStyle.MESSAGE, mainMessage, subMessage, closedCallback);
    }

    /**
     * Shows a {@link MessageBox} that displays a single message.
     * The message box will have an "alert" icon.
     *
     * @param mainMessage The message to be displayed.
     */
    public void showAlert(@Nonnull String mainMessage) {
        showAlert(mainMessage, "");
    }

    /**
     * Shows a {@link MessageBox} that displays a main message (like a title) along with a sub-message.
     * The message box will have an "alert" icon.
     *
     * @param mainMessage The main message.
     * @param subMessage  The sub-message.
     */
    public void showAlert(@Nonnull String mainMessage,
                          @Nonnull String subMessage) {
        showMessageBox(MessageStyle.ALERT, mainMessage, subMessage, () -> {});
    }

    /**
     * Shows a {@link MessageBox} that displays a main message (like a title) along with a sub-message.
     * The message box will have an "alert" icon.
     *
     * @param mainMessage    The main message.
     * @param subMessage     The sub-message.
     * @param closedCallback A callback that will be run when the user dismisses the message box.
     */
    public void showAlert(@Nonnull String mainMessage,
                          @Nonnull String subMessage,
                          @Nonnull Runnable closedCallback) {
        showMessageBox(MessageStyle.ALERT, mainMessage, subMessage, closedCallback);
    }

    public void showErrorMessage(String mainMessage, Throwable throwable) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        sb.appendEscaped("Details: ");
        sb.appendEscaped(throwable.getMessage());
        showAlert(mainMessage, sb.toSafeHtml().toString());
    }

    public void showOKCancelConfirmBox(String mainMessage, String subMessage, final OKCancelHandler handler) {
        final MessageBoxView messageBoxView = createMessageBox(MessageStyle.QUESTION, mainMessage, subMessage);
        ModalPresenter presenter = modalManager.createPresenter();
        presenter.setView(messageBoxView);
        presenter.setButtonHandler(OK, closer -> {
            closer.closeModal();
            handler.handleOK();
        });
        presenter.setButtonHandler(CANCEL, closer -> {
            closer.closeModal();
            handler.handleCancel();
        });
        modalManager.showModal(presenter);
    }

    public void showYesNoConfirmBox(String mainMessage, String subMessage, final YesNoHandler handler) {
        final MessageBoxView messageBoxView = createMessageBox(MessageStyle.QUESTION, mainMessage, subMessage);
        ModalPresenter presenter = new ModalPresenter(new ModalViewImpl(WebProtegeClientBundle.BUNDLE));
        presenter.setEscapeButton(YES);
        presenter.setPrimaryButton(NO);
        presenter.setButtonHandler(YES, closer -> {
            closer.closeModal();
            handler.handleYes();
        });
        presenter.setButtonHandler(NO, closer -> {
            closer.closeModal();
            handler.handleNo();
        });
        presenter.setView(messageBoxView);
        modalManager.showModal(presenter);
    }

    public void showConfirmBox(String mainMessage, String subMessage,
                               DialogButton escapeButton, DialogButton acceptButton,
                               Runnable acceptHandler, DialogButton defaultButton) {
        showConfirmBox(MessageStyle.QUESTION,
                       mainMessage,
                       subMessage,
                       escapeButton,
                       () -> {},
                       acceptButton, acceptHandler, defaultButton);
    }

    public void showConfirmBox(MessageStyle messageStyle, String mainMessage, String subMessage,
                               DialogButton escapeButton, Runnable escapeHandler, DialogButton acceptButton,
                               Runnable acceptHandler, DialogButton defaultButton) {
        final MessageBoxView messageBoxView = createMessageBox(messageStyle, mainMessage, subMessage);
        ModalPresenter presenter = modalManager.createPresenter();
        presenter.setTitle(DLG_TITLE);
        presenter.setPrimaryButton(escapeButton);
        presenter.setEscapeButton(acceptButton);
        presenter.setPrimaryButtonFocusedOnShow(true);
        presenter.setButtonHandler(escapeButton, closer -> {
            closer.closeModal();
            escapeHandler.run();
        });
        presenter.setButtonHandler(acceptButton, closer -> {
            closer.closeModal();
            acceptHandler.run();
        });
        presenter.setView(messageBoxView);
        modalManager.showModal(presenter);
    }

    public void showYesNoConfirmBox(String mainMessage, String subMessage, final Runnable yesHandler) {
        showYesNoConfirmBox(mainMessage, subMessage, new YesNoHandler() {
            @Override
            public void handleYes() {
                yesHandler.run();
            }

            @Override
            public void handleNo() {
                // Ignore
            }
        });
    }

    private void showMessageBox(MessageStyle messageStyle, String mainMessage, String subMessage, Runnable callback) {
        final MessageBoxView messageBoxView = createMessageBox(messageStyle, mainMessage, subMessage);
        ModalPresenter presenter = modalManager.createPresenter();
        presenter.setView(messageBoxView);
        presenter.setPrimaryButton(OK);
        presenter.setButtonHandler(OK, closer -> {
            closer.closeModal();
            callback.run();
        });
        presenter.setPrimaryButtonFocusedOnShow(true);
        modalManager.showModal(presenter);
    }
}
