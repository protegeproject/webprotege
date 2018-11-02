package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetAnnotationPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetAnnotationPropertyFrameResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;

import static edu.stanford.bmir.protege.web.server.logging.Markers.BROWSING;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetAnnotationPropertyFrameActionHandler extends AbstractProjectActionHandler<GetAnnotationPropertyFrameAction, GetAnnotationPropertyFrameResult> {

    private Logger logger = LoggerFactory.getLogger(GetAnnotationPropertyFrameActionHandler.class);

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final Provider<AnnotationPropertyFrameTranslator> translatorProvider;

    @Inject
    public GetAnnotationPropertyFrameActionHandler(@Nonnull AccessManager accessManager,
                                                   @Nonnull RenderingManager renderingManager,
                                                   @Nonnull Provider<AnnotationPropertyFrameTranslator> translatorProvider) {
        super(accessManager);
        this.renderingManager = renderingManager;
        this.translatorProvider = translatorProvider;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetAnnotationPropertyFrameResult execute(@Nonnull GetAnnotationPropertyFrameAction action, @Nonnull ExecutionContext executionContext) {
        AnnotationPropertyFrameTranslator translator = translatorProvider.get();
        OWLAnnotationPropertyData annotationPropertyData = renderingManager.getAnnotationPropertyData(action.getSubject());
        AnnotationPropertyFrame frame = translator.getFrame(annotationPropertyData);
        logger.info(BROWSING,
                     "{} {} retrieved AnnotationProperty frame for {} ({})",
                    action.getProjectId(),
                    executionContext.getUserId(),
                    action.getSubject(),
                    frame.getSubject().getBrowserText());
        return new GetAnnotationPropertyFrameResult(frame);
    }

    @Nonnull
    @Override
    public Class<GetAnnotationPropertyFrameAction> getActionClass() {
        return GetAnnotationPropertyFrameAction.class;
    }
}
