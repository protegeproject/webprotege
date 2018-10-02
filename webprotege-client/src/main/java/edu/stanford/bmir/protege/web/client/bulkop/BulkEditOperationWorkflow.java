package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.shared.SimpleEventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalCloser;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBoxHandler;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class BulkEditOperationWorkflow {

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final BulkEditOperationPresenter presenter;

    @Nonnull
    private final BulkEditOperationViewContainer viewContainer;

    @Nonnull
    private final ImmutableSet<OWLEntityData> entities;

    @Nonnull
    private final ModalPresenter modalPresenter;

    @AutoFactory
    @Inject
    public BulkEditOperationWorkflow(@Provided @Nonnull DispatchServiceManager dispatch,
                                     @Provided @Nonnull BulkEditOperationViewContainer viewContainer,
                                     @Nonnull BulkEditOperationPresenter presenter,
                                     @Nonnull ImmutableSet<OWLEntityData> entities,
                                     @Provided @Nonnull ModalPresenter modalPresenter) {
        this.dispatch = checkNotNull(dispatch);
        this.presenter = checkNotNull(presenter);
        this.viewContainer = checkNotNull(viewContainer);
        this.entities = checkNotNull(entities);
        this.modalPresenter = checkNotNull(modalPresenter);
    }

    public void start() {
        WebProtegeEventBus eventBus = new WebProtegeEventBus(new SimpleEventBus());
        List<DialogButton> dialogButtons = new ArrayList<>();

        modalPresenter.setTitle(presenter.getTitle());
        modalPresenter.addEscapeButton(DialogButton.CANCEL);
        DialogButton execButton = DialogButton.get(presenter.getExecuteButtonText());
        modalPresenter.setPrimaryButton(execButton);
        modalPresenter.setButtonHandler(execButton, this::handleExecute);
        modalPresenter.show(container -> presenter.start(container, eventBus));
    }

    private void handleExecute(ModalCloser closer) {
        if(!presenter.isDataWellFormed()) {
            presenter.displayErrorMessage();
        }
        else {
            ImmutableSet<OWLEntity> rawEntities = entities.stream()
                    .map(OWLEntityData::getEntity)
                    .collect(toImmutableSet());
            InputBox.showDialog(presenter.getTitle() + " Commit Message",
                                true,
                                presenter.getDefaultCommitMessage(entities),
                                input -> {
                                    presenter.createAction(rawEntities, input)
                                            .ifPresent(this::executeAction);
                                    closer.closeModal();
                                });

        }
    }

    private void executeAction(@Nonnull Action<?> action) {
        dispatch.execute(action,
                         result -> {});
    }

}
