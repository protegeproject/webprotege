package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.mansyntax.render.DeprecatedEntityChecker;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyChildrenAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyChildrenResult;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import edu.stanford.protege.gwt.graphtree.shared.graph.SuccessorMap;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;
import static java.util.Comparator.comparing;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class GetClassHierarchyChildrenActionHandler extends AbstractHasProjectActionHandler<GetHierarchyChildrenAction, GetHierarchyChildrenResult> {

    @Nonnull
    private final HierarchyProviderMapper hierarchyProviderMapper;

    @Nonnull
    private final DeprecatedEntityChecker deprecatedEntityChecker;

    @Nonnull
    private EntityHierarchyNodeRenderer renderer;

    @Inject
    public GetClassHierarchyChildrenActionHandler(@Nonnull AccessManager accessManager,
                                                  @Nonnull HierarchyProviderMapper hierarchyProviderMapper,
                                                  @Nonnull DeprecatedEntityChecker deprecatedEntityChecker,
                                                  @Nonnull EntityHierarchyNodeRenderer renderer) {
        super(accessManager);
        this.hierarchyProviderMapper = hierarchyProviderMapper;
        this.deprecatedEntityChecker = deprecatedEntityChecker;
        this.renderer = renderer;
    }

    @Override
    public Class<GetHierarchyChildrenAction> getActionClass() {
        return GetHierarchyChildrenAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    public GetHierarchyChildrenResult execute(GetHierarchyChildrenAction action, ExecutionContext executionContext) {
        HierarchyId hierarchyId = action.getHierarchyId();
        Optional<HierarchyProvider<OWLEntity>> hierarchyProvider = hierarchyProviderMapper.getHierarchyProvider(hierarchyId);
        if(!hierarchyProvider.isPresent()) {
            return emptyResult();
        }
        OWLEntity entity = action.getEntity();
        GraphNode<EntityHierarchyNode> parentNode = toGraphNode(toEntityHierarchyNode(entity, action.getProjectId(), executionContext.getUserId()), hierarchyProvider.get());
        SuccessorMap.Builder<EntityHierarchyNode> successorMap = SuccessorMap.builder();
        hierarchyProvider.get().getChildren(entity).stream()
                              .filter(childEntity -> !deprecatedEntityChecker.isDeprecated(childEntity))
                              .map(childEntity -> toEntityHierarchyNode(childEntity, action.getProjectId(), executionContext.getUserId()))
                              .map(node -> new GraphNode<>(node, hierarchyProvider.get().getChildren(node.getEntity()).isEmpty()))
                              .forEach(node -> successorMap.add(parentNode, node));
        return new GetHierarchyChildrenResult(successorMap.sort(comparing(GraphNode::getUserObject)).build());
    }

    static GetHierarchyChildrenResult emptyResult() {
        return new GetHierarchyChildrenResult(SuccessorMap.<EntityHierarchyNode>builder().build());
    }

    private GraphNode<EntityHierarchyNode> toGraphNode(EntityHierarchyNode node, HierarchyProvider<OWLEntity> hierarchyProvider) {
        return new GraphNode<>(
                node,
                hierarchyProvider.getChildren(node.getEntity()).isEmpty());
    }

    private EntityHierarchyNode toEntityHierarchyNode(OWLEntity entity, ProjectId projectId, UserId userId) {
        return renderer.render(entity, userId);
    }
}
