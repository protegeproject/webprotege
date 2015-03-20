package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedFromTrashEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.RemoveProjectsFromTrashAction;
import edu.stanford.bmir.protege.web.shared.project.RemoveProjectsFromTrashResult;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class RemoveProjectsFromTrashActionHandler implements ActionHandler<RemoveProjectsFromTrashAction, RemoveProjectsFromTrashResult> {

    private ProjectDetailsManager projectDetailsManager;

    @Inject
    public RemoveProjectsFromTrashActionHandler(ProjectDetailsManager projectDetailsManager) {
        this.projectDetailsManager = projectDetailsManager;
    }

    @Override
    public Class<RemoveProjectsFromTrashAction> getActionClass() {
        return RemoveProjectsFromTrashAction.class;
    }

    @Override
    public RequestValidator<RemoveProjectsFromTrashAction> getRequestValidator(RemoveProjectsFromTrashAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public RemoveProjectsFromTrashResult execute(RemoveProjectsFromTrashAction action, ExecutionContext executionContext) {
        List<ProjectMovedFromTrashEvent> events = new ArrayList<ProjectMovedFromTrashEvent>();
        for(ProjectId projectId : action.getProjectIds()) {
            projectDetailsManager.setInTrash(projectId, false);
            events.add(new ProjectMovedFromTrashEvent(projectId));
        }
        return new RemoveProjectsFromTrashResult(new EventList<ProjectMovedFromTrashEvent>(EventTag.getFirst(), events, EventTag.getFirst()));
    }
}
