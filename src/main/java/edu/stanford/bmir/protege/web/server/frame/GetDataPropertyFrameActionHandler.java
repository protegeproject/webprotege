package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
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
public class GetDataPropertyFrameActionHandler extends AbstractHasProjectActionHandler<GetDataPropertyFrameAction, GetDataPropertyFrameResult> {

    private static final Logger logger = LoggerFactory.getLogger(GetDataPropertyFrameActionHandler.class);

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final Provider<DataPropertyFrameTranslator> translatorProvider;

    @Inject
    public GetDataPropertyFrameActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull RenderingManager renderingManager,
                                             @Nonnull Provider<DataPropertyFrameTranslator> translatorProvider) {
        super(accessManager);
        this.renderingManager = renderingManager;
        this.translatorProvider = translatorProvider;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_CHANGES;
    }

    @Override
    public GetDataPropertyFrameResult execute(GetDataPropertyFrameAction action, ExecutionContext executionContext) {
        DataPropertyFrameTranslator translator = translatorProvider.get();
        final DataPropertyFrame frame = translator.getFrame(renderingManager.getRendering(action.getSubject())
        );
        String displayName = renderingManager.getBrowserText(action.getSubject());
        logger.info(BROWSING,
                    "{} {} retrieved DataProperty frame for {} ({})",
                    action.getProjectId(),
                    executionContext.getUserId(),
                    action.getSubject(),
                    displayName);
        return new GetDataPropertyFrameResult(new LabelledFrame<>(displayName, frame));
    }

    @Override
    public Class<GetDataPropertyFrameAction> getActionClass() {
        return GetDataPropertyFrameAction.class;
    }
}
