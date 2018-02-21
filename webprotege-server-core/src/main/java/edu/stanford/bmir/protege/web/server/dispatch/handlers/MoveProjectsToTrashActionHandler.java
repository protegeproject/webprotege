package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.EventTag;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedToTrashEvent;
import edu.stanford.bmir.protege.web.shared.project.MoveProjectsToTrashAction;
import edu.stanford.bmir.protege.web.shared.project.MoveProjectsToTrashResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class MoveProjectsToTrashActionHandler implements ApplicationActionHandler<MoveProjectsToTrashAction, MoveProjectsToTrashResult> {

    private ProjectDetailsManager projectDetailsManager;

    @Inject
    public MoveProjectsToTrashActionHandler(ProjectDetailsManager projectDetailsManager) {
        this.projectDetailsManager = projectDetailsManager;
    }

    @Nonnull
    @Override
    public Class<MoveProjectsToTrashAction> getActionClass() {
        return MoveProjectsToTrashAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull MoveProjectsToTrashAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public MoveProjectsToTrashResult execute(@Nonnull MoveProjectsToTrashAction action, @Nonnull ExecutionContext executionContext) {
        List<ProjectMovedToTrashEvent> events = new ArrayList<>();
        ProjectId projectId = action.getProjectId();
            projectDetailsManager.setInTrash(projectId, true);
            events.add(new ProjectMovedToTrashEvent(projectId));

        return new MoveProjectsToTrashResult(new EventList<>(EventTag.getFirst(), events, EventTag.getFirst()));
    }
}
