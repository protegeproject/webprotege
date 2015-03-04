package edu.stanford.bmir.protege.web.server.render;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingResult;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class GetEntityRenderingActionHandler extends AbstractHasProjectActionHandler<GetEntityRenderingAction, GetEntityRenderingResult> {

    @Inject
    public GetEntityRenderingActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }

    @Override
    public Class<GetEntityRenderingAction> getActionClass() {
        return GetEntityRenderingAction.class;
    }

    @Override
    protected RequestValidator<GetEntityRenderingAction> getAdditionalRequestValidator(GetEntityRenderingAction action,
                                                                                       RequestContext requestContext) {
        return null;
    }

    @Override
    protected GetEntityRenderingResult execute(GetEntityRenderingAction action,
                                               final OWLAPIProject project,
                                               ExecutionContext executionContext) {
        OWLEntity entity = action.getEntity();
        HasGetFrameRendering renderer = project.getRenderingManager();
        return new GetEntityRenderingResult(renderer.getFrameRendering(entity));
    }
}
