package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateEntitiesInHierarchyAction;
import edu.stanford.bmir.protege.web.client.entity.CreateEntityDialogController;
import edu.stanford.bmir.protege.web.client.entity.CreateEntityInfo;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 5 Dec 2017
 */
public class CreateEntityPresenter {

    @Nonnull
    private final Messages messages;
    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;
    @Nonnull
    private final ProjectId projectId;

    @Inject
    public CreateEntityPresenter(@Nonnull Messages messages,
                                 @Nonnull DispatchServiceManager dispatchServiceManager,
                                 @Nonnull ProjectId projectId) {
        this.messages = checkNotNull(messages);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.projectId = checkNotNull(projectId);
    }

    public void createEntities(@Nonnull EntityType<?> entityType,
                               TreeWidget<EntityHierarchyNode, OWLEntity> treeWidget,
                               @Nonnull ActionFactory actionFactory) {

        CreateEntityDialogController.CreateEntityHandler createEntityHandler = createEntityInfo ->
                handleCreateEntities(createEntityInfo, actionFactory, treeWidget);
        CreateEntityDialogController controller = new CreateEntityDialogController(checkNotNull(entityType),
                                                                                   createEntityHandler,
                                                                                   messages);
        WebProtegeDialog.showDialog(controller);
    }

    private void handleCreateEntities(@Nonnull CreateEntityInfo createEntityInfo,
                                      @Nonnull ActionFactory actionFactory,
                                      @Nonnull TreeWidget<EntityHierarchyNode, OWLEntity> treeWidget) {

        final Set<String> browserTexts = createEntityInfo.getBrowserTexts().stream()
                                                         .filter(browserText -> !browserText.trim().isEmpty())
                                                         .collect(Collectors.toSet());
        CreateEntitiesInHierarchyAction<?, ?> action = actionFactory.createAction(projectId,
                                                                                  ImmutableSet.copyOf(createEntityInfo.getBrowserTexts()));
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
                @Nonnull ImmutableSet<String> browserText);
    }
}
