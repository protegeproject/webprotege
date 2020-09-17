package edu.stanford.bmir.protege.web.client.library.msgbox;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.*;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/04/2013
 */
public class InputBox {

    @Nonnull
    private final ModalManager modalManager;

    @Nonnull
    private final Provider<InputBoxView> viewProvider;

    @Inject
    public InputBox(@Nonnull ModalManager modalManager, @Nonnull Provider<InputBoxView> viewProvider) {
        this.modalManager = checkNotNull(modalManager);
        this.viewProvider = checkNotNull(viewProvider);
    }

    public void showDialog(String title, InputBoxHandler handler) {
        showDialog(title, true, "", handler);
    }

    public void showDialog(String title, boolean multiline, String initialInput, InputBoxHandler handler) {
        showModal(title, "", multiline, initialInput, handler, true);
    }

    public void showDialog(String title, String message, boolean multiline, String initialInput, InputBoxHandler handler) {
        showModal(title, message, multiline, initialInput, handler, true);
    }

    public void showOkDialog(String title, boolean multiline, String initialInput, InputBoxHandler handler) {
        showModal(title, "", multiline, initialInput, handler, false);
    }

    public void showOkDialog(String title, String message, boolean multiline, String initialInput, InputBoxHandler handler) {
        showModal(title, message, multiline, initialInput, handler, false);
    }

    private void showModal(@Nonnull String title,
                           @Nonnull String message,
                           boolean multiline,
                           @Nonnull String initialInput,
                           InputBoxHandler handler,
                           boolean showCancelButton) {
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle(title);
        InputBoxView view = viewProvider.get();
        view.setMessage(message);
        view.setMultiline(multiline);
        view.setInitialInput(initialInput);
        modalPresenter.setView(view);
        if(showCancelButton) {
            modalPresenter.setEscapeButton(DialogButton.CANCEL);
        }
        modalPresenter.setPrimaryButton(DialogButton.OK);
        modalPresenter.setPrimaryButtonFocusedOnShow(false);
        modalPresenter.setButtonHandler(DialogButton.OK, closer -> {
            closer.closeModal();
            handler.handleAcceptInput(view.getInputValue());
        });
        modalManager.showModal(modalPresenter);
        Scheduler.get()
                 .scheduleDeferred(() -> {
                     view.getInitialFocusable().ifPresent(HasRequestFocus::requestFocus);
                 });
    }
}
