package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectMetadataManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedToTrashEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.project.MoveProjectsToTrashAction;
import edu.stanford.bmir.protege.web.shared.project.MoveProjectsToTrashResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class MoveProjectsToTrashActionHandler implements ActionHandler<MoveProjectsToTrashAction, MoveProjectsToTrashResult> {

    @Override
    public Class<MoveProjectsToTrashAction> getActionClass() {
        return MoveProjectsToTrashAction.class;
    }

    @Override
    public RequestValidator<MoveProjectsToTrashAction> getRequestValidator(MoveProjectsToTrashAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public MoveProjectsToTrashResult execute(MoveProjectsToTrashAction action, ExecutionContext executionContext) {
        List<ProjectMovedToTrashEvent> events = new ArrayList<ProjectMovedToTrashEvent>();
        for(ProjectId projectId : action.getProjectIds()) {
            OWLAPIProjectMetadataManager.getManager().setInTrash(projectId, true);
            events.add(new ProjectMovedToTrashEvent(projectId));
        }

        return new MoveProjectsToTrashResult(new EventList<ProjectMovedToTrashEvent>(EventTag.getFirst(), events, EventTag.getFirst()));
    }
}
