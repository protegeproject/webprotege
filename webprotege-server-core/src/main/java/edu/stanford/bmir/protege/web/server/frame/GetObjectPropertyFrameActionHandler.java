package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
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
    private final RenderingManager rm;

    @Nonnull
    private final Provider<ObjectPropertyFrameTranslator> translatorProvider;

    @Inject
    public GetObjectPropertyFrameActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull RenderingManager rm,
                                               @Nonnull Provider<ObjectPropertyFrameTranslator> translatorProvider) {
        super(accessManager);
        this.rm = rm;
        this.translatorProvider = translatorProvider;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Nonnull
    public GetObjectPropertyFrameResult execute(@Nonnull GetObjectPropertyFrameAction action, @Nonnull ExecutionContext executionContext) {
        ObjectPropertyFrameTranslator translator = translatorProvider.get();
        OWLObjectPropertyData objectPropertyData = rm.getObjectPropertyData(action.getSubject());
        ObjectPropertyFrame f = translator.getFrame(objectPropertyData);
        logger.info(BROWSING,
                     "{} {} retrieved ObjectProperty frame for {} ({})",
                     action.getProjectId(),
                     executionContext.getUserId(),
                     action.getSubject(),
                     f.getSubject().getBrowserText());
        return new GetObjectPropertyFrameResult(f);
    }

    @Nonnull
    @Override
    public Class<GetObjectPropertyFrameAction> getActionClass() {
        return GetObjectPropertyFrameAction.class;
    }
}
