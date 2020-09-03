package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyPathsToRootAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyPathsToRootResult;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;
import static edu.stanford.protege.gwt.graphtree.shared.PathCollector.toPath;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class GetHierarchyPathsToRootActionHandler extends AbstractProjectActionHandler<GetHierarchyPathsToRootAction, GetHierarchyPathsToRootResult> {

    @Nonnull
    private final HierarchyProviderMapper hierarchyProviderMapper;

    @Nonnull
    private final GraphNodeRenderer nodeRenderer;

    @Inject
    public GetHierarchyPathsToRootActionHandler(@Nonnull AccessManager accessManager,
                                                @Nonnull HierarchyProviderMapper hierarchyProviderMapper,
                                                @Nonnull GraphNodeRenderer nodeRenderer) {
        super(accessManager);
        this.hierarchyProviderMapper = hierarchyProviderMapper;
        this.nodeRenderer = nodeRenderer;
    }

    @Nonnull
    @Override
    public Class<GetHierarchyPathsToRootAction> getActionClass() {
        return GetHierarchyPathsToRootAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetHierarchyPathsToRootAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetHierarchyPathsToRootResult execute(@Nonnull GetHierarchyPathsToRootAction action, @Nonnull ExecutionContext executionContext) {
        Optional<HierarchyProvider<OWLEntity>> hierarchyProvider = hierarchyProviderMapper.getHierarchyProvider(action.getHierarchyId());
        return hierarchyProvider.map(hp -> {
            Collection<List<OWLEntity>> pathsToRoot = hp.getPathsToRoot(action.getEntity());
            List<Path<GraphNode<EntityNode>>> result =
                    pathsToRoot.stream()
                               .map(pathList -> pathList.stream()
                                                        .map(e -> nodeRenderer.toGraphNode(e, hp))
                                                        .collect(toPath()))
                               .collect(toList());
            return new GetHierarchyPathsToRootResult(result);
        }).orElse(new GetHierarchyPathsToRootResult(Collections.emptyList()));

    }
}
