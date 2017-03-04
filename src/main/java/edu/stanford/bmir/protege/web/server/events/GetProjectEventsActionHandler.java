package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.events.ProjectEventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class GetProjectEventsActionHandler implements ActionHandler<GetProjectEventsAction, GetProjectEventsResult> {

    public final WebProtegeLogger logger;

    private ProjectManager projectManager;

    @Inject
    public GetProjectEventsActionHandler(ProjectManager projectManager, WebProtegeLogger logger) {
        this.projectManager = checkNotNull(projectManager);
        this.logger = checkNotNull(logger);
    }

    @Override
    public Class<GetProjectEventsAction> getActionClass() {
        return GetProjectEventsAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetProjectEventsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetProjectEventsResult execute(GetProjectEventsAction action, ExecutionContext executionContext) {
        final EventTag sinceTag = action.getSinceTag();
        final ProjectId projectId = action.getProjectId();
        if(!projectManager.isActive(projectId)) {
            return getEmptyResult(projectId, sinceTag);
        }
        Optional<Project> project = projectManager.getProjectIfActive(projectId);
        if(!project.isPresent()) {
            return getEmptyResult(projectId, sinceTag);
        }
        // TODO: FIX THIS.  NEEDS TO GO ELSEWHERE
//        project.getProjectAccessManager().logAccessForUser(action.getUserId());
        EventManager<ProjectEvent<?>> eventManager = project.get().getEventManager();
        EventList<ProjectEvent<?>> eventList = eventManager.getEventsFromTag(sinceTag);
        ProjectEventList projectEventList = ProjectEventList.builder(eventList.getStartTag(), projectId, eventList.getEndTag()).addEvents(eventList.getEvents()).build();
        return  new GetProjectEventsResult(projectEventList);
    }

    private static GetProjectEventsResult getEmptyResult(ProjectId projectId, EventTag sinceTag) {
        return new GetProjectEventsResult(ProjectEventList.builder(sinceTag, projectId, sinceTag).build());
    }
}
