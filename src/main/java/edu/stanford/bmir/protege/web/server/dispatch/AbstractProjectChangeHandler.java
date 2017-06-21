package edu.stanford.bmir.protege.web.server.dispatch;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/02/2013
 */
public abstract class AbstractProjectChangeHandler<T, A extends ProjectAction<R> & HasProjectId, R extends Result> extends AbstractHasProjectActionHandler<A, R> {

    @Nonnull
    private final EventManager<ProjectEvent<?>> eventManager;

    @Nonnull
    private final HasApplyChanges applyChanges;

    @Nonnull
    public AbstractProjectChangeHandler(@Nonnull AccessManager accessManager,
                                        @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                        @Nonnull HasApplyChanges applyChanges) {
        super(accessManager);
        this.eventManager = eventManager;
        this.applyChanges = applyChanges;
    }

    @Override
    public final R execute(A action, ExecutionContext executionContext) {
        EventTag tag = eventManager.getCurrentTag();
        ChangeListGenerator<T> changeListGenerator = getChangeListGenerator(action, executionContext);
        final ChangeDescriptionGenerator<T> changeDescription = getChangeDescription(action, executionContext);
        ChangeApplicationResult<T> changeApplicationResult = applyChanges.applyChanges(executionContext.getUserId(), changeListGenerator, changeDescription);
        EventList<ProjectEvent<?>> eventList = eventManager.getEventsFromTag(tag);
        return createActionResult(changeApplicationResult, action, executionContext, eventList);
    }

    protected abstract ChangeListGenerator<T> getChangeListGenerator(A action,
                                                                     ExecutionContext executionContext);

    protected abstract ChangeDescriptionGenerator<T> getChangeDescription(A action,
                                                                          ExecutionContext executionContext);

    protected abstract R createActionResult(ChangeApplicationResult<T> changeApplicationResult,
                                            A action,
                                            ExecutionContext executionContext,
                                            EventList<ProjectEvent<?>> eventList);



}
