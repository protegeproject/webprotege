package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.LoadProjectAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.LoadProjectResult;
import edu.stanford.bmir.protege.web.server.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectMetadataManager;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.smi.protege.server.metaproject.Operation;

import java.util.Collection;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class LoadProjectActionHandler implements ActionHandler<LoadProjectAction, LoadProjectResult> {

    private static final UserHasProjectReadPermissionValidator<LoadProjectAction,LoadProjectResult> VALIDATOR = new UserHasProjectReadPermissionValidator<LoadProjectAction, LoadProjectResult>();

    @Override
    public Class<LoadProjectAction> getActionClass() {
        return LoadProjectAction.class;
    }

    @Override
    public RequestValidator<LoadProjectAction> getRequestValidator(LoadProjectAction action, RequestContext requestContext) {
        return VALIDATOR;
    }

    @Override
    public LoadProjectResult execute(LoadProjectAction action, ExecutionContext executionContext) {
        long t0 = System.currentTimeMillis();
        // We don't actually need to load the project here - leave it to be loaded on demand.  This way, things
        // can be done in parallel (the UI is the slowest part).

//        OWLAPIProjectManager pm = OWLAPIProjectManager.getProjectManager();
//        OWLAPIProject project = pm.getProject(action.getProjectId());
        final ProjectId projectId = action.getProjectId();//project.getProjectId();

        final OWLAPIProjectMetadataManager manager = OWLAPIProjectMetadataManager.getManager();
        ProjectDetails projectDetails = manager.getProjectDetails(projectId);

        Collection<Operation> ops = MetaProjectManager.getManager().getAllowedOperations(projectId.getProjectName(), executionContext.getUserId().getUserName());
        PermissionsSet.Builder builder = PermissionsSet.builder();
        for(Operation op : ops) {
            builder.addPermission(Permission.getPermission(op.getName()));
        }
        long t1 = System.currentTimeMillis();
        System.out.println("Processed load call in " + (t1 - t0));
        return new LoadProjectResult(executionContext.getUserId(), builder.build(), projectDetails);
    }
}
