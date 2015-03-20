package edu.stanford.bmir.protege.web.server.dispatch;

import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/02/2013
 */
public abstract class AbstractProjectChangeHandler<T, A extends Action<R> & HasProjectId, R extends Result> extends AbstractHasProjectActionHandler<A, R> {

    @Inject
    public AbstractProjectChangeHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }

    @Override
    final protected R execute(A action, OWLAPIProject project, ExecutionContext executionContext) {
        EventTag tag = project.getEventManager().getCurrentTag();
        ChangeListGenerator<T> changeListGenerator = getChangeListGenerator(action, project, executionContext);
        final ChangeDescriptionGenerator<T> changeDescription = getChangeDescription(action, project, executionContext);
        ChangeApplicationResult<T> changeApplicationResult = project.applyChanges(executionContext.getUserId(), changeListGenerator, changeDescription);
        EventList<ProjectEvent<?>> eventList = project.getEventManager().getEventsFromTag(tag);
        return createActionResult(changeApplicationResult, action, project, executionContext, eventList);
    }

    protected abstract ChangeListGenerator<T> getChangeListGenerator(A action, OWLAPIProject project, ExecutionContext executionContext);

    protected abstract ChangeDescriptionGenerator<T> getChangeDescription(A action, OWLAPIProject project, ExecutionContext executionContext);

    protected abstract R createActionResult(ChangeApplicationResult<T> changeApplicationResult, A action, OWLAPIProject project, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList);



}
