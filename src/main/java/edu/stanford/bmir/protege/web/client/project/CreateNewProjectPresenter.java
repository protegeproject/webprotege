package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.progress.ProgressMonitor;
import edu.stanford.bmir.protege.web.client.projectmanager.ProjectCreatedEvent;
import edu.stanford.bmir.protege.web.client.upload.FileUploadResponse;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.UPLOAD_PROJECT;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Nov 2017
 */
public class CreateNewProjectPresenter {

    interface ProjectCreatedHandler {
        void handleProjectCreated();
    }

    @Nonnull
    private final CreateNewProjectView view;

    @Nonnull
    private final LoggedInUserManager loggedInUserManager;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final EventBus eventBus;

    @Inject
    public CreateNewProjectPresenter(@Nonnull CreateNewProjectView view,
                                     @Nonnull LoggedInUserManager loggedInUserManager,
                                     @Nonnull DispatchServiceManager dispatchServiceManager,
                                     @Nonnull EventBus eventBus) {
        this.view = checkNotNull(view);
        this.loggedInUserManager = checkNotNull(loggedInUserManager);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.eventBus = checkNotNull(eventBus);
    }

    @Nonnull
    public CreateNewProjectView getView() {
        return view;
    }

    public void start() {
        view.clear();
        if (loggedInUserManager.isAllowedApplicationAction(UPLOAD_PROJECT)) {
            view.setFileUploadEnabled(true);
        }
        else {
            view.setFileUploadEnabled(false);
        }
    }

    public boolean validate() {
        if (view.getProjectName().isEmpty()) {
            MessageBox.showAlert("Project name missing", "Please enter a project name");
            return false;
        }
        return true;
    }


    public void submitCreateProjectRequest(ProjectCreatedHandler handler) {
        if (view.isFileUploadSpecified()) {
            GWT.log("[CreateNewProjectPresenter] Creating project from existing sources");
            uploadSourcesAndCreateProject(handler);
        }
        else {
            GWT.log("[CreateNewProjectPresenter] Creating empty project");
            createEmptyProject(handler);
        }
    }

    private void createEmptyProject(ProjectCreatedHandler projectCreatedHandler) {
        NewProjectSettings newProjectSettings = new NewProjectSettings(
                loggedInUserManager.getLoggedInUserId(),
                view.getProjectName(),
                view.getProjectDescription());
        submitCreateNewProjectRequest(newProjectSettings, projectCreatedHandler);
    }


    private void uploadSourcesAndCreateProject(@Nonnull ProjectCreatedHandler projectCreatedHandler) {
        checkNotNull(projectCreatedHandler);
        String postUrl = GWT.getModuleBaseURL() + "submitfile";
        GWT.log("[CreateNewProjectPresenter] File upload submission URL: " + postUrl);
        view.setFileUploadPostUrl(postUrl);
        ProgressMonitor.get().showProgressMonitor("Uploading sources", "Uploading file");
        view.setSubmitCompleteHandler(event -> {
            GWT.log("[CreateNewProjectPresenter] Form submission complete.  Sources have been uploaded.");
            ProgressMonitor.get().hideProgressMonitor();
            handleSourcesUploadComplete(event, projectCreatedHandler);
        });
        GWT.log("[CreateNewProjectPresenter] Submitting existing sources as form data.");
        view.submitFormData();
    }

    private void handleSourcesUploadComplete(FormPanel.SubmitCompleteEvent event,
                                             ProjectCreatedHandler projectCreatedHandler) {
        FileUploadResponse response = new FileUploadResponse(event.getResults());
        if (response.wasUploadAccepted()) {
            DocumentId documentId = response.getDocumentId();
            NewProjectSettings newProjectSettings = new NewProjectSettings(
                    loggedInUserManager.getLoggedInUserId(),
                    view.getProjectName(),
                    view.getProjectDescription(),
                    documentId
            );
            submitCreateNewProjectRequest(newProjectSettings, projectCreatedHandler);
        }
        else {
            MessageBox.showAlert("Upload Failed", response.getUploadRejectedMessage());
        }
    }

    private void submitCreateNewProjectRequest(@Nonnull NewProjectSettings newProjectSettings,
                                               @Nonnull ProjectCreatedHandler projectCreatedHandler) {
        dispatchServiceManager.execute(new CreateNewProjectAction(newProjectSettings),
                new DispatchServiceCallbackWithProgressDisplay<CreateNewProjectResult>() {
                    @Override
                    public String getProgressDisplayTitle() {
                        return "Creating project";
                    }

                    @Override
                    public String getProgressDisplayMessage() {
                        return "Please wait.";
                    }

                    @Override
                    public void handleSuccess(CreateNewProjectResult result) {
                        projectCreatedHandler.handleProjectCreated();
                        eventBus.fireEvent(new ProjectCreatedEvent(result.getProjectDetails()));
                    }

                    @Override
                    public void handleExecutionException(Throwable cause) {
                        if (cause instanceof PermissionDeniedException) {
                            MessageBox.showMessage("You do not have permission to create new projects");
                        }
                        else if (cause instanceof ProjectAlreadyRegisteredException) {
                            ProjectAlreadyRegisteredException ex = (ProjectAlreadyRegisteredException) cause;
                            String projectName = ex.getProjectId().getId();
                            MessageBox.showMessage("The project name " + projectName + " is already registered.  Please try a different name.");
                        }
                        else if (cause instanceof ProjectDocumentExistsException) {
                            ProjectDocumentExistsException ex = (ProjectDocumentExistsException) cause;
                            String projectName = ex.getProjectId().getId();
                            MessageBox.showMessage("There is already a non-empty project on the server with the id " + projectName + ".  This project has NOT been overwritten.  Please contact the administrator to resolve this issue.");
                        }
                        else {
                            MessageBox.showMessage(cause.getMessage());
                        }
                    }
                });
    }

    public NewProjectInfo getNewProjectInfo() {
        return new NewProjectInfo(view.getProjectName(), view.getProjectDescription());
    }
}
