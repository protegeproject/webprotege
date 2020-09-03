package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.frame.translator.AnnotationPropertyFrameTranslator;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.frame.GetAnnotationPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetAnnotationPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.Comparator;

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
    private final Provider<AnnotationPropertyFrameTranslator> translatorProvider;

    @Nonnull
    private final FrameComponentSessionRenderer renderer;

    @Nonnull
    private final Comparator<PropertyValue> propertyValueComparator;

    @Inject
    public GetAnnotationPropertyFrameActionHandler(@Nonnull AccessManager accessManager,
                                                   @Nonnull RenderingManager renderingManager,
                                                   @Nonnull Provider<AnnotationPropertyFrameTranslator> translatorProvider,
                                                   @Nonnull FrameComponentSessionRenderer renderer,
                                                   @Nonnull Comparator<PropertyValue> propertyValueComparator) {
        super(accessManager);
        this.renderer = renderer;
        this.translatorProvider = translatorProvider;
        this.propertyValueComparator = propertyValueComparator;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetAnnotationPropertyFrameAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetAnnotationPropertyFrameResult execute(@Nonnull GetAnnotationPropertyFrameAction action, @Nonnull ExecutionContext executionContext) {
        var translator = translatorProvider.get();
        var plainFrame = translator.getFrame(action.getSubject());
        var renderedFrame = plainFrame.toEntityFrame(renderer, propertyValueComparator);
        logger.info(BROWSING,
                     "{} {} retrieved AnnotationProperty frame for {}",
                    action.getProjectId(),
                    executionContext.getUserId(),
                    action.getSubject());
        return new GetAnnotationPropertyFrameResult(renderedFrame);
    }

    @Nonnull
    @Override
    public Class<GetAnnotationPropertyFrameAction> getActionClass() {
        return GetAnnotationPropertyFrameAction.class;
    }
}
