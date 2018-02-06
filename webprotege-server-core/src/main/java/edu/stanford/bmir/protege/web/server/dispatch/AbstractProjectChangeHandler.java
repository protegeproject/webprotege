package edu.stanford.bmir.protege.web.server.dispatch;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.EventTag;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/02/2013
 */
public abstract class AbstractProjectChangeHandler<T, A extends ProjectAction<R>, R extends Result> extends AbstractProjectActionHandler<A, R> {

    @Nonnull
    private final EventManager<ProjectEvent<?>> eventManager;

    @Nonnull
    private final HasApplyChanges applyChanges;

    @Nonnull
    public AbstractProjectChangeHandler(@Nonnull AccessManager accessManager,
                                        @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                        @Nonnull HasApplyChanges applyChanges) {
        super(accessManager);
        this.eventManager = checkNotNull(eventManager);
        this.applyChanges = checkNotNull(applyChanges);
    }

    @Nonnull
    @Override
    public final R execute(@Nonnull A action, @Nonnull ExecutionContext executionContext) {
        EventTag tag = eventManager.getCurrentTag();
        ChangeListGenerator<T> changeListGenerator = getChangeListGenerator(action, executionContext);
        ChangeApplicationResult<T> result = applyChanges.applyChanges(executionContext.getUserId(),
                                                                                       changeListGenerator);
        EventList<ProjectEvent<?>> eventList = eventManager.getEventsFromTag(tag);
        return createActionResult(result, action, executionContext, eventList);
    }

    protected abstract ChangeListGenerator<T> getChangeListGenerator(A action,
                                                                     ExecutionContext executionContext);

    protected abstract R createActionResult(ChangeApplicationResult<T> changeApplicationResult,
                                            A action,
                                            ExecutionContext executionContext,
                                            EventList<ProjectEvent<?>> eventList);



}
