package edu.stanford.bmir.protege.web.client.tag;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Mar 2018
 */
public class EditEntityTagsUiAction extends AbstractUiAction {

    @Nonnull
    private final SelectionModel selectionModel;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final ModalManager modalManager;

    @Nonnull
    private final EntityTagsSelectorPresenter presenter;

    @Inject
    public EditEntityTagsUiAction(@Nonnull SelectionModel selectionModel,
                                  @Nonnull Messages messages,
                                  @Nonnull ModalManager modalManager,
                                  @Nonnull EntityTagsSelectorPresenter presenter) {
        super(messages.tags_edit());
        this.selectionModel = selectionModel;
        this.messages = messages;
        this.modalManager = modalManager;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        selectionModel.getSelection().ifPresent(this::showDialog);

    }

    private void showDialog(OWLEntity entity) {
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle(messages.tags_entityTags());
        modalPresenter.setView(presenter.getView());
        presenter.start(entity);
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setPrimaryButton(DialogButton.OK);
        modalPresenter.setButtonHandler(DialogButton.OK, closer -> {
            closer.closeModal();
            presenter.saveSelectedTags();
        });
        modalManager.showModal(modalPresenter);
    }
}
