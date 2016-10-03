package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.client.project.NewProjectSettings;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserIsSignedInValidator;
import edu.stanford.bmir.protege.web.server.sharing.ProjectSharingSettingsManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectAction;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSetting;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public RequestValidator getRequestValidator(CreateNewProjectAction action, RequestContext requestContext) {
        return new UserIsSignedInValidator(requestContext.getUserId());
    }

    @Override
    public CreateNewProjectResult execute(CreateNewProjectAction action, ExecutionContext executionContext) {
        try {
            NewProjectSettings newProjectSettings = action.getNewProjectSettings();
            OWLAPIProject project = pm.createNewProject(newProjectSettings);
            ProjectId projectId = project.getProjectId();
            if (!projectDetailsManager.isExistingProject(projectId)) {
                projectDetailsManager.registerProject(projectId, newProjectSettings);
                UserId userId = executionContext.getUserId();
                applyOwnerPermissions(projectId, userId);
            }
            return new CreateNewProjectResult(projectDetailsManager.getProjectDetails(projectId));
        } catch (OWLOntologyCreationException | OWLOntologyStorageException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void applyOwnerPermissions(ProjectId projectId, UserId userId) {
        List<SharingSetting> sharingSettings = new ArrayList<>();
        PersonId personId = PersonId.of(userId);
        sharingSettings.add(new SharingSetting(personId, SharingPermission.MANAGE));
        projectSharingSettingsManager.setProjectSharingSettings(
                new ProjectSharingSettings(projectId, Optional.empty(), sharingSettings));
    }


}
