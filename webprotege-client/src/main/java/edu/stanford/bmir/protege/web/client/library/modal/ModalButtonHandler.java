package edu.stanford.bmir.protege.web.client.library.modal;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 2018
 */
public interface ModalButtonHandler {

    void handleModalButton(@Nonnull ModalCloser closer);
}
