package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class GetHierarchyPathsToRootResult implements Result {

    private Collection<Path<GraphNode<EntityHierarchyNode>>> paths;

    public GetHierarchyPathsToRootResult(Collection<Path<GraphNode<EntityHierarchyNode>>> paths) {
        this.paths = new ArrayList<>(paths);
    }

    @GwtSerializationConstructor
    private GetHierarchyPathsToRootResult() {
    }

    public Collection<Path<GraphNode<EntityHierarchyNode>>> getPaths() {
        return new ArrayList<>(paths);
    }
}
