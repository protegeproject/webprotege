package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectResult;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.UpdateAnnotationPropertyFrameAction;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/04/2013
 */
public class UpdateAnnotationPropertyFrameActionHandler extends AbstractUpdateFrameHandler<UpdateAnnotationPropertyFrameAction, AnnotationPropertyFrame, OWLAnnotationPropertyData> {

    @Inject
    public UpdateAnnotationPropertyFrameActionHandler(ProjectManager projectManager, AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    protected Result createResponse(LabelledFrame<AnnotationPropertyFrame> to, EventList<ProjectEvent<?>> events) {
        return new UpdateObjectResult(events);
    }

    @Override
    protected FrameTranslator<AnnotationPropertyFrame, OWLAnnotationPropertyData> createTranslator() {
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
