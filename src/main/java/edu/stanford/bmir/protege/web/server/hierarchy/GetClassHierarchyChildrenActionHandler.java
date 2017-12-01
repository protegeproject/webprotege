package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.mansyntax.render.DeprecatedEntityChecker;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyChildrenAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyChildrenResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import edu.stanford.protege.gwt.graphtree.shared.graph.SuccessorMap;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static java.util.Comparator.comparing;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class GetClassHierarchyChildrenActionHandler extends AbstractHasProjectActionHandler<GetHierarchyChildrenAction, GetHierarchyChildrenResult> {

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;

    @Nonnull
    private final DeprecatedEntityChecker deprecatedEntityChecker;

    @Nonnull
    private OWLEntityRenderingGenerator hierarchyNodeRenderer;

    @Inject
    public GetClassHierarchyChildrenActionHandler(@Nonnull AccessManager accessManager,
                                                  @Nonnull ClassHierarchyProvider classHierarchyProvider,
                                                  @Nonnull DeprecatedEntityChecker deprecatedEntityChecker, @Nonnull OWLEntityRenderingGenerator hierarchyNodeRenderer) {
        super(accessManager);
        this.classHierarchyProvider = classHierarchyProvider;
        this.deprecatedEntityChecker = deprecatedEntityChecker;
        this.hierarchyNodeRenderer = hierarchyNodeRenderer;
    }

    @Override
    public Class<GetHierarchyChildrenAction> getActionClass() {
        return GetHierarchyChildrenAction.class;
    }

    @Override
    public GetHierarchyChildrenResult execute(GetHierarchyChildrenAction action, ExecutionContext executionContext) {
        if(!action.getEntity().isOWLClass()) {
            return new GetHierarchyChildrenResult(SuccessorMap.<EntityHierarchyNode>builder().build());
        }
        OWLClass theCls = action.getEntity().asOWLClass();
        GraphNode<EntityHierarchyNode> parentNode = toGraphNode(toEntityHierarchyNode(theCls, action.getProjectId(), executionContext.getUserId()));
        SuccessorMap.Builder<EntityHierarchyNode> successorMap = SuccessorMap.builder();
        classHierarchyProvider.getChildren(theCls).stream()
                              .filter(cls -> !theCls.isOWLThing() || !deprecatedEntityChecker.isDeprecated(cls))
                              .map(cls -> toEntityHierarchyNode(cls, action.getProjectId(), executionContext.getUserId()))
                              .map(node -> new GraphNode<>(node, classHierarchyProvider.getChildren(node.getEntity().asOWLClass()).isEmpty()))
                              .forEach(node -> successorMap.add(parentNode, node));
        return new GetHierarchyChildrenResult(successorMap.sort(comparing(GraphNode::getUserObject)).build());
    }

    private GraphNode<EntityHierarchyNode> toGraphNode(EntityHierarchyNode node) {
        return new GraphNode<>(
                node,
                classHierarchyProvider.getChildren((OWLClass) node.getEntity()).isEmpty());
    }

    private EntityHierarchyNode toEntityHierarchyNode(OWLEntity entity, ProjectId projectId, UserId userId) {
        return hierarchyNodeRenderer.render(entity, projectId, userId);
    }
}
