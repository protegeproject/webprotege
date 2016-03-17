package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.dispatch.actions.UpdateClassFrameAction;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.change.ReverseEngineeredChangeDescriptionGeneratorFactory;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.dispatch.validators.WritePermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import org.semanticweb.owlapi.model.OWLClass;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class UpdateClassFrameActionHandler extends AbstractUpdateFrameHandler<UpdateClassFrameAction, ClassFrame, OWLClass> {

    @Inject
    public UpdateClassFrameActionHandler(OWLAPIProjectManager projectManager, ValidatorFactory<WritePermissionValidator> validatorFactory) {
        super(projectManager, validatorFactory);
    }

    /**
     * Gets the class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action} handled by this handler.
     * @return The class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action}.  Not {@code null}.
     */
    @Override
    public Class<UpdateClassFrameAction> getActionClass() {
        return UpdateClassFrameAction.class;
    }

    @Override
    protected Result createResponse(LabelledFrame<ClassFrame> to, EventList<ProjectEvent<?>> events) {
        return new UpdateObjectResult(events);
    }

    @Override
    protected FrameTranslator<ClassFrame, OWLClass> createTranslator() {
        return new ClassFrameTranslator();
    }

    @Override
    protected String getChangeDescription(LabelledFrame<ClassFrame> from, LabelledFrame<ClassFrame> to) {
        return "Edited class";
    }
}
