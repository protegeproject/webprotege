package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.LoadProjectAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.LoadProjectResult;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.smi.protege.server.metaproject.Operation;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class LoadProjectActionHandler implements ActionHandler<LoadProjectAction, LoadProjectResult> {

    private ProjectDetailsManager projectDetailsManager;

    @Inject
    public LoadProjectActionHandler(ProjectDetailsManager projectDetailsManager) {
        this.projectDetailsManager = projectDetailsManager;
    }

    @Override
    public Class<LoadProjectAction> getActionClass() {
        return LoadProjectAction.class;
    }

    @Override
    public RequestValidator<LoadProjectAction> getRequestValidator(LoadProjectAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    public LoadProjectResult execute(final LoadProjectAction action, ExecutionContext executionContext) {
        // Load project in parallel (as we don't return it, but want it ready for further calls).
        final WebProtegeLogger webProtegeLogger = WebProtegeLoggerManager.get(LoadProjectActionHandler.class);
        long t0 = System.currentTimeMillis();
        webProtegeLogger.info("Loading project: " + action.getProjectId());
        OWLAPIProjectManager pm = OWLAPIProjectManager.getProjectManager();
        pm.getProject(action.getProjectId());
        long t1 = System.currentTimeMillis();
        webProtegeLogger.info(".... loaded project in " + (t1 - t0) + " ms");
        final ProjectId projectId = action.getProjectId();//project.getProjectId();

        ProjectDetails projectDetails = projectDetailsManager.getProjectDetails(projectId);

        Collection<Operation> ops = MetaProjectManager.getManager().getAllowedOperations(projectId.getId(), executionContext.getUserId().getUserName());
        PermissionsSet.Builder builder = PermissionsSet.builder();
        for (Operation op : ops) {
            builder.addPermission(Permission.getPermission(op.getName()));
        }
        return new LoadProjectResult(executionContext.getUserId(), builder.build(), projectDetails);
    }
}
