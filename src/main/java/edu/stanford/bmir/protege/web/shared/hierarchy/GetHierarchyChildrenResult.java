package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.protege.gwt.graphtree.shared.graph.SuccessorMap;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class GetHierarchyChildrenResult implements Result {

    private SuccessorMap<EntityHierarchyNode> children;

    public GetHierarchyChildrenResult(SuccessorMap<EntityHierarchyNode> children) {
        this.children = children;
    }

    @GwtSerializationConstructor
    private GetHierarchyChildrenResult() {
    }

    public SuccessorMap<EntityHierarchyNode> getChildren() {
        return children;
    }
}
