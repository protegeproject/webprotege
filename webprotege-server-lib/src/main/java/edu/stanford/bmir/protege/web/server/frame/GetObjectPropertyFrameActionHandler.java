package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.server.logging.Markers.BROWSING;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetObjectPropertyFrameActionHandler extends AbstractProjectActionHandler<GetObjectPropertyFrameAction, GetObjectPropertyFrameResult> {

    private static Logger logger = LoggerFactory.getLogger(GetObjectPropertyFrameAction.class);

    @Nonnull
    private final RenderingManager renderer;

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public GetObjectPropertyFrameActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull RenderingManager renderer,
                                               @Nonnull OWLOntology rootOntology) {
        super(accessManager);
        this.renderer = renderer;
        this.rootOntology = rootOntology;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Nonnull
    public GetObjectPropertyFrameResult execute(@Nonnull GetObjectPropertyFrameAction action, @Nonnull ExecutionContext executionContext) {
        FrameTranslator<ObjectPropertyFrame, OWLObjectPropertyData> translator = new ObjectPropertyFrameTranslator(renderer,
                                                                                                                   rootOntology);

        FrameActionResultTranslator<ObjectPropertyFrame, OWLObjectPropertyData> t = new FrameActionResultTranslator<>(
                renderer,
                translator,
                renderer.getRendering(action.getSubject()));
        LabelledFrame<ObjectPropertyFrame> f = t.doIT();
        logger.info(BROWSING,
                     "{} {} retrieved ObjectProperty frame for {} ({})",
                     action.getProjectId(),
                     executionContext.getUserId(),
                     action.getSubject(),
                     f.getDisplayName());
        return new GetObjectPropertyFrameResult(f);
    }

    @Nonnull
    @Override
    public Class<GetObjectPropertyFrameAction> getActionClass() {
        return GetObjectPropertyFrameAction.class;
    }
}
