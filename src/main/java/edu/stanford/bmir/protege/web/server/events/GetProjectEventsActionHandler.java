package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.ProjectEventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class GetProjectEventsActionHandler implements ActionHandler<GetProjectEventsAction, GetProjectEventsResult> {

    @Override
    public Class<GetProjectEventsAction> getActionClass() {
        return GetProjectEventsAction.class;
    }

    @Override
    public RequestValidator<GetProjectEventsAction> getRequestValidator(GetProjectEventsAction action, RequestContext requestContext) {
        return new NullValidator<GetProjectEventsAction, GetProjectEventsResult>();
    }

    @Override
    public GetProjectEventsResult execute(GetProjectEventsAction action, ExecutionContext executionContext) {
        OWLAPIProject project = OWLAPIProjectManager.getProjectManager().getProject(action.getProjectId());
        project.getProjectAccessManager().logAccessForUser(action.getUserId());
        EventManager<ProjectEvent<?>> eventManager = project.getEventManager();
        EventList<ProjectEvent<?>> eventList = eventManager.getEventsFromTag(action.getSinceTag());
        ProjectEventList projectEventList = ProjectEventList.builder(eventList.getStartTag(), action.getProjectId(), eventList.getEndTag()).addEvents(eventList.getEvents()).build();
        return  new GetProjectEventsResult(projectEventList);
    }
}
