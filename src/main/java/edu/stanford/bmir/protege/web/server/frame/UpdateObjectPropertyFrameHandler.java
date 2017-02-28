package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.UpdateObjectPropertyFrameAction;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class UpdateObjectPropertyFrameHandler extends AbstractUpdateFrameHandler<UpdateObjectPropertyFrameAction, ObjectPropertyFrame, OWLObjectProperty> {

    @Inject
    public UpdateObjectPropertyFrameHandler(OWLAPIProjectManager projectManager,
                                            AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    protected Result createResponse(LabelledFrame<ObjectPropertyFrame> to, EventList<ProjectEvent<?>> events) {
        return new UpdateObjectResult(events);
    }

    @Override
    protected FrameTranslator<ObjectPropertyFrame, OWLObjectProperty> createTranslator() {
        return new ObjectPropertyFrameTranslator();
    }

    @Override
    protected String getChangeDescription(LabelledFrame<ObjectPropertyFrame> from, LabelledFrame<ObjectPropertyFrame> to) {
        return "Edited object property";
    }

    @Override
    public Class<UpdateObjectPropertyFrameAction> getActionClass() {
        return UpdateObjectPropertyFrameAction.class;
    }
}
