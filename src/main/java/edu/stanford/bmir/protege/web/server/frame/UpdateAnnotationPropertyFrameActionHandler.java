package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.change.ReverseEngineeredChangeDescriptionGeneratorFactory;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectResult;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.UpdateAnnotationPropertyFrameAction;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/04/2013
 */
public class UpdateAnnotationPropertyFrameActionHandler extends AbstractUpdateFrameHandler<UpdateAnnotationPropertyFrameAction, AnnotationPropertyFrame, OWLAnnotationPropertyData> {

    @Nonnull
    private final Provider<AnnotationPropertyFrameTranslator> translatorProvider;

    @Inject
    public UpdateAnnotationPropertyFrameActionHandler(@Nonnull AccessManager accessManager,
                                                      @Nonnull ReverseEngineeredChangeDescriptionGeneratorFactory changeDescriptionGeneratorFactory,
                                                      @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                                      @Nonnull HasApplyChanges applyChanges,
                                                      @Nonnull OWLOntology rootOntology,
                                                      @Nonnull Provider<AnnotationPropertyFrameTranslator> translatorProvider) {
        super(accessManager, changeDescriptionGeneratorFactory, eventManager, applyChanges, rootOntology);
        this.translatorProvider = translatorProvider;
    }

    @Override
    protected Result createResponse(LabelledFrame<AnnotationPropertyFrame> to, EventList<ProjectEvent<?>> events) {
        return new UpdateObjectResult(events);
    }

    @Override
    protected FrameTranslator<AnnotationPropertyFrame, OWLAnnotationPropertyData> createTranslator() {
        return translatorProvider.get();
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
