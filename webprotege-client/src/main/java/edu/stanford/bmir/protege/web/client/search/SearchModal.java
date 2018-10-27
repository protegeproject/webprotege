package edu.stanford.bmir.protege.web.client.search;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2018
 */
public class SearchModal {

    @Nonnull
    private final ModalManager modalManager;

    @Nonnull
    private final SearchPresenter searchPresenter;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final SelectionModel selectionModel;

    @Nonnull
    private String title;

    @Inject
    public SearchModal(@Nonnull ModalManager modalManager, @Nonnull SearchPresenter searchPresenter, @Nonnull Messages messages, @Nonnull SelectionModel selectionModel) {
        this.modalManager = modalManager;
        this.searchPresenter = searchPresenter;
        this.messages = messages;
        this.selectionModel = selectionModel;
        title = messages.search();
    }

    public void setEntityTypes(EntityType<?>... entityTypes) {
        searchPresenter.setEntityTypes(entityTypes);
        if(entityTypes.length == 1) {
            title = messages.searchFor(entityTypes[0].getPrintName());
        }
    }

    public void showModal() {
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle(title);
        modalPresenter.setView(searchPresenter.getView());
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setPrimaryButton(DialogButton.SELECT);
        modalPresenter.setButtonHandler(DialogButton.SELECT, closer -> {
            closer.closeModal();
            selectChosenEntity();
        });
        searchPresenter.start();
        searchPresenter.setSearchResultChosenHandler(result -> modalPresenter.accept());
        modalManager.showModal(modalPresenter);
    }

    private void selectChosenEntity() {
        searchPresenter.getSelectedSearchResult()
                .ifPresent(sel -> selectionModel.setSelection(sel.getEntity()));
    }
}
