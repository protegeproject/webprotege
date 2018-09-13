package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.google.common.collect.ImmutableMultimap;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import edu.stanford.protege.gwt.graphtree.shared.graph.SuccessorMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.Serializable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class GetHierarchyChildrenResult implements Result {

    @Nullable
    private GraphNode<EntityNode> parent;

    private Page<GraphNode<EntityNode>> children;

    public GetHierarchyChildrenResult(@Nonnull GraphNode<EntityNode> parent,
                                      @Nonnull Page<GraphNode<EntityNode>> children) {
        this.parent = checkNotNull(parent);
        this.children = checkNotNull(children);
    }

    public GetHierarchyChildrenResult() {
        parent = null;
        children = Page.emptyPage();
    }

    @Nonnull
    public Page<GraphNode<EntityNode>> getChildren() {
        return children;
    }

    public SuccessorMap<EntityNode> getSuccessorMap() {
        if(parent == null) {
            return SuccessorMap.<EntityNode>builder().build();
        }
        SuccessorMap.Builder<EntityNode> builder = SuccessorMap.builder();
        children.getPageElements().forEach(child -> builder.add(parent, child));
        return builder.build();
    }
}
