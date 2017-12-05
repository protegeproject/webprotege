package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyPathsToRootAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyPathsToRootResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class GetHierarchyPathsToRootActionHandler extends AbstractHasProjectActionHandler<GetHierarchyPathsToRootAction, GetHierarchyPathsToRootResult> {

    @Nonnull
    private final HierarchyProviderMapper hierarchyProviderMapper;

    @Nonnull
    private final EntityHierarchyNodeRenderer renderer;

    @Inject
    public GetHierarchyPathsToRootActionHandler(@Nonnull AccessManager accessManager,
                                                @Nonnull HierarchyProviderMapper hierarchyProviderMapper, @Nonnull EntityHierarchyNodeRenderer renderer) {
        super(accessManager);
        this.hierarchyProviderMapper = hierarchyProviderMapper;
        this.renderer = renderer;
    }

    @Override
    public Class<GetHierarchyPathsToRootAction> getActionClass() {
        return GetHierarchyPathsToRootAction.class;
    }

    @Override
    public GetHierarchyPathsToRootResult execute(GetHierarchyPathsToRootAction action, ExecutionContext executionContext) {
        Optional<HierarchyProvider<OWLEntity>> hierarchyProvider = hierarchyProviderMapper.getHierarchyProvider(action.getHierarchyId());
        return hierarchyProvider.map(hp -> {
            Set<List<OWLEntity>> pathsToRoot = hp.getPathsToRoot(action.getEntity());
            List<Path<GraphNode<EntityHierarchyNode>>> result =
                    pathsToRoot.stream()
                               .map(path -> {
                                   List<GraphNode<EntityHierarchyNode>> nodePath = path.stream()
                                                                                       .map(e -> toGraphNode(e, executionContext.getUserId(), hp))
                                                                                       .collect(toList());
                                   return new Path<>(nodePath);
                               }).collect(toList());
            return new GetHierarchyPathsToRootResult(result);
        }).orElse(new GetHierarchyPathsToRootResult(Collections.emptyList()));

    }

    private GraphNode<EntityHierarchyNode> toGraphNode(OWLEntity entity, UserId userId, HierarchyProvider<OWLEntity> hierarchyProvider) {
        return new GraphNode<>(
                renderer.render(entity, userId),
                hierarchyProvider.getChildren(entity).isEmpty());
    }
}
