package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.common.collect.ImmutableCollection;
import edu.stanford.bmir.protege.web.client.entity.CreateEntityPresenter.EntitiesCreatedHandler;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Sep 2018
 */
public class CreateEntitiesInHierarchyHandler<E extends OWLEntity> implements EntitiesCreatedHandler {

    @Nonnull
    private final TreeWidget<EntityNode, OWLEntity> treeWidget;

    public static <E extends OWLEntity> CreateEntitiesInHierarchyHandler<E> get(@Nonnull TreeWidget<EntityNode, OWLEntity> treeWidget) {
        return new CreateEntitiesInHierarchyHandler<>(treeWidget);
    }

    private CreateEntitiesInHierarchyHandler(@Nonnull TreeWidget<EntityNode, OWLEntity> treeWidget) {
        this.treeWidget = checkNotNull(treeWidget);
    }

    @Override
    public void handleEntitiesCreated(ImmutableCollection<EntityNode> entities) {
        entities.stream()
                .findFirst()
                .map(EntityNode::getEntity)
                .ifPresent(e -> {
                    treeWidget.getSelectedKeyPaths().stream()
                              .findFirst()
                              .ifPresent(parentPath -> {
                                  treeWidget.clearSelection();
                                  treeWidget.setSelected(parentPath.pathByAppending(e),
                                                         true, () -> {});
                                  treeWidget.revealTreeNodesForKey(e, RevealMode.REVEAL_FIRST);
                              });
                });
    }
}
