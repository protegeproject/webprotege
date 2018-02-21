package edu.stanford.bmir.protege.web.client.hierarchy;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateEntitiesInHierarchyAction;
import edu.stanford.bmir.protege.web.client.entity.CreateEntitiesDialogController;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 5 Dec 2017
 */
public class CreateEntityPresenter {

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private CreateEntitiesDialogController dialogController;

    @Inject
    public CreateEntityPresenter(@Nonnull DispatchServiceManager dispatchServiceManager,
                                 @Nonnull ProjectId projectId,
                                 @Nonnull CreateEntitiesDialogController dialogController) {
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.projectId = checkNotNull(projectId);
        this.dialogController = checkNotNull(dialogController);
    }

    public void createEntities(@Nonnull EntityType<?> entityType,
                               TreeWidget<EntityHierarchyNode, OWLEntity> treeWidget,
                               @Nonnull ActionFactory actionFactory) {
        dialogController.clear();
        dialogController.setEntityType(entityType);
        dialogController.setCreateEntityHandler(createEntityInfo -> {
            handleCreateEntities(createEntityInfo, actionFactory, treeWidget);
        });
        WebProtegeDialog.showDialog(dialogController);
    }

    private void handleCreateEntities(@Nonnull String enteredText,
                                      @Nonnull ActionFactory actionFactory,
                                      @Nonnull TreeWidget<EntityHierarchyNode, OWLEntity> treeWidget) {

        CreateEntitiesInHierarchyAction<?, ?> action = actionFactory.createAction(projectId,
                                                                                  enteredText);
        dispatchServiceManager.execute(action,
                                       result -> {
                                           result.getEntities().stream()
                                                 .findFirst()
                                                 .ifPresent(e -> {
                                                     treeWidget.getSelectedKeyPaths().stream()
                                                               .findFirst()
                                                               .ifPresent(parentPath -> {
                                                                   treeWidget.setSelected(parentPath.pathByAppending(e),
                                                                                          true, () -> {});
                                                                   treeWidget.revealTreeNodesForKey(e, RevealMode.REVEAL_FIRST);
                                                               });
                                                 });
                                       });

    }

    interface ActionFactory {
        CreateEntitiesInHierarchyAction<?, ?> createAction(
                @Nonnull ProjectId projectId,
                @Nonnull String createFromText);
    }
}
