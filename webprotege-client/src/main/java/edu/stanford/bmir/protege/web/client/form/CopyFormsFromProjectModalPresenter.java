package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalCloser;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.form.FormId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-13
 */
public class CopyFormsFromProjectModalPresenter {

    interface FormsCopiedHandler {
        void handleFormsCopied();
    }

    @Nonnull
    private final ModalManager modalManager;

    @Nonnull
    private final CopyFormsFromProjectPresenter copyFormsFromProjectPresenter;

    @Nonnull
    private final FormsMessages formsMessages;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private FormsCopiedHandler formsCopiedHandler = () -> {};

    @Inject
    public CopyFormsFromProjectModalPresenter(@Nonnull ModalManager modalManager,
                                              @Nonnull CopyFormsFromProjectPresenter copyFormsFromProjectPresenter,
                                              @Nonnull FormsMessages formsMessages,
                                              @Nonnull MessageBox messageBox) {
        this.modalManager = modalManager;
        this.copyFormsFromProjectPresenter = copyFormsFromProjectPresenter;
        this.formsMessages = formsMessages;
        this.messageBox = messageBox;
    }



    public void show() {
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle(formsMessages.copyFormsFromProject_title());
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        DialogButton primaryButton = DialogButton.get(formsMessages.copyFormsFromProject_title());
        modalPresenter.setPrimaryButton(primaryButton);
        modalPresenter.setPrimaryButtonFocusedOnShow(false);
        modalPresenter.setButtonHandler(primaryButton, this::handleCopyForms);
        copyFormsFromProjectPresenter.start(modalPresenter::setView);
        modalManager.showModal(modalPresenter);
    }

    public void setFormsCopiedHandler(@Nonnull FormsCopiedHandler formsCopiedHandler) {
        this.formsCopiedHandler = checkNotNull(formsCopiedHandler);
    }

    public void handleCopyForms(ModalCloser closer) {
        ImmutableList<FormId> selectedForms = copyFormsFromProjectPresenter.getSelectedFormIds();
        if(selectedForms.isEmpty()) {
            messageBox.showAlert(formsMessages.noFormSelected(),
                                 formsMessages.noFormSelected_description());
        }
        else {
            copyFormsFromProjectPresenter.copySelectedForms(() -> {
                formsCopiedHandler.handleFormsCopied();
                closer.closeModal();
            });
        }
    }
}
