package edu.stanford.bmir.protege.web.server.match;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.hierarchy.EntityHierarchyNodeRenderer;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesAction;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static edu.stanford.bmir.protege.web.server.pagination.PageCollector.toPage;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class GetMatchingEntitiesActionHandler extends AbstractProjectActionHandler<GetMatchingEntitiesAction, GetMatchingEntitiesResult>{

    private static Logger logger = LoggerFactory.getLogger(GetMatchingEntitiesActionHandler.class);

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final MatcherFactory matcherFactory;

    @Nonnull
    private final EntityHierarchyNodeRenderer nodeRenderer;


    @Inject
    public GetMatchingEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull OWLOntology rootOntology,
                                            @Nonnull RenderingManager renderingManager,
                                            @Nonnull MatcherFactory matcherFactory,
                                            @Nonnull EntityHierarchyNodeRenderer nodeRenderer) {
        super(accessManager);
        this.rootOntology = rootOntology;
        this.renderingManager = renderingManager;
        this.matcherFactory = matcherFactory;
        this.nodeRenderer = nodeRenderer;
    }

    @Nonnull
    @Override
    public Class<GetMatchingEntitiesAction> getActionClass() {
        return GetMatchingEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetMatchingEntitiesResult execute(@Nonnull GetMatchingEntitiesAction action, @Nonnull ExecutionContext executionContext) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Matcher<OWLEntity> matcher = matcherFactory.getMatcher((RootCriteria) action.getCriteria());
        PageRequest pageRequest = action.getPageRequest();
        Optional<Page<OWLEntityData>> result = rootOntology.getSignature().stream()
                                                                 .filter(matcher::matches)
                                                                 .map(entity -> DataFactory.getOWLEntityData(entity, renderingManager.getShortForm(entity)))
                                                                 .sorted()
                                                                 .collect(toPage(pageRequest.getPageNumber(),
                                                                           pageRequest.getPageSize()));
        stopwatch.stop();
        logger.info("Answer criteria query in {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        Optional<Page<EntityHierarchyNode>> entityHierarchyNodes = result.map(pg -> {
            List<EntityHierarchyNode> nodes = pg.getPageElements().stream()
                                                .map(ed -> nodeRenderer.render(ed.getEntity()))
                                                .collect(toList());
            return new Page<>(pg.getPageNumber(),
                              pg.getPageCount(),
                              nodes,
                              pg.getTotalElements());
        });
        return entityHierarchyNodes.map(GetMatchingEntitiesResult::get)
                     .orElseGet(() -> GetMatchingEntitiesResult.get(Page.emptyPage()));
    }
}
