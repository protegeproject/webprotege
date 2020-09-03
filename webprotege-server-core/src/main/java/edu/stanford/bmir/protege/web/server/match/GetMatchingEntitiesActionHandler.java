package edu.stanford.bmir.protege.web.server.match;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesAction;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;
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
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final EntityNodeRenderer nodeRenderer;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final MatchingEngine matchingEngine;

    @Inject
    public GetMatchingEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull DictionaryManager dictionaryManager,
                                            @Nonnull EntityNodeRenderer nodeRenderer,
                                            @Nonnull RenderingManager renderingManager, @Nonnull MatchingEngine matchingEngine) {
        super(accessManager);
        this.dictionaryManager = checkNotNull(dictionaryManager);
        this.nodeRenderer = checkNotNull(nodeRenderer);
        this.renderingManager = renderingManager;
        this.matchingEngine = checkNotNull(matchingEngine);
    }

    @Nonnull
    @Override
    public Class<GetMatchingEntitiesAction> getActionClass() {
        return GetMatchingEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetMatchingEntitiesAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetMatchingEntitiesResult execute(@Nonnull GetMatchingEntitiesAction action, @Nonnull ExecutionContext executionContext) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        PageRequest pageRequest = action.getPageRequest();
        Criteria criteria = action.getCriteria();
        Optional<Page<OWLEntityData>> result = matchingEngine.match(criteria)
                                                             .map(renderingManager::getRendering)
                                                             .sorted()
                                                             .collect(toPage(pageRequest.getPageNumber(),
                                                                           pageRequest.getPageSize()));
        stopwatch.stop();
        logger.info("{} {} Answered query in {} ms",
                    action.getProjectId(),
                    executionContext.getUserId(),
                    stopwatch.elapsed(TimeUnit.MILLISECONDS));
        Optional<Page<EntityNode>> entityHierarchyNodes = result.map(pg -> {
            List<EntityNode> nodes = pg.getPageElements().stream()
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
