package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalCloser;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
    private final CommitMessageInputView commitMessageInputView;

    @Nonnull
    private final ModalManager modalManager;

    @AutoFactory
    @Inject
    public BulkEditOperationWorkflow(@Provided @Nonnull DispatchServiceManager dispatch,
                                     @Provided @Nonnull BulkEditOperationViewContainer viewContainer,
                                     @Nonnull BulkEditOperationPresenter presenter,
                                     @Nonnull ImmutableSet<OWLEntityData> entities,
                                     @Provided @Nonnull CommitMessageInputView commitMessageInputView,
                                     @Provided @Nonnull ModalManager modalManager) {
        this.dispatch = checkNotNull(dispatch);
        this.presenter = checkNotNull(presenter);
        this.viewContainer = checkNotNull(viewContainer);
        this.entities = checkNotNull(entities);
        this.commitMessageInputView = checkNotNull(commitMessageInputView);
        this.modalManager = checkNotNull(modalManager);
    }

    public void start() {
        WebProtegeEventBus eventBus = new WebProtegeEventBus(new SimpleEventBus());
        List<DialogButton> dialogButtons = new ArrayList<>();

        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle(presenter.getTitle());
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        DialogButton execButton = DialogButton.get(presenter.getExecuteButtonText());
        modalPresenter.setPrimaryButton(execButton);
        modalPresenter.setButtonHandler(execButton, this::handleExecute);
        SimplePanel content = new SimplePanel();
        modalPresenter.setView(content);
        presenter.start(content, eventBus);
        modalManager.showModal(modalPresenter);
    }

    private void handleExecute(ModalCloser closer) {
        if (!presenter.isDataWellFormed()) {
            presenter.displayErrorMessage();
        }
        else {
            ModalPresenter commitMsgPresenter = getCommitMessagePresenter(closer, entities);
            modalManager.showModal(commitMsgPresenter);
        }
    }

    private ModalPresenter getCommitMessagePresenter(ModalCloser mainCloser, ImmutableSet<OWLEntityData> entities) {
        ModalPresenter commitMsgPresenter =  modalManager.createPresenter();
        commitMsgPresenter.setTitle(presenter.getTitle() + " Commit Message");
        commitMsgPresenter.setView(commitMessageInputView);
        commitMsgPresenter.setEscapeButton(DialogButton.CANCEL);
        DialogButton continueButton = DialogButton.get("Continue");
        commitMsgPresenter.setPrimaryButton(continueButton);
        commitMsgPresenter.setButtonHandler(continueButton, msgCloser -> {
            ImmutableSet<OWLEntity> rawEntities = entities.stream().map(OWLEntityData::getEntity).collect(toImmutableSet());
            presenter.createAction(rawEntities, commitMessageInputView.getCommitMessage())
                    .ifPresent(this::executeAction);
            msgCloser.closeModal();
            mainCloser.closeModal();
        });
        commitMessageInputView.setDefaultCommitMessage(presenter.getDefaultCommitMessage(entities));
        return commitMsgPresenter;
    }

    private void executeAction(@Nonnull Action<?> action) {
        dispatch.execute(action,
                         result -> {
                         });
    }

}
