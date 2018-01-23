package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.EntityHierarchyNodeUpdater;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.shared.graph.*;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyChangedEvent.ON_HIERARCHY_CHANGED;
import static edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId.CLASS_HIERARCHY;
import static java.util.Collections.singletonList;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class EntityHierarchyModel implements GraphModel<EntityHierarchyNode, OWLEntity> {

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityHierarchyNodeUpdater hierarchyNodeUpdater;

    private final List<GraphModelChangedHandler<EntityHierarchyNode>> handlers = new ArrayList<>();

    private final Map<OWLEntity, EntityHierarchyNode> nodeCache = new HashMap<>();

    private final SetMultimap<OWLEntity, OWLEntity> parent2ChildMap = HashMultimap.create();

    private final Set<OWLEntity> rootNodes = new HashSet<>();

    @Nonnull
    private HierarchyId hierarchyId = CLASS_HIERARCHY;

    @Inject
    public EntityHierarchyModel(@Nonnull DispatchServiceManager dispatchServiceManager,
                                @Nonnull ProjectId projectId,
                                @Nonnull EntityHierarchyNodeUpdater hierarchyNodeUpdater) {
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.projectId = checkNotNull(projectId);
        this.hierarchyNodeUpdater = checkNotNull(hierarchyNodeUpdater);
    }

    public void start(@Nonnull WebProtegeEventBus eventBus, @Nonnull HierarchyId hierarchyId) {
        this.hierarchyId = checkNotNull(hierarchyId);
        hierarchyNodeUpdater.start(eventBus, this);
        eventBus.addProjectEventHandler(projectId, ON_HIERARCHY_CHANGED, this::handleEntityHierarchyChanged);
    }

    private void handleEntityHierarchyChanged(EntityHierarchyChangedEvent event) {
        if(!event.getHierarchyId().equals(hierarchyId)) {
            return;
        }
        GraphModelChangeProcessor changeProcessor = new GraphModelChangeProcessor(parent2ChildMap, rootNodes);
        event.getChangeEvent().getChanges().forEach(evt -> evt.accept(changeProcessor));
        changeProcessor.getUpdatedNodes().forEach(node -> nodeCache.put(node.getEntity(), node));
        GraphModelChangedEvent<EntityHierarchyNode> graphModelChangedEvent = new GraphModelChangedEvent<>(changeProcessor.getChanges());
        if (!graphModelChangedEvent.getChanges().isEmpty()) {
            handlers.forEach(handler ->
                                     handler.handleGraphModelChanged(graphModelChangedEvent));
        }
    }

    public Optional<EntityHierarchyNode> getHierarchyNode(@Nonnull OWLEntity entity) {
        return Optional.ofNullable(nodeCache.get(entity));
    }

    public void updateNode(@Nonnull EntityHierarchyNode node) {
        EntityHierarchyNode currentNode = nodeCache.get(node.getEntity());
        if (node.equals(currentNode)) {
            return;
        }
        nodeCache.put(node.getEntity(), node);
        handlers.forEach(handler ->
                                 handler.handleGraphModelChanged(
                                         new GraphModelChangedEvent<>(
                                                 singletonList(new UpdateUserObject<>(node)))));
    }

    @Override
    public void getRootNodes(GetRootNodesCallback<EntityHierarchyNode> callback) {
        dispatchServiceManager.execute(new GetHierarchyRootsAction(projectId, hierarchyId), result -> {
            cacheRootNodes(result);
            callback.handleRootNodes(result.getRootNodes());
        });

    }

    private void cacheRootNodes(GetHierarchyRootsResult result) {
        result.getRootNodes().stream()
              .map(GraphNode::getUserObject)
              .forEach(node -> {
                  nodeCache.put(node.getEntity(), node);
                  rootNodes.add(node.getEntity());
              });
    }

    @Override
    public void getSuccessorNodes(@Nonnull OWLEntity parent,
                                  @Nonnull GetSuccessorNodesCallback<EntityHierarchyNode> callback) {
        dispatchServiceManager.execute(new GetHierarchyChildrenAction(projectId, parent, hierarchyId),
                                       result -> {
                                           cacheEdges(parent, result);
                                           callback.handleSuccessorNodes(result.getChildren());
                                       });
    }

    private void cacheEdges(@Nonnull OWLEntity parent, GetHierarchyChildrenResult result) {
        result.getChildren().getSuccessors().stream()
              .map(GraphNode::getUserObject)
              .forEach(node -> {
                  nodeCache.put(node.getEntity(), node);
                  parent2ChildMap.put(parent, node.getEntity());
              });
    }

    @Override
    public void getPathsFromRootNodes(@Nonnull OWLEntity node,
                                      @Nonnull GetPathsBetweenNodesCallback<EntityHierarchyNode> callback) {
        dispatchServiceManager.execute(new GetHierarchyPathsToRootAction(projectId, node, hierarchyId),
                                       result -> callback.handlePaths(result.getPaths()));
    }

    @Override
    public void getPathsBetweenNodes(@Nonnull OWLEntity nodeA,
                                     @Nonnull OWLEntity nodeB,
                                     @Nonnull GetPathsBetweenNodesCallback<EntityHierarchyNode> callback) {

    }

    @Override
    public HandlerRegistration addGraphModelHandler(GraphModelChangedHandler<EntityHierarchyNode> handler) {
        handlers.add(handler);
        return () -> handlers.remove(handler);
    }


    private static class GraphModelChangeProcessor implements GraphModelChangeVisitor<EntityHierarchyNode> {

        @Nonnull
        private final SetMultimap<OWLEntity, OWLEntity> parent2ChildMap;

        @Nonnull
        private final Set<OWLEntity> rootNodes;

        private final List<GraphModelChange<EntityHierarchyNode>> changes = new ArrayList<>();

        private final Set<EntityHierarchyNode> updatedNodes = new HashSet<>();

        public GraphModelChangeProcessor(@Nonnull SetMultimap<OWLEntity, OWLEntity> parent2ChildMap,
                                         @Nonnull Set<OWLEntity> rootNodes) {
            this.parent2ChildMap = parent2ChildMap;
            this.rootNodes = rootNodes;
        }

        public List<GraphModelChange<EntityHierarchyNode>> getChanges() {
            return changes;
        }

        public Set<EntityHierarchyNode> getUpdatedNodes() {
            return updatedNodes;
        }

        @Override
        public void visit(AddRootNode<EntityHierarchyNode> addRootNode) {
            EntityHierarchyNode entityHierarchyNode = addRootNode.getNode().getUserObject();
            if (rootNodes.add(entityHierarchyNode.getEntity())) {
                updatedNodes.add(entityHierarchyNode);
                changes.add(addRootNode);
            }
        }

        @Override
        public void visit(RemoveRootNode<EntityHierarchyNode> removeRootNode) {
            if (rootNodes.remove(removeRootNode.getNode().getUserObject().getEntity())) {
                changes.add(removeRootNode);
            }
        }

        @Override
        public void visit(AddEdge<EntityHierarchyNode> addEdge) {
            GraphEdge<EntityHierarchyNode> edge = addEdge.getEdge();
            EntityHierarchyNode successorEntityHierarchyNode = edge.getSuccessor().getUserObject();
            OWLEntity child = successorEntityHierarchyNode.getEntity();
            EntityHierarchyNode predecessorEntityHierarchyNode = edge.getPredecessor().getUserObject();
            OWLEntity parent = predecessorEntityHierarchyNode.getEntity();
            if (parent2ChildMap.put(parent, child)) {
                updatedNodes.add(predecessorEntityHierarchyNode);
                updatedNodes.add(successorEntityHierarchyNode);
                changes.add(addEdge);
            }
        }

        @Override
        public void visit(RemoveEdge<EntityHierarchyNode> removeEdge) {
            GraphEdge<EntityHierarchyNode> edge = removeEdge.getEdge();
            OWLEntity child = edge.getSuccessor().getUserObject().getEntity();
            OWLEntity parent = edge.getPredecessor().getUserObject().getEntity();
            if (parent2ChildMap.remove(parent, child)) {
                changes.add(removeEdge);
            }
        }

        @Override
        public void visit(UpdateUserObject<EntityHierarchyNode> updateUserObject) {
            // Don't handle this here
        }
    }
}
