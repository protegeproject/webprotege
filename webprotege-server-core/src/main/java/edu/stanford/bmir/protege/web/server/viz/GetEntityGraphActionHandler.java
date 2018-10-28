package edu.stanford.bmir.protege.web.server.viz;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.viz.GetEntityGraphAction;
import edu.stanford.bmir.protege.web.shared.viz.GetEntityGraphResult;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public class GetEntityGraphActionHandler extends AbstractProjectActionHandler<GetEntityGraphAction, GetEntityGraphResult> {

    private static Logger logger = LoggerFactory.getLogger(GetEntityGraphActionHandler.class);

    @Nonnull
    private final EntityGraphBuilder graphBuilder;

    @Inject
    public GetEntityGraphActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull EntityGraphBuilder graphBuilder) {
        super(accessManager);
        this.graphBuilder = checkNotNull(graphBuilder);
    }

    @Nonnull
    @Override
    public Class<GetEntityGraphAction> getActionClass() {
        return GetEntityGraphAction.class;
    }

    @Nonnull
    @Override
    public GetEntityGraphResult execute(@Nonnull GetEntityGraphAction action, @Nonnull ExecutionContext executionContext) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        EntityGraph graph = graphBuilder.createGraph(action.getEntity());
        stopwatch.stop();
        logger.debug("Created entity graph [{} nodes; edges {}] in {} ms",
                    graph.getNodes().size(),
                    graph.getEdges().size(),
                    stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return GetEntityGraphResult.get(graph);
    }
}
