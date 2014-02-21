package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.RenderableGetObjectResult;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.frame.AnnotationPropertyFrameTranslator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetAnnotationPropertyFrameAction;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetAnnotationPropertyFrameActionHandler extends AbstractHasProjectActionHandler<GetAnnotationPropertyFrameAction, GetObjectResult<LabelledFrame<AnnotationPropertyFrame>>> {

    @Override
    protected RequestValidator<GetAnnotationPropertyFrameAction> getAdditionalRequestValidator(GetAnnotationPropertyFrameAction action, RequestContext requestContext) {
        return new UserHasProjectReadPermissionValidator();
    }

    @Override
    protected GetObjectResult<LabelledFrame<AnnotationPropertyFrame>> execute(GetAnnotationPropertyFrameAction action, OWLAPIProject project, ExecutionContext executionContext) {
        AnnotationPropertyFrameTranslator translator = new AnnotationPropertyFrameTranslator();
        AnnotationPropertyFrame frame = translator.getFrame(action.getSubject(), project.getRootOntology(), project);
        LabelledFrame<AnnotationPropertyFrame> labelledFrame = new LabelledFrame<AnnotationPropertyFrame>(project.getRenderingManager().getBrowserText(action.getSubject()), frame);
        return new RenderableGetObjectResult<LabelledFrame<AnnotationPropertyFrame>>(labelledFrame, BrowserTextMap.build(project.getRenderingManager(), frame.getSignature().toArray()));
    }

    @Override
    public Class<GetAnnotationPropertyFrameAction> getActionClass() {
        return GetAnnotationPropertyFrameAction.class;
    }
}
