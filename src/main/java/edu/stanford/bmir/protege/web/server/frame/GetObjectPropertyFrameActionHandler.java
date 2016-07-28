package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetObjectPropertyFrameActionHandler extends AbstractHasProjectActionHandler<GetObjectPropertyFrameAction, GetObjectPropertyFrameResult> {

    private static final ObjectPropertyFrameTranslator TRANSLATOR = new ObjectPropertyFrameTranslator();

    private final ValidatorFactory<ReadPermissionValidator> validatorFactory;

    @Inject
    public GetObjectPropertyFrameActionHandler(OWLAPIProjectManager projectManager, ValidatorFactory<ReadPermissionValidator> validatorFactory) {
        super(projectManager);
        this.validatorFactory = validatorFactory;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(GetObjectPropertyFrameAction action, RequestContext requestContext) {
        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected GetObjectPropertyFrameResult execute(GetObjectPropertyFrameAction action, OWLAPIProject project, ExecutionContext executionContext) {
        FrameActionResultTranslator<ObjectPropertyFrame, OWLObjectProperty> translator = new FrameActionResultTranslator<>(action.getSubject(), project, TRANSLATOR);
        LabelledFrame<ObjectPropertyFrame> f = translator.doIT();
        final BrowserTextMap browserTextMap = new BrowserTextMap(f, project.getRenderingManager());
        return new GetObjectPropertyFrameResult(f, browserTextMap);
    }

    @Override
    public Class<GetObjectPropertyFrameAction> getActionClass() {
        return GetObjectPropertyFrameAction.class;
    }
}
