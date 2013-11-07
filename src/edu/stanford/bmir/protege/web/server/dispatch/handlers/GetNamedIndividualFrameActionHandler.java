package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GetNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.frame.NamedIndividualFrameTranslator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class GetNamedIndividualFrameActionHandler implements ActionHandler<GetNamedIndividualFrameAction, GetObjectResult<LabelledFrame<NamedIndividualFrame>>> {

    private static final NamedIndividualFrameTranslator TRANSLATOR = new NamedIndividualFrameTranslator();

    /**
     * Gets the class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action} handled by this handler.
     * @return The class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action}.  Not {@code null}.
     */
    @Override
    public Class<GetNamedIndividualFrameAction> getActionClass() {
        return GetNamedIndividualFrameAction.class;
    }

    @Override
    public RequestValidator<GetNamedIndividualFrameAction> getRequestValidator(GetNamedIndividualFrameAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    public GetObjectResult<LabelledFrame<NamedIndividualFrame>> execute(GetNamedIndividualFrameAction action, ExecutionContext executionContext) {
        OWLAPIProject project = OWLAPIProjectManager.getProjectManager().getProject(action.getProjectId());
        FrameActionResultTranslator<NamedIndividualFrame, OWLNamedIndividual> t = new FrameActionResultTranslator<NamedIndividualFrame, OWLNamedIndividual>(action.getSubject(), project, TRANSLATOR);
        return new GetObjectResult<LabelledFrame<NamedIndividualFrame>>(t.doIT());
    }

}
