package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.shared.graph.*;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonList;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
@ProjectSingleton
public class EntityHierarchyModel implements GraphModel<EntityHierarchyNode, OWLEntity> {

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final ProjectId projectId;

    private final List<GraphModelChangedHandler<EntityHierarchyNode>> handlers = new ArrayList<>();

    private Map<OWLEntity, EntityHierarchyNode> nodeCache = new HashMap<>();


    @Inject
    public EntityHierarchyModel(@Nonnull DispatchServiceManager dispatchServiceManager,
                                @Nonnull ProjectId projectId) {
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.projectId = checkNotNull(projectId);
    }

    public Optional<EntityHierarchyNode> getHierarchyNode(@Nonnull OWLEntity entity) {
        return Optional.ofNullable(nodeCache.get(entity));
    }

    public void updateNode(@Nonnull EntityHierarchyNode node) {
        EntityHierarchyNode currentNode = nodeCache.get(node.getEntity());
        if(Objects.equals(currentNode, node)) {
            return;
        }
        GWT.log("[EntityHierarchyModel] Updating node: " + node);
        nodeCache.put(node.getEntity(), node);
        handlers.forEach(handler -> {
            GWT.log("[EntityHierarchyModel] Dispatching events to handler: " + handler);
            handler.handleGraphModelChanged(new GraphModelChangedEvent<>(
                    singletonList(new UpdateUserObject<>(node)
                    )));
        });
    }

    @Override
    public void getRootNodes(GetRootNodesCallback<EntityHierarchyNode> callback) {
        dispatchServiceManager.execute(new GetHierarchyRootsAction(projectId), result -> {
            result.getRootNodes().stream()
                  .map(GraphNode::getUserObject)
                  .forEach(node -> nodeCache.put(node.getEntity(), node));
            callback.handleRootNodes(result.getRootNodes());
        });

    }

    @Override
    public void getSuccessorNodes(@Nonnull OWLEntity parent,
                                  @Nonnull GetSuccessorNodesCallback<EntityHierarchyNode> callback) {
        dispatchServiceManager.execute(new GetHierarchyChildrenAction(projectId, parent),
                                       result -> {
                                           result.getChildren().getSuccessors().stream()
                                                 .map(GraphNode::getUserObject)
                                                 .peek(hn -> GWT.log("Cached hierarchy node: " + hn))
                                                 .forEach(hn -> nodeCache.put(hn.getEntity(), hn));
                                           callback.handleSuccessorNodes(result.getChildren());
                                       });
    }

    @Override
    public void getPathsFromRootNodes(@Nonnull OWLEntity node,
                                      @Nonnull GetPathsBetweenNodesCallback<EntityHierarchyNode> callback) {
        dispatchServiceManager.execute(new GetHierarchyPathsToRootAction(projectId, node),
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
}
