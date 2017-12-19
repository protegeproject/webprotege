package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 19 Dec 2017
 */
public class GraphNodeRenderer {

    @Nonnull
    private final EntityHierarchyNodeRenderer renderer;

    public GraphNodeRenderer(@Nonnull EntityHierarchyNodeRenderer renderer) {
        this.renderer = checkNotNull(renderer);
    }

    public GraphNode<EntityHierarchyNode> toGraphNode(@Nonnull OWLEntity entity, HierarchyProvider<OWLEntity> hierarchyProvider) {
        return new GraphNode<>(renderer.render(entity), hierarchyProvider.getChildren(entity).isEmpty());
    }
}
