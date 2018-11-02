package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetClassFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetClassFrameResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;

import static edu.stanford.bmir.protege.web.server.logging.Markers.BROWSING;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class GetClassFrameActionHandler extends AbstractProjectActionHandler<GetClassFrameAction, GetClassFrameResult> {

    private static final Logger logger = LoggerFactory.getLogger(GetClassFrameActionHandler.class);

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final Provider<ClassFrameTranslator> translatorProvider;

    @Inject
    public GetClassFrameActionHandler(@Nonnull AccessManager accessManager,
                                      @Nonnull RenderingManager renderingManager,
                                      @Nonnull Provider<ClassFrameTranslator> translatorProvider) {
        super(accessManager);
        this.renderingManager = renderingManager;
        this.translatorProvider = translatorProvider;
    }

    /**
     * Gets the class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action} handled by this handler.
     * @return The class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public Class<GetClassFrameAction> getActionClass() {
        return GetClassFrameAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetClassFrameResult execute(@Nonnull GetClassFrameAction action, @Nonnull ExecutionContext executionContext) {
        var subject = action.getSubject();
        var subjectData = renderingManager.getClassData(subject);
        var translator = translatorProvider.get();
        var classFrame = translator.getFrame(subjectData);
        logger.info(BROWSING,
                    "{} {} retrieved Class frame for {} ({})",
                    action.getProjectId(),
                    executionContext.getUserId(),
                    subject,
                    classFrame.getSubject().getBrowserText());
        return new GetClassFrameResult(classFrame);
    }
}
