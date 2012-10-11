package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerService;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.client.ui.library.common.Refreshable;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 */
public class NewProjectDialog extends WebProtegeDialog<NewProjectInfo> {
    
    private Refreshable [] refreshables;

    public NewProjectDialog(final Refreshable ... refreshables) {
        super(new NewProjectDialogController());
        this.refreshables = refreshables;
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<NewProjectInfo>() {
            public void handleHide(NewProjectInfo data, WebProtegeDialogCloser closer) {
                handleCreateNewProject(data);
                closer.hide();
            }
        });
    }

    private void handleCreateNewProject(NewProjectInfo data) {
        String userName = GlobalSettings.getGlobalSettings().getUserName();
        if(userName == null) {
            throw new RuntimeException("User name is null");
        }
        NewProjectSettings newProjectSettings = new NewProjectSettings(UserId.getUserId(userName), data.getProjectName(), data.getProjectDescription(), data.getProjectType());
        ProjectManagerServiceAsync projectManagerService = GWT.create(ProjectManagerService.class);
        projectManagerService.createNewProject(newProjectSettings, new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                handleCreateProjectFailure(caught);
            }

            public void onSuccess(Void result) {
                handleCreateProjectSuccess();
            }
        });
    }

    private void handleCreateProjectFailure(Throwable caught) {
        if(caught instanceof NotSignedInException) {
            MessageBox.alert("You must be signed in to create new projects");
        }
        else if(caught instanceof ProjectAlreadyRegisteredException) {
            ProjectAlreadyRegisteredException ex = (ProjectAlreadyRegisteredException) caught;
            String projectName = ex.getProjectId().getProjectName();
            MessageBox.alert("The project name " + projectName + " is already registered.  Please try a different name.");
        }
        else if(caught instanceof ProjectDocumentExistsException) {
            ProjectDocumentExistsException ex = (ProjectDocumentExistsException) caught;
            String projectName = ex.getProjectId().getProjectName();
            MessageBox.alert("There is already a non-empty project on the server which is named " + projectName + ".  This project has NOT been overwritten.  Please contact the administrator to resolve this issue.");
        }
        else {
            MessageBox.alert(caught.getMessage());
        }
    }

    private void handleCreateProjectSuccess() {
        // TODO: There must be a nicer way of doing this
        broadcastRefresh(refreshables);
    }

    private void broadcastRefresh(Refreshable[] refreshables) {
        for(Refreshable refreshable : refreshables) {
            refreshable.refresh();
        }
    }
}
