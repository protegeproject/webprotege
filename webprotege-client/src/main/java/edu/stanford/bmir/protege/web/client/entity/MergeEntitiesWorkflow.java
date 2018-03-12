package edu.stanford.bmir.protege.web.client.entity;

import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.EntitySelectionChangedEvent;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CANCEL;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.NO;
import static edu.stanford.bmir.protege.web.client.library.msgbox.MessageStyle.MESSAGE;
import static edu.stanford.bmir.protege.web.client.library.msgbox.MessageStyle.QUESTION;
import static edu.stanford.bmir.protege.web.shared.entity.MergeEntitiesAction.mergeEntities;
import static edu.stanford.bmir.protege.web.shared.entity.MergedEntityTreatment.DELETE_MERGED_ENTITY;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2018
 */
public class MergeEntitiesWorkflow {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final SelectionModel selectionModel;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private HandlerRegistration selectionHandlerRegistration = () -> {};

    @Nonnull
    private Optional<OWLEntity> sourceEntity = Optional.empty();

    private boolean inMerge = false;

    @Inject
    public MergeEntitiesWorkflow(@Nonnull ProjectId projectId,
                                 @Nonnull SelectionModel selectionModel,
                                 @Nonnull DispatchServiceManager dispatchServiceManager,
                                 @Nonnull Messages messages) {
        this.projectId = checkNotNull(projectId);
        this.messages = checkNotNull(messages);
        this.selectionModel = checkNotNull(selectionModel);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    public void start() {
        selectionHandlerRegistration = selectionModel.addSelectionChangedHandler(this::handleSelectionChanged);
        sourceEntity = selectionModel.getSelection();
        startTargetSelection();
    }

    private void startTargetSelection() {
        String typeName = sourceEntity.map(e -> e.getEntityType().getPrintName()).orElse("Entity");
        String typeNameL = typeName.toLowerCase();
        String message = messages.merge_description(typeNameL);
        MessageBox.showConfirmBox(MESSAGE,
                                  messages.merge_mergeEntity(typeName),
                                  message,
                                  CANCEL,
                                  this::end,
                                  DialogButton.get(messages.merge_selectEntityToMergeInto(typeNameL)),
                                  // We don't do anything apart from wait for the selection to change
                                  () -> {},
                                  CANCEL);
    }

    private void handleSelectionChanged(EntitySelectionChangedEvent event) {
        if(inMerge) {
            return;
        }
        inMerge = true;
        Optional<OWLEntity> sel = selectionModel.getSelection();
        if (sel.equals(sourceEntity)) {
            startTargetSelection();
        }
        else {
            String typePluralName = sourceEntity.map(e -> e.getEntityType().getPluralPrintName()).orElse("Entities");
            String mergeEntitiesMsg = messages.merge_mergeEntities(typePluralName);
            MessageBox.showConfirmBox(QUESTION,
                                      mergeEntitiesMsg,
                                      messages.merge_confirmMergeMessage(),
                                      NO,
                                      this::end,
                                      DialogButton.get(mergeEntitiesMsg),
                                      this::performMerge,
                                      NO);
        }
    }



    private void performMerge() {
        Optional<OWLEntity> targetEntity = selectionModel.getSelection();
        if (!targetEntity.isPresent()) {
            return;
        }
        if (!sourceEntity.isPresent()) {
            return;
        }
        dispatchServiceManager.execute(mergeEntities(projectId,
                                                     sourceEntity.get(),
                                                     targetEntity.get(),
                                                     DELETE_MERGED_ENTITY),
                                       result -> {});
        end();
    }

    private void end() {
        selectionHandlerRegistration.removeHandler();
        inMerge = false;
    }
}
