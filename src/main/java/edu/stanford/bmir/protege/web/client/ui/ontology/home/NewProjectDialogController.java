package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerService;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.*;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.ProjectCreatedEvent;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyRegisteredException;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectDocumentExistsException;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 */
public class NewProjectDialogController extends WebProtegeOKCancelDialogController<NewProjectInfo> {

    public static final String TITLE = "Create project";

    private NewProjectInfoWidget widget = new NewProjectInfoWidget();

    public NewProjectDialogController() {
        super(TITLE);
        this.widget = new NewProjectInfoWidget();
        for(WebProtegeDialogValidator validator : widget.getDialogValidators()) {
            addDialogValidator(validator);
        }
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<NewProjectInfo>() {
            public void handleHide(NewProjectInfo data, WebProtegeDialogCloser closer) {
                handleCreateNewProject(data);
                closer.hide();
            }
        });
    }

    private void handleCreateNewProject(NewProjectInfo data) {
        UserId userId = Application.get().getUserId();
        if(userId.isGuest()) {
            throw new RuntimeException("User is guest.  Guest users are not allowed to create projects.");
        }
        NewProjectSettings newProjectSettings = new NewProjectSettings(userId, data.getProjectName(), data.getProjectDescription(), data.getProjectType());
        ProjectManagerServiceAsync projectManagerService = GWT.create(ProjectManagerService.class);
        projectManagerService.createNewProject(newProjectSettings, new AsyncCallback<ProjectDetails>() {
            public void onFailure(Throwable caught) {
                handleCreateProjectFailure(caught);
            }

            public void onSuccess(ProjectDetails result) {
                handleCreateProjectSuccess(result);
            }
        });
    }

    private void handleCreateProjectFailure(Throwable caught) {
        if(caught instanceof NotSignedInException) {
            MessageBox.alert("You must be signed in to create new projects");
        }
        else if(caught instanceof ProjectAlreadyRegisteredException) {
            ProjectAlreadyRegisteredException ex = (ProjectAlreadyRegisteredException) caught;
            String projectName = ex.getProjectId().getId();
            MessageBox.alert("The project name " + projectName + " is already registered.  Please try a different name.");
        }
        else if(caught instanceof ProjectDocumentExistsException) {
            ProjectDocumentExistsException ex = (ProjectDocumentExistsException) caught;
            String projectName = ex.getProjectId().getId();
            MessageBox.alert("There is already a non-empty project on the server with the id " + projectName + ".  This project has NOT been overwritten.  Please contact the administrator to resolve this issue.");
        }
        else {
            MessageBox.alert(caught.getMessage());
        }
    }

    private void handleCreateProjectSuccess(ProjectDetails projectDetails) {
        EventBusManager.getManager().postEvent(new ProjectCreatedEvent(projectDetails));
    }


    public Widget getWidget() {
        return widget;
    }

    public Optional<Focusable> getInitialFocusable() {
        return widget.getInitialFocusable();
    }

    public NewProjectInfo getData() {
        return widget.getNewProjectInfo();
    }
}
