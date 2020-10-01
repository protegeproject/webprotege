package edu.stanford.bmir.protege.web.server.mansyntax.render;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityHtmlRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityHtmlRenderingResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-27
 */
public class GetEntityHtmlRenderingActionHandler extends AbstractProjectActionHandler<GetEntityHtmlRenderingAction, GetEntityHtmlRenderingResult> {

    @Nonnull
    private final ManchesterSyntaxEntityFrameRenderer renderer;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetEntityHtmlRenderingActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull ManchesterSyntaxEntityFrameRenderer renderer,
                                               @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.renderer = checkNotNull(renderer);
        this.renderingManager = checkNotNull(renderingManager);
    }

    @Nonnull
    @Override
    public Class<GetEntityHtmlRenderingAction> getActionClass() {
        return GetEntityHtmlRenderingAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetEntityHtmlRenderingAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetEntityHtmlRenderingResult execute(@Nonnull GetEntityHtmlRenderingAction action,
                                                @Nonnull ExecutionContext executionContext) {
        var sb = new StringBuilder();
        var entity = action.getEntity();
        renderer.render(entity, sb);
        var entityData = renderingManager.getRendering(entity);
        return new GetEntityHtmlRenderingResult(entityData, sb.toString());
    }
}
