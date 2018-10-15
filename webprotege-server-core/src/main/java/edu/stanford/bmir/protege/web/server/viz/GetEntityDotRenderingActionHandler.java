package edu.stanford.bmir.protege.web.server.viz;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.viz.GetEntityDotRenderingAction;
import edu.stanford.bmir.protege.web.shared.viz.GetEntityDotRenderingResult;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraph;

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

    @Nonnull
    private final DotRendererFactory rendererFactory;

    @Nonnull
    private final EntityGraphBuilder graphBuilder;

    @Inject
    public GetEntityDotRenderingActionHandler(@Nonnull AccessManager accessManager,
                                              @Nonnull DotRendererFactory rendererFactory,
                                              @Nonnull EntityGraphBuilder graphBuilder) {
        super(accessManager);
        this.rendererFactory = checkNotNull(rendererFactory);
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
        DotRenderer dotRenderer = rendererFactory.create(graph);
        StringWriter writer = new StringWriter();
        dotRenderer.render(writer);
        return GetEntityDotRenderingResult.get(writer.toString(),
                                               graph);
    }
}
