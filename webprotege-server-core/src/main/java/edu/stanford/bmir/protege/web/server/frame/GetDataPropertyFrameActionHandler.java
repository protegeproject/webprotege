package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.DataPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetDataPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetDataPropertyFrameResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;

import static edu.stanford.bmir.protege.web.server.logging.Markers.BROWSING;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_CHANGES;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetDataPropertyFrameActionHandler extends AbstractProjectActionHandler<GetDataPropertyFrameAction, GetDataPropertyFrameResult> {

    private static final Logger logger = LoggerFactory.getLogger(GetDataPropertyFrameActionHandler.class);

    @Nonnull
    private final Provider<DataPropertyFrameTranslator> translatorProvider;

    @Nonnull
    private final FrameComponentSessionRendererFactory rendererFactory;

    @Inject
    public GetDataPropertyFrameActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull Provider<DataPropertyFrameTranslator> translatorProvider,
                                             @Nonnull FrameComponentSessionRendererFactory rendererFactory) {
        super(accessManager);
        this.translatorProvider = translatorProvider;
        this.rendererFactory = rendererFactory;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_CHANGES;
    }

    @Nonnull
    @Override
    public GetDataPropertyFrameResult execute(@Nonnull GetDataPropertyFrameAction action, @Nonnull ExecutionContext executionContext) {
        var translator = translatorProvider.get();
        var plainFrame = translator.getFrame(action.getSubject());
        var renderedFrame = plainFrame.toEntityFrame(rendererFactory.create());
        logger.info(BROWSING,
                    "{} {} retrieved DataProperty frame for {} ({})",
                    action.getProjectId(),
                    executionContext.getUserId(),
                    action.getSubject(),
                    renderedFrame.getSubject().getBrowserText());
        return new GetDataPropertyFrameResult(renderedFrame);
    }

    @Nonnull
    @Override
    public Class<GetDataPropertyFrameAction> getActionClass() {
        return GetDataPropertyFrameAction.class;
    }
}
