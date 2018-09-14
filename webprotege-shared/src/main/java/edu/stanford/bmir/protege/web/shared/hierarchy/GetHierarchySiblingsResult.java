package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Sep 2018
 */
public class GetHierarchySiblingsResult implements Result {

    private Page<GraphNode<EntityNode>> siblingsPage;

    public GetHierarchySiblingsResult(Page<GraphNode<EntityNode>> siblingsPage) {
        this.siblingsPage = checkNotNull(siblingsPage);
    }

    @GwtSerializationConstructor
    private GetHierarchySiblingsResult() {

    }

    public Page<GraphNode<EntityNode>> getSiblingsPage() {
        return siblingsPage;
    }
}
