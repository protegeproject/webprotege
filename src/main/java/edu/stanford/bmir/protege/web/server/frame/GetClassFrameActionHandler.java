package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.dispatch.RenderableGetObjectResult;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetClassFrameAction;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import org.semanticweb.owlapi.model.OWLClass;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class GetClassFrameActionHandler extends AbstractHasProjectActionHandler<GetClassFrameAction, GetObjectResult<LabelledFrame<ClassFrame>>> {

    public static final ClassFrameTranslator TRANSLATOR = new ClassFrameTranslator();

    @Inject
    public GetClassFrameActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }

    /**
     * Gets the class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action} handled by this handler.
     * @return The class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action}.  Not {@code null}.
     */
    @Override
    public Class<GetClassFrameAction> getActionClass() {
        return GetClassFrameAction.class;
    }

    @Override
    protected RequestValidator<GetClassFrameAction> getAdditionalRequestValidator(GetClassFrameAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected GetObjectResult<LabelledFrame<ClassFrame>> execute(GetClassFrameAction action, OWLAPIProject project, ExecutionContext executionContext) {
        FrameActionResultTranslator<ClassFrame, OWLClass> translator = new FrameActionResultTranslator<ClassFrame, OWLClass>(action.getSubject(), project, TRANSLATOR);
        LabelledFrame<ClassFrame> f = translator.doIT();
        final BrowserTextMap browserTextMap = new BrowserTextMap(f, project.getRenderingManager());
        return new RenderableGetObjectResult<LabelledFrame<ClassFrame>>(f, browserTextMap);
    }
}
