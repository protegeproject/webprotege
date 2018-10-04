package edu.stanford.bmir.protege.web.client.library.modal;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 2018
 */
public interface ModalButtonHandler {

    /**
     * Handle the activaton of the button.
     * @param closer The closer that should be used to dismiss the modal.  The modal is not dismissed
     *               automatically.
     */
    void handleModalButton(@Nonnull ModalCloser closer);
}
