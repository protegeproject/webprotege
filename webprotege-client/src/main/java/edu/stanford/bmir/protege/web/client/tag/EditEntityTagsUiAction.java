package edu.stanford.bmir.protege.web.client.tag;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Mar 2018
 */
public class EditEntityTagsUiAction extends AbstractUiAction {

    @Nonnull
    private final Provider<EntityTagsDialogController> controllerProvider;

    @Nonnull
    private SelectionModel selectionModel;

    @Nonnull
    private Messages messages;

    @Inject
    public EditEntityTagsUiAction(@Nonnull Provider<EntityTagsDialogController> controllerProvider,
                                  @Nonnull SelectionModel selectionModel,
                                  @Nonnull Messages messages) {
        super(messages.tags_edit());
        this.controllerProvider = checkNotNull(controllerProvider);
        this.selectionModel = selectionModel;
    }

    @Override
    public void execute() {
        selectionModel.getSelection().ifPresent(entity -> {
            EntityTagsDialogController controller = controllerProvider.get();
            controller.start(entity);
            WebProtegeDialog.showDialog(controller);
        });

    }
}
