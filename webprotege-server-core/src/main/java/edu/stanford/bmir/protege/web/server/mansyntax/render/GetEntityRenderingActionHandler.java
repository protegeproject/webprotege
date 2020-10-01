package edu.stanford.bmir.protege.web.server.mansyntax.render;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingResult;

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

    @Inject
    public GetEntityRenderingActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.renderingManager = renderingManager;
    }

    @Nonnull
    @Override
    public Class<GetEntityRenderingAction> getActionClass() {
        return GetEntityRenderingAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetEntityRenderingAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetEntityRenderingResult execute(@Nonnull GetEntityRenderingAction action,
                                            @Nonnull ExecutionContext executionContext) {
        var entity = action.getEntity();
        return new GetEntityRenderingResult(renderingManager.getRendering(entity));
    }
}
