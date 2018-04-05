package edu.stanford.bmir.protege.web.server.mansyntax.render;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingResult;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class GetEntityRenderingActionHandler extends AbstractProjectActionHandler<GetEntityRenderingAction, GetEntityRenderingResult> {

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final ManchesterSyntaxEntityFrameRenderer frameRenderer;

    @Inject
    public GetEntityRenderingActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull RenderingManager renderingManager,
                                           @Nonnull ManchesterSyntaxEntityFrameRenderer frameRenderer) {
        super(accessManager);
        this.renderingManager = renderingManager;
        this.frameRenderer = frameRenderer;
    }

    @Nonnull
    @Override
    public Class<GetEntityRenderingAction> getActionClass() {
        return GetEntityRenderingAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetEntityRenderingResult execute(@Nonnull GetEntityRenderingAction action,
                                            @Nonnull ExecutionContext executionContext) {
        OWLEntity entity = action.getEntity();
        StringBuilder sb = new StringBuilder();
        frameRenderer.render(entity, sb);
        return new GetEntityRenderingResult(sb.toString(),
                                            renderingManager.getRendering(entity));
    }
}
