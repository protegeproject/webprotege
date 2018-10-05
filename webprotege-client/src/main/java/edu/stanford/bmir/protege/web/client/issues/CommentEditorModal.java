package edu.stanford.bmir.protege.web.client.issues;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2018
 */
public class CommentEditorModal {

    @Nonnull
    private final ModalManager modalManager;

    @Nonnull
    private final CommentEditorView view;

    @Nonnull
    private final Messages messages;

    @Inject
    public CommentEditorModal(@Nonnull ModalManager modalManager,
                              @Nonnull CommentEditorView view,
                              @Nonnull Messages messages) {
        this.modalManager = checkNotNull(modalManager);
        this.view = checkNotNull(view);
        this.messages = checkNotNull(messages);
    }

    public void showModal(@Nonnull String initialBody, @Nonnull Consumer<String> acceptBodyConsumer) {
        ModalPresenter presenter = modalManager.createPresenter();
        presenter.setTitle(messages.editComment());
        view.setBody(initialBody);
        presenter.setView(view);
        presenter.setEscapeButton(DialogButton.CANCEL);
        presenter.setPrimaryButton(DialogButton.OK);
        presenter.setButtonHandler(DialogButton.OK, closer -> {
            closer.closeModal();
            String body = view.getBody();
            acceptBodyConsumer.accept(body);
        });
        modalManager.showModal(presenter);
    }
}
