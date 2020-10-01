package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.frame.translator.NamedIndividualFrameTranslator;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetNamedIndividualFrameResult;
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
 * Date: 20/02/2013
 */
public class GetNamedIndividualFrameActionHandler extends AbstractProjectActionHandler<GetNamedIndividualFrameAction, GetNamedIndividualFrameResult> {

    private static Logger logger = LoggerFactory.getLogger(GetNamedIndividualFrameActionHandler.class);

    @Nonnull
    private final Provider<NamedIndividualFrameTranslator> translatorProvider;

    @Nonnull
    private final FrameComponentSessionRendererFactory rendererFactory;

    @Nonnull
    private final Comparator<PropertyValue> propertyValueComparator;

    @Inject
    public GetNamedIndividualFrameActionHandler(@Nonnull AccessManager accessManager,
                                                @Nonnull Provider<NamedIndividualFrameTranslator> translatorProvider,
                                                @Nonnull FrameComponentSessionRendererFactory rendererFactory,
                                                @Nonnull Comparator<PropertyValue> propertyValueComparator) {
        super(accessManager);
        this.translatorProvider = translatorProvider;
        this.rendererFactory = rendererFactory;
        this.propertyValueComparator = propertyValueComparator;
    }

    /**
     * Gets the class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action} handled by this handler.
     * @return The class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public Class<GetNamedIndividualFrameAction> getActionClass() {
        return GetNamedIndividualFrameAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetNamedIndividualFrameAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetNamedIndividualFrameResult execute(@Nonnull GetNamedIndividualFrameAction action,
                                                 @Nonnull ExecutionContext executionContext) {
        var translator = translatorProvider.get();
        translator.setMinimizePropertyValues(true);
        var plainFrame = translator.getFrame(action.getSubject());
        var renderer = rendererFactory.create();
        var renderedFrame = plainFrame.toEntityFrame(renderer, propertyValueComparator);
        logger.info(BROWSING,
                     "{} {} retrieved NamedIndividual frame for {}",
                    action.getProjectId(),
                    executionContext.getUserId(),
                    action.getSubject());
        return new GetNamedIndividualFrameResult(renderedFrame);

    }
}
