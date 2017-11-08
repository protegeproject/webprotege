package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.client.project.NewProjectSettings;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ApplicationResource;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ApplicationPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.CompositeRequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserIsSignedInValidator;
import edu.stanford.bmir.protege.web.server.sharing.ProjectSharingSettingsManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectAction;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import javax.inject.Inject;
import java.io.IOException;

import static edu.stanford.bmir.protege.web.server.access.Subject.forAnySignedInUser;
import static edu.stanford.bmir.protege.web.server.access.Subject.forUser;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_EMPTY_PROJECT;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.UPLOAD_PROJECT;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInRole.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class CreateNewProjectActionHandler implements ApplicationActionHandler<CreateNewProjectAction, CreateNewProjectResult> {

    private final ProjectManager pm;

    private final ProjectDetailsManager projectDetailsManager;

    private final ProjectSharingSettingsManager projectSharingSettingsManager;

    private final AccessManager accessManager;

    @Inject
    public CreateNewProjectActionHandler(ProjectManager pm,
                                         ProjectDetailsManager projectDetailsManager,
                                         ProjectSharingSettingsManager projectSharingSettingsManager,
                                         AccessManager accessManager) {
        this.pm = pm;
        this.projectDetailsManager = projectDetailsManager;
        this.projectSharingSettingsManager = projectSharingSettingsManager;
        this.accessManager = accessManager;
    }

    @Override
    public Class<CreateNewProjectAction> getActionClass() {
        return CreateNewProjectAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(CreateNewProjectAction action, RequestContext requestContext) {
        return new CompositeRequestValidator(
                new UserIsSignedInValidator(requestContext.getUserId()),
                new ApplicationPermissionValidator(
                        accessManager,
                        requestContext.getUserId(),
                        CREATE_EMPTY_PROJECT)
        );
    }

    @Override
    public CreateNewProjectResult execute(CreateNewProjectAction action, ExecutionContext executionContext) {
        try {
            if(!accessManager.hasPermission(forUser(executionContext.getUserId()), ApplicationResource.get(), CREATE_EMPTY_PROJECT)) {
                throw new PermissionDeniedException("You do not have permission to create new projects");
            }
            NewProjectSettings newProjectSettings = action.getNewProjectSettings();
            if(newProjectSettings.hasSourceDocument()) {
                if(!accessManager.hasPermission(forUser(executionContext.getUserId()), ApplicationResource.get(), UPLOAD_PROJECT)) {
                    throw new PermissionDeniedException("You do not have permission to upload projects");
                }
            }
            ProjectId projectId = pm.createNewProject(newProjectSettings);
            if (!projectDetailsManager.isExistingProject(projectId)) {
                projectDetailsManager.registerProject(projectId, newProjectSettings);
                UserId userId = executionContext.getUserId();
                applyDefaultPermissions(projectId, userId);
            }
            return new CreateNewProjectResult(projectDetailsManager.getProjectDetails(projectId));
        } catch (OWLOntologyCreationException | OWLOntologyStorageException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void applyDefaultPermissions(ProjectId projectId, UserId userId) {
        ProjectResource projectResource = new ProjectResource(projectId);
        // Owner is manager
        accessManager.setAssignedRoles(forUser(userId),
                                       projectResource,
                                       asList(CAN_MANAGE.getRoleId(), PROJECT_DOWNLOADER.getRoleId()));
        // Any signed in user can edit the layout
        accessManager.setAssignedRoles(forAnySignedInUser(),
                                       projectResource,
                                       singleton(LAYOUT_EDITOR.getRoleId()));
    }


}
