package edu.stanford.bmir.protege.web.client.hierarchy;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.entity.DeleteEntitiesAction;
import edu.stanford.bmir.protege.web.shared.entity.Entity;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CANCEL;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.DELETE;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.joining;

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
    public void start(@Nonnull TreeWidget<EntityNode, OWLEntity> treeWidget) {
        final Set<TreeNode<EntityNode>> currentSelection = treeWidget.getSelectedNodes();
        if(currentSelection.isEmpty()) {
            return;
        }
        Set<OWLEntityData> entities = currentSelection.stream()
                .map(TreeNode::getUserObject)
                .map(EntityNode::getEntityData)
                .collect(toImmutableSet());
        MessageBox.showConfirmBox(getDeleteConfirmationTitle(entities),
                                  getDeleteConfirmationMessage(entities),
                                  CANCEL, DELETE,
                                  () -> deleteEntity(entities, treeWidget),
                                  CANCEL);
    }

    private String getDeleteConfirmationTitle(@Nonnull Set<OWLEntityData> entities) {
        if(entities.size() == 1) {
            return entities.stream()
                    .findFirst()
                    .map(this::getDeleteConfirmationTitle)
                    .orElse("");
        }
        else {
            return entities.stream()
                    .findFirst()
                    .map(OWLEntityData::getEntity)
                    .map(OWLEntity::getEntityType)
                    .map(this::getDeleteConfirmationTitle)
                    .orElse("");
        }

    }

    private String getDeleteConfirmationTitle(@Nonnull OWLEntityData entity) {
        return messages.delete_entity_title(entity.getEntity().getEntityType().getPrintName().toLowerCase());
    }

    @Nonnull
    private String getDeleteConfirmationMessage(@Nonnull Set<OWLEntityData> entities) {
        String typeName = entities.stream()
                .findFirst()
                .map(OWLEntityData::getEntity)
                .map(OWLEntity::getEntityType)
                .map(type -> entities.size() > 1 ? type.getPluralPrintName() : type.getPrintName())
                .map(String::toLowerCase)
                .orElse("");
        String browserText = entities.stream()
                .map(OWLEntityData::getBrowserText)
                .limit(30)
                .collect(joining(", "));
        return messages.delete_entity_msg(typeName,
                                          browserText);
    }

    @Nonnull
    private String getDeleteConfirmationTitle(@Nonnull EntityType<?> entityType) {
        return messages.delete_entity_title(entityType.getPluralPrintName().toLowerCase());
    }

    private void deleteEntity(@Nonnull Set<OWLEntityData> entities,
                              @Nonnull TreeWidget<EntityNode, OWLEntity> treeWidget) {
        treeWidget.moveSelectionDown();
        Set<OWLEntity> entitiesToDelete = entities.stream().map(OWLEntityData::getEntity).collect(toImmutableSet());
        dispatchServiceManager.execute(new DeleteEntitiesAction(projectId, entitiesToDelete), deleteEntityResult -> {});
    }

}
