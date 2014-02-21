package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.frame.AnnotationPropertyFrameTranslator;
import edu.stanford.bmir.protege.web.server.frame.FrameTranslator;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.UpdateAnnotationPropertyFrameAction;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/04/2013
 */
public class UpdateAnnotationPropertyFrameActionHandler extends AbstractUpdateFrameHandler<UpdateAnnotationPropertyFrameAction, AnnotationPropertyFrame, OWLAnnotationProperty> {

    @Override
    protected Result createResponse(LabelledFrame<AnnotationPropertyFrame> to, EventList<ProjectEvent<?>> events) {
        return new UpdateObjectResult(events);
    }

    @Override
    protected FrameTranslator<AnnotationPropertyFrame, OWLAnnotationProperty> createTranslator() {
        return new AnnotationPropertyFrameTranslator();
    }

    @Override
    protected String getChangeDescription(LabelledFrame<AnnotationPropertyFrame> from, LabelledFrame<AnnotationPropertyFrame> to) {
        return "Edited annotation property";
    }

    @Override
    public Class<UpdateAnnotationPropertyFrameAction> getActionClass() {
        return UpdateAnnotationPropertyFrameAction.class;
    }
}
