package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.change.ReverseEngineeredChangeDescriptionGeneratorFactory;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectResult;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.UpdateObjectPropertyFrameAction;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class UpdateObjectPropertyFrameHandler extends AbstractUpdateFrameHandler<UpdateObjectPropertyFrameAction, ObjectPropertyFrame, OWLObjectPropertyData> {

    @Nonnull
    private final Provider<ObjectPropertyFrameTranslator> translatorProvider;

    @Inject
    public UpdateObjectPropertyFrameHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull ReverseEngineeredChangeDescriptionGeneratorFactory changeDescriptionGeneratorFactory,
                                            @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                            @Nonnull HasApplyChanges applyChanges,
                                            @Nonnull OWLOntology rootOntology,
                                            @Nonnull Provider<ObjectPropertyFrameTranslator> translatorProvider) {
        super(accessManager, changeDescriptionGeneratorFactory, eventManager, applyChanges, rootOntology);
        this.translatorProvider = translatorProvider;
    }

    @Override
    protected Result createResponse(LabelledFrame<ObjectPropertyFrame> to, EventList<ProjectEvent<?>> events) {
        return new UpdateObjectResult(events);
    }

    @Override
    protected FrameTranslator<ObjectPropertyFrame, OWLObjectPropertyData> createTranslator() {
        return translatorProvider.get();
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
