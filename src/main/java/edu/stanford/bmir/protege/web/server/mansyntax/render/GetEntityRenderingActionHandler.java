package edu.stanford.bmir.protege.web.server.mansyntax.render;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
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
public class GetEntityRenderingActionHandler extends AbstractHasProjectActionHandler<GetEntityRenderingAction, GetEntityRenderingResult> {

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetEntityRenderingActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.renderingManager = renderingManager;
    }

    @Override
    public Class<GetEntityRenderingAction> getActionClass() {
        return GetEntityRenderingAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    public GetEntityRenderingResult execute(GetEntityRenderingAction action,
                                               ExecutionContext executionContext) {
        OWLEntity entity = action.getEntity();
        return new GetEntityRenderingResult(renderingManager.getFrameRendering(entity),
                                            renderingManager.getRendering(entity));
    }
}
