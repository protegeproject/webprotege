package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30 Nov 2017
 */
public class GetHierarchyRootsResult implements Result {

    private ImmutableList<GraphNode<EntityHierarchyNode>> rootNodes;

    public GetHierarchyRootsResult(List<GraphNode<EntityHierarchyNode>> rootNodes) {
        this.rootNodes = ImmutableList.copyOf(rootNodes);
    }

    @GwtSerializationConstructor
    private GetHierarchyRootsResult() {
    }

    @Nonnull
    public List<GraphNode<EntityHierarchyNode>> getRootNodes() {
        return rootNodes;
    }
}
