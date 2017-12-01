package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyRootsAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyRootsResult;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static java.util.Collections.singletonList;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30 Nov 2017
 */
public class GetHierarchyRootsActionHandler extends AbstractHasProjectActionHandler<GetHierarchyRootsAction, GetHierarchyRootsResult> {

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;

    @Nonnull
    private final OWLEntityRenderingGenerator renderingGenerator;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public GetHierarchyRootsActionHandler(@Nonnull AccessManager accessManager, @Nonnull ClassHierarchyProvider classHierarchyProvider, @Nonnull OWLEntityRenderingGenerator renderingGenerator, @Nonnull OWLDataFactory dataFactory) {
        super(accessManager);
        this.classHierarchyProvider = classHierarchyProvider;
        this.renderingGenerator = renderingGenerator;
        this.dataFactory = dataFactory;
    }

    @Override
    public Class<GetHierarchyRootsAction> getActionClass() {
        return GetHierarchyRootsAction.class;
    }

    @Override
    public GetHierarchyRootsResult execute(GetHierarchyRootsAction action, ExecutionContext executionContext) {
        OWLClass owlThing = dataFactory.getOWLThing();
        EntityHierarchyNode rootNode = renderingGenerator.render(owlThing, action.getProjectId(), executionContext.getUserId());
        GraphNode<EntityHierarchyNode> graphNode = new GraphNode<>(rootNode, classHierarchyProvider.getChildren(owlThing).isEmpty());
        return new GetHierarchyRootsResult(singletonList(graphNode));
    }
}
