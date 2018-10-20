package edu.stanford.bmir.protege.web.server.viz;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.viz.GetEntityDotRenderingAction;
import edu.stanford.bmir.protege.web.shared.viz.GetEntityDotRenderingResult;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.io.StringWriter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public class GetEntityDotRenderingActionHandler extends AbstractProjectActionHandler<GetEntityDotRenderingAction, GetEntityDotRenderingResult> {

    private static Logger logger = LoggerFactory.getLogger(GetEntityDotRenderingActionHandler.class);

    @Nonnull
    private final EntityGraphBuilder graphBuilder;

    @Inject
    public GetEntityDotRenderingActionHandler(@Nonnull AccessManager accessManager,
                                              @Nonnull EntityGraphBuilder graphBuilder) {
        super(accessManager);
        this.graphBuilder = checkNotNull(graphBuilder);
    }

    @Nonnull
    @Override
    public Class<GetEntityDotRenderingAction> getActionClass() {
        return GetEntityDotRenderingAction.class;
    }

    @Nonnull
    @Override
    public GetEntityDotRenderingResult execute(@Nonnull GetEntityDotRenderingAction action, @Nonnull ExecutionContext executionContext) {
        EntityGraph graph = graphBuilder.createGraph(action.getEntity());
        logger.info("Created entity graph [" + graph.getNodes().size() + " nodes; edges " + graph.getEdges().size() + "]");
        return GetEntityDotRenderingResult.get(graph);
    }
}
