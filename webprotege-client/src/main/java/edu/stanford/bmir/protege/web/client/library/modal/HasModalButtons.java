package edu.stanford.bmir.protege.web.client.library.modal;

import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 2018
 */
public interface HasModalButtons {

    void addEscapeButton(DialogButton button, @Nonnull ModalButtonHandler handler);

    void addButton(@Nonnull DialogButton button, @Nonnull ModalButtonHandler handler);

    void addPrimaryButton(@Nonnull DialogButton button, @Nonnull ModalButtonHandler handler);
}
