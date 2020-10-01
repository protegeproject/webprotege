package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.frame.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class SetManchesterSyntaxFrameActionHandler extends AbstractProjectChangeHandler<Optional<ManchesterSyntaxFrameParseError>, SetManchesterSyntaxFrameAction, SetManchesterSyntaxFrameResult> {

    @Nonnull
    private final GetManchesterSyntaxFrameActionHandler handler;

    @Nonnull
    private final RenderingManager renderer;

    @Nonnull
    private final ManchesterSyntaxChangeGeneratorFactory factory;

    @Inject
    public SetManchesterSyntaxFrameActionHandler(@Nonnull AccessManager accessManager,
                                                 @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                                 @Nonnull HasApplyChanges applyChanges,
                                                 @Nonnull GetManchesterSyntaxFrameActionHandler handler,
                                                 @Nonnull RenderingManager renderer,
                                                 @Nonnull ManchesterSyntaxChangeGeneratorFactory factory) {
        super(accessManager, eventManager, applyChanges);
        this.handler = handler;
        this.renderer = renderer;
        this.factory = factory;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetManchesterSyntaxFrameAction action) {
        return EDIT_ONTOLOGY;
    }

    @Override
    protected ChangeListGenerator<Optional<ManchesterSyntaxFrameParseError>> getChangeListGenerator(SetManchesterSyntaxFrameAction action,
                                                                                                    ExecutionContext executionContext) {
        return factory.create(
                renderer.getRendering(action.getSubject()),
                action.getFromRendering(),
                action.getToRendering(),
                action.getCommitMessage().orElse(""),
                action);
    }

    @Override
    protected SetManchesterSyntaxFrameResult createActionResult(ChangeApplicationResult<Optional<ManchesterSyntaxFrameParseError>> result,
                                                                SetManchesterSyntaxFrameAction action,
                                                                ExecutionContext executionContext,
                                                                EventList<ProjectEvent<?>> eventList) {

        if (result.getSubject().isPresent()) {
            throw new SetManchesterSyntaxFrameException(result.getSubject().get());
        }
        else {
            var ac = new GetManchesterSyntaxFrameAction(action.getProjectId(),
                                                        action.getSubject());
            GetManchesterSyntaxFrameResult frame = handler.execute(ac, executionContext);
            String reformattedFrame = frame.getFrameManchesterSyntax();
            return new SetManchesterSyntaxFrameResult(eventList, reformattedFrame);
        }
    }

    @Nonnull
    @Override
    public Class<SetManchesterSyntaxFrameAction> getActionClass() {
        return SetManchesterSyntaxFrameAction.class;
    }


}
