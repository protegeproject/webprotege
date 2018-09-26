package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.shared.SimpleEventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

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
    private final ImmutableSet<OWLEntity> entities;

    @AutoFactory
    @Inject
    public BulkEditOperationWorkflow(@Provided @Nonnull DispatchServiceManager dispatch,
                                     @Provided @Nonnull BulkEditOperationViewContainer viewContainer,
                                     @Nonnull BulkEditOperationPresenter presenter,
                                     @Nonnull ImmutableSet<OWLEntity> entities) {
        this.dispatch = checkNotNull(dispatch);
        this.presenter = checkNotNull(presenter);
        this.viewContainer = checkNotNull(viewContainer);
        this.entities = checkNotNull(entities);
    }

    public void start() {
        WebProtegeEventBus eventBus = new WebProtegeEventBus(new SimpleEventBus());
        List<DialogButton> dialogButtons = new ArrayList<>();
        DialogButton escapeButton = DialogButton.CANCEL;
        dialogButtons.add(escapeButton);
        DialogButton executeButton = DialogButton.get(presenter.getExecuteButtonText());
        dialogButtons.add(executeButton);
        viewContainer.setHelpText(presenter.getHelpMessage());
        presenter.start(viewContainer.getContainer(), eventBus);
        BulkEditingOperationDialogController controller = new BulkEditingOperationDialogController(
                presenter.getTitle(),
                dialogButtons,
                executeButton,
                escapeButton,
                viewContainer
        );
        controller.setDialogButtonHandler(executeButton, this::handleExecute);
        WebProtegeDialog.showDialog(controller);
    }

    private void handleExecute(Void data, WebProtegeDialogCloser closer) {
        if(!presenter.isDataWellFormed()) {
            presenter.displayErrorMessage();
        }
        else {
            presenter.createAction(entities).ifPresent(this::executeAction);
            closer.hide();
        }
    }

    private void executeAction(@Nonnull Action<?> action) {
        dispatch.execute(action,
                         result -> {});
    }

}
