package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.SharingSetting;
import edu.stanford.bmir.protege.web.client.rpc.data.UserSharingSetting;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserIsSignedInValidator;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectSharingSettingsManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectAction;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class CreateNewProjectActionHandler  implements ActionHandler<CreateNewProjectAction, CreateNewProjectResult> {

    private final OWLAPIProjectManager pm;

    private final ProjectDetailsManager projectDetailsManager;

    private final ProjectSharingSettingsManager projectSharingSettingsManager;

    @Inject
    public CreateNewProjectActionHandler(OWLAPIProjectManager pm, ProjectDetailsManager projectDetailsManager, ProjectSharingSettingsManager projectSharingSettingsManager) {
        this.pm = pm;
        this.projectDetailsManager = projectDetailsManager;
        this.projectSharingSettingsManager = projectSharingSettingsManager;
    }

    @Override
    public Class<CreateNewProjectAction> getActionClass() {
        return CreateNewProjectAction.class;
    }

    @Override
    public RequestValidator<CreateNewProjectAction> getRequestValidator(CreateNewProjectAction action, RequestContext requestContext) {
        return UserIsSignedInValidator.get();
    }

    @Override
    public CreateNewProjectResult execute(CreateNewProjectAction action, ExecutionContext executionContext) {
        NewProjectSettings newProjectSettings = action.getNewProjectSettings();
        OWLAPIProject project = pm.createNewProject(newProjectSettings);
        ProjectId projectId = project.getProjectId();
        if (!isRegisteredProject(projectId)) {
            projectDetailsManager.registerProject(projectId, newProjectSettings);
            projectSharingSettingsManager.applyDefaultSharingSettings(projectId, executionContext.getUserId());
        }
        return new CreateNewProjectResult(projectDetailsManager.getProjectDetails(projectId));
    }

    private synchronized boolean isRegisteredProject(ProjectId projectId) {
        return projectDetailsManager.isExistingProject(projectId);
    }


}
