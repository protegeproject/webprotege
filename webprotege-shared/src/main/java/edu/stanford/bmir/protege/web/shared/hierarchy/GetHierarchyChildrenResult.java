package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.protege.gwt.graphtree.shared.graph.SuccessorMap;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class GetHierarchyChildrenResult implements Result {

    private SuccessorMap<EntityNode> children;

    public GetHierarchyChildrenResult(SuccessorMap<EntityNode> children) {
        this.children = children;
    }

    @GwtSerializationConstructor
    private GetHierarchyChildrenResult() {
    }

    public SuccessorMap<EntityNode> getChildren() {
        return children;
    }
}
