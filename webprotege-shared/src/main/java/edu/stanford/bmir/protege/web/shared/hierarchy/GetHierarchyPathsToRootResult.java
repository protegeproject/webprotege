package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class GetHierarchyPathsToRootResult implements Result {

    private Collection<Path<GraphNode<EntityNode>>> paths;

    public GetHierarchyPathsToRootResult(Collection<Path<GraphNode<EntityNode>>> paths) {
        this.paths = new ArrayList<>(paths);
    }

    @GwtSerializationConstructor
    private GetHierarchyPathsToRootResult() {
    }

    public Collection<Path<GraphNode<EntityNode>>> getPaths() {
        return new ArrayList<>(paths);
    }
}
