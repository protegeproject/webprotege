package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.UpdateNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.frame.FrameTranslator;
import edu.stanford.bmir.protege.web.server.frame.NamedIndividualFrameTranslator;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class UpdateNamedIndividualFrameHandler extends AbstractUpdateFrameHandler<UpdateNamedIndividualFrameAction, NamedIndividualFrame, OWLNamedIndividual> {

    /**
     * Gets the class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action} handled by this handler.
     * @return The class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action}.  Not {@code null}.
     */
    @Override
    public Class<UpdateNamedIndividualFrameAction> getActionClass() {
        return UpdateNamedIndividualFrameAction.class;
    }

    @Override
    protected Result createResponse(LabelledFrame<NamedIndividualFrame> to, EventList<ProjectEvent<?>> events) {
        return new UpdateObjectResult(events);
    }

    @Override
    protected FrameTranslator<NamedIndividualFrame, OWLNamedIndividual> createTranslator() {
        return new NamedIndividualFrameTranslator();
    }

    @Override
    protected String getChangeDescription(LabelledFrame<NamedIndividualFrame> from, LabelledFrame<NamedIndividualFrame> to) {
        return "Edited individual";
    }

//    @Override
//    protected EntityFrameChangedEvent<OWLNamedIndividual, ?> createEvent(OWLNamedIndividual subject, OWLAPIProject project) {
//        return new NamedIndividualFrameChangedEvent(subject, project.getProjectId());
//    }
}
