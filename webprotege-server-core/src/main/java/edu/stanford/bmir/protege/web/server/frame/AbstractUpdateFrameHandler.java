package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.EventTag;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.frame.EntityFrame;
import edu.stanford.bmir.protege.web.shared.frame.PlainEntityFrame;
import edu.stanford.bmir.protege.web.shared.frame.UpdateFrameAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public abstract class AbstractUpdateFrameHandler<A extends UpdateFrameAction, F extends EntityFrame> extends AbstractProjectActionHandler<A, Result> implements ActionHandler<A, Result> {

    @Nonnull
    private final EventManager<ProjectEvent<?>> eventManager;

    @Nonnull
    private final HasApplyChanges applyChanges;

    @Nonnull
    private final FrameChangeGeneratorFactory frameChangeGeneratorFactory;

    public AbstractUpdateFrameHandler(@Nonnull AccessManager accessManager,
                                      @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                      @Nonnull HasApplyChanges applyChanges,
                                      @Nonnull FrameChangeGeneratorFactory frameChangeGeneratorFactory) {
        super(accessManager);
        this.eventManager = eventManager;
        this.applyChanges = applyChanges;
        this.frameChangeGeneratorFactory = frameChangeGeneratorFactory;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(A action) {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    /**
     * Executes the specified action, against the specified project in the specified context.
     * @param action The action to be handled/executed
     * @param executionContext The {@link edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext} that should be
     * used to provide details such as the
     * {@link edu.stanford.bmir.protege.web.shared.user.UserId} of the user who requested the action be executed.
     * @return The result of the execution to be returned to the client.
     */
    @Nonnull
    @Override
    public Result execute(@Nonnull A action, @Nonnull ExecutionContext executionContext) {
        var from = action.getFrom();
        var to = action.getTo();
        final EventTag startTag = eventManager.getCurrentTag();
        if(from.equals(to)) {
            return createResponse(action.getTo(), eventManager.getEventsFromTag(startTag));
        }
        var userId = executionContext.getUserId();
        var frameUpdate = FrameUpdate.get(from, to);
        var changeGenerator = frameChangeGeneratorFactory.create(frameUpdate);
        applyChanges.applyChanges(userId, changeGenerator);
        var events = eventManager.getEventsFromTag(startTag);
        return createResponse(action.getTo(), events);
    }

    protected abstract Result createResponse(PlainEntityFrame to, EventList<ProjectEvent<?>> events);
}
