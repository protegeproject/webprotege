package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.frame.translator.ObjectPropertyFrameTranslator;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameResult;
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
public class GetObjectPropertyFrameActionHandler extends AbstractProjectActionHandler<GetObjectPropertyFrameAction, GetObjectPropertyFrameResult> {

    private static Logger logger = LoggerFactory.getLogger(GetObjectPropertyFrameAction.class);

    @Nonnull
    private final Provider<ObjectPropertyFrameTranslator> translatorProvider;

    @Nonnull
    private final FrameComponentSessionRendererFactory rendererFactory;

    @Nonnull
    private final PropertyValueComparator propertyValueComparator;

    @Inject
    public GetObjectPropertyFrameActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull Provider<ObjectPropertyFrameTranslator> translatorProvider,
                                               @Nonnull FrameComponentSessionRendererFactory rendererFactory,
                                               @Nonnull PropertyValueComparator propertyValueComparator) {
        super(accessManager);
        this.rendererFactory = rendererFactory;
        this.translatorProvider = translatorProvider;
        this.propertyValueComparator = propertyValueComparator;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetObjectPropertyFrameAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    public GetObjectPropertyFrameResult execute(@Nonnull GetObjectPropertyFrameAction action, @Nonnull ExecutionContext executionContext) {
        var translator = translatorProvider.get();
        var plainFrame = translator.getFrame(action.getSubject());
        var renderedFrame = plainFrame.toEntityFrame(rendererFactory.create(), propertyValueComparator);
        logger.info(BROWSING,
                     "{} {} retrieved ObjectProperty frame for {}",
                     action.getProjectId(),
                     executionContext.getUserId(),
                     action.getSubject());
        return new GetObjectPropertyFrameResult(renderedFrame);
    }

    @Nonnull
    @Override
    public Class<GetObjectPropertyFrameAction> getActionClass() {
        return GetObjectPropertyFrameAction.class;
    }
}
