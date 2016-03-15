package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionsManager;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsAction;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2013
 */
public class GetAvailableProjectsHandler implements ActionHandler<GetAvailableProjectsAction, GetAvailableProjectsResult> {

    private final ProjectPermissionsManager projectPermissionsManager;

    @Inject
    public GetAvailableProjectsHandler(ProjectPermissionsManager projectPermissionsManager) {
        this.projectPermissionsManager = projectPermissionsManager;
    }

    @Override
    public Class<GetAvailableProjectsAction> getActionClass() {
        return GetAvailableProjectsAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetAvailableProjectsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetAvailableProjectsResult execute(GetAvailableProjectsAction action, ExecutionContext executionContext) {
        UserId userId = executionContext.getUserId();
        List<ProjectDetails> details = projectPermissionsManager.getReadableProjects(userId);
        Collections.sort(details);
        return new GetAvailableProjectsResult(details);
    }
}
