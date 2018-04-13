package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.mansyntax.render.DeprecatedEntityChecker;
import edu.stanford.bmir.protege.web.server.util.AlphaNumericStringComparator;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyChildrenAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyChildrenResult;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import edu.stanford.protege.gwt.graphtree.shared.graph.SuccessorMap;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.server.util.AlphaNumericStringComparator.alphaNumerically;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;
import static java.util.Comparator.comparing;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class GetEntityHierarchyChildrenActionHandler extends AbstractProjectActionHandler<GetHierarchyChildrenAction, GetHierarchyChildrenResult> {

    @Nonnull
    private final HierarchyProviderMapper hierarchyProviderMapper;

    @Nonnull
    private final DeprecatedEntityChecker deprecatedEntityChecker;

    @Nonnull
    private final GraphNodeRenderer nodeRenderer;

    @Inject
    public GetEntityHierarchyChildrenActionHandler(@Nonnull AccessManager accessManager,
                                                   @Nonnull HierarchyProviderMapper hierarchyProviderMapper,
                                                   @Nonnull DeprecatedEntityChecker deprecatedEntityChecker,
                                                   @Nonnull GraphNodeRenderer nodeRenderer) {
        super(accessManager);
        this.hierarchyProviderMapper = hierarchyProviderMapper;
        this.deprecatedEntityChecker = deprecatedEntityChecker;
        this.nodeRenderer = nodeRenderer;
    }

    static GetHierarchyChildrenResult emptyResult() {
        return new GetHierarchyChildrenResult(SuccessorMap.<EntityHierarchyNode>builder().build());
    }

    @Nonnull
    @Override
    public Class<GetHierarchyChildrenAction> getActionClass() {
        return GetHierarchyChildrenAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetHierarchyChildrenResult execute(@Nonnull GetHierarchyChildrenAction action, @Nonnull ExecutionContext executionContext) {
        HierarchyId hierarchyId = action.getHierarchyId();
        Optional<HierarchyProvider<OWLEntity>> hierarchyProvider = hierarchyProviderMapper.getHierarchyProvider(hierarchyId);
        if (!hierarchyProvider.isPresent()) {
            return emptyResult();
        }
        OWLEntity parent = action.getEntity();
        GraphNode<EntityHierarchyNode> parentNode = nodeRenderer.toGraphNode(parent, hierarchyProvider.get());
        SuccessorMap.Builder<EntityHierarchyNode> successorMap = SuccessorMap.builder();
        hierarchyProvider.get().getChildren(parent).stream()
                         // Filter out deprecated entities that are displayed under owl:Thing, owl:topObjectProperty
                         // owl:topDataProperty
                         .filter(child -> isNotDeprecatedTopLevelEntity(parent, child))
                         .map(child -> nodeRenderer.toGraphNode(child, hierarchyProvider.get()))
                         .forEach(childNode -> successorMap.add(parentNode, childNode));
        successorMap.sort(comparing(node -> node.getUserObject().getBrowserText(), alphaNumerically()));
        return new GetHierarchyChildrenResult(successorMap.build());
    }

    private boolean isNotDeprecatedTopLevelEntity(OWLEntity parent, OWLEntity child) {
        return !(parent.isTopEntity() && deprecatedEntityChecker.isDeprecated(child));
    }
}
