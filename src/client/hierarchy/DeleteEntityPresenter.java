package edu.stanford.bmir.protege.web.client.hierarchy;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.entity.DeleteEntitiesAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CANCEL;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.DELETE;
import static java.util.Collections.singleton;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 6 Dec 2017
 */
public class DeleteEntityPresenter {

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public DeleteEntityPresenter(@Nonnull Messages messages,
                                 @Nonnull DispatchServiceManager dispatchServiceManager,
                                 @Nonnull ProjectId projectId) {
        this.messages = checkNotNull(messages);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.projectId = checkNotNull(projectId);
    }

    /**
     * Start the deletion of the entities represented by the selected nodes in the specified tree.
     * @param treeWidget The tree.
     */
    public void start(@Nonnull TreeWidget<EntityHierarchyNode, OWLEntity> treeWidget) {
        final Optional<EntityHierarchyNode> currentSelection = treeWidget.getFirstSelectedUserObject();
        currentSelection.ifPresent(sel -> {
            OWLEntity entity = sel.getEntity();
            String browserText = sel.getBrowserText();
            MessageBox.showConfirmBox(getDeleteConfirmationTitle(entity),
                                      getDeleteConfirmationMessage(entity, browserText),
                                      CANCEL, DELETE,
                                      () -> deleteEntity(entity, treeWidget),
                                      CANCEL);
        });
    }

    private String getDeleteConfirmationTitle(@Nonnull OWLEntity entity) {
        return messages.delete_entity_title(entity.getEntityType().getPrintName().toLowerCase());
    }

    @Nonnull
    private String getDeleteConfirmationMessage(@Nonnull OWLEntity entity,
                                                @Nonnull String browserText) {
        return messages.delete_entity_msg(entity.getEntityType()
                                                .getPrintName()
                                                .toLowerCase(),
                                          browserText);
    }

    private void deleteEntity(@Nonnull OWLEntity cls,
                              @Nonnull TreeWidget<EntityHierarchyNode, OWLEntity> treeWidget) {
        treeWidget.moveSelectionDown();
        dispatchServiceManager.execute(new DeleteEntitiesAction(projectId, singleton(cls)), deleteEntityResult -> {});
    }

}
