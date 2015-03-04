package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.dispatch.RenderableGetObjectResult;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.frame.DataPropertyFrameTranslator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.frame.DataPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetDataPropertyFrameAction;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetDataPropertyFrameActionHandler extends AbstractHasProjectActionHandler<GetDataPropertyFrameAction, GetObjectResult<LabelledFrame<DataPropertyFrame>>> {

    @Inject
    public GetDataPropertyFrameActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }

    @Override
    protected RequestValidator<GetDataPropertyFrameAction> getAdditionalRequestValidator(GetDataPropertyFrameAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected GetObjectResult<LabelledFrame<DataPropertyFrame>> execute(GetDataPropertyFrameAction action, OWLAPIProject project, ExecutionContext executionContext) {
        DataPropertyFrameTranslator translator = new DataPropertyFrameTranslator();
        final DataPropertyFrame frame = translator.getFrame(action.getSubject(), project.getRootOntology(), project);
        String displayName = project.getRenderingManager().getBrowserText(action.getSubject());
        BrowserTextMap btm = BrowserTextMap.build(project.getRenderingManager(), frame.getSignature().toArray());
        return new RenderableGetObjectResult<LabelledFrame<DataPropertyFrame>>(new LabelledFrame<DataPropertyFrame>(displayName, frame), btm);
    }

    @Override
    public Class<GetDataPropertyFrameAction> getActionClass() {
        return GetDataPropertyFrameAction.class;
    }
}
