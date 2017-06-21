package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GetNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetNamedIndividualFrameResult;
import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
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
 * Date: 20/02/2013
 */
public class GetNamedIndividualFrameActionHandler extends AbstractHasProjectActionHandler<GetNamedIndividualFrameAction, GetNamedIndividualFrameResult> {

    private static Logger logger = LoggerFactory.getLogger(GetNamedIndividualFrameActionHandler.class);

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final NamedIndividualFrameTranslator translator;

    @Inject
    public GetNamedIndividualFrameActionHandler(@Nonnull AccessManager accessManager,
                                                @Nonnull RenderingManager renderingManager,
                                                @Nonnull NamedIndividualFrameTranslator translator) {
        super(accessManager);
        this.renderingManager = renderingManager;
        this.translator = translator;
    }

    /**
     * Gets the class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action} handled by this handler.
     * @return The class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action}.  Not {@code null}.
     */
    @Override
    public Class<GetNamedIndividualFrameAction> getActionClass() {
        return GetNamedIndividualFrameAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    public GetNamedIndividualFrameResult execute(GetNamedIndividualFrameAction action,
                                                    ExecutionContext executionContext) {
        NamedIndividualFrame frame = translator.getFrame(
                renderingManager.getRendering(action.getSubject())
        );
        String rendering = renderingManager.getShortForm(action.getSubject());
        LabelledFrame<NamedIndividualFrame> labelledFrame = new LabelledFrame<>(rendering, frame);
        logger.info(BROWSING,
                     "{} {} retrieved NamedIndividual frame for {} ({})",
                    action.getProjectId(),
                    executionContext.getUserId(),
                    action.getSubject(),
                    labelledFrame.getDisplayName());
        return new GetNamedIndividualFrameResult(labelledFrame);

    }
}
