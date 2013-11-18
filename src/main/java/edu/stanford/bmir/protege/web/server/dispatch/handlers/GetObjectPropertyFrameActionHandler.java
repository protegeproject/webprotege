package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.RenderableGetObjectResult;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.frame.ObjectPropertyFrameTranslator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetObjectPropertyFrameActionHandler extends AbstractHasProjectActionHandler<GetObjectPropertyFrameAction, GetObjectResult<LabelledFrame<ObjectPropertyFrame>>> {

    private static final ObjectPropertyFrameTranslator TRANSLATOR = new ObjectPropertyFrameTranslator();

    @Override
    protected RequestValidator<GetObjectPropertyFrameAction> getAdditionalRequestValidator(GetObjectPropertyFrameAction action, RequestContext requestContext) {
        return new UserHasProjectReadPermissionValidator();
    }

    @Override
    protected GetObjectResult<LabelledFrame<ObjectPropertyFrame>> execute(GetObjectPropertyFrameAction action, OWLAPIProject project, ExecutionContext executionContext) {
        FrameActionResultTranslator<ObjectPropertyFrame, OWLObjectProperty> translator = new FrameActionResultTranslator<ObjectPropertyFrame, OWLObjectProperty>(action.getSubject(), project, TRANSLATOR);
        LabelledFrame<ObjectPropertyFrame> f = translator.doIT();
        final BrowserTextMap browserTextMap = new BrowserTextMap(f, project.getRenderingManager());
        return new RenderableGetObjectResult<LabelledFrame<ObjectPropertyFrame>>(f, browserTextMap);
    }

    @Override
    public Class<GetObjectPropertyFrameAction> getActionClass() {
        return GetObjectPropertyFrameAction.class;
    }
}
