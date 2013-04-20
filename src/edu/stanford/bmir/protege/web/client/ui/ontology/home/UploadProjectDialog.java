package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerService;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.client.ui.library.common.Refreshable;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.ProjectCreatedEvent;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyRegisteredException;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectDocumentExistsException;
import edu.stanford.bmir.protege.web.shared.user.UserId;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2012
 */
public class UploadProjectDialog extends WebProtegeDialog<UploadFileInfo> {

    public static final String PROGRESS_DIALOG_TITLE = "Uploading";

    public UploadProjectDialog(final Refreshable... refreshables) {
        super(new UploadProjectDialogController());
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<UploadFileInfo>() {
            public void handleHide(final UploadFileInfo data, final WebProtegeDialogCloser closer) {
                UIUtil.showLoadProgessBar(PROGRESS_DIALOG_TITLE, "Uploading file...");
                data.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
                    public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                        UIUtil.hideLoadProgessBar();
                        handleSubmissionComplete(data, event, refreshables);
                        closer.hide();

                    }
                });
                data.submit();
            }
        });
    }

    private void refreshRefreshables(Refreshable[] refreshables) {
        for(Refreshable refreshable : refreshables) {
            refreshable.refresh();
        }
    }


    private void handleSubmissionComplete(UploadFileInfo data, FormPanel.SubmitCompleteEvent event, Refreshable ... refreshables) {
        UploadFileResult result = new UploadFileResult(event.getResults());
        if(result.wasUploadAccepted()) {
            createProjectFromUpload(data, result, refreshables);
        }
        else {
            displayProjectUploadError(result);
        }


        


        
    }

    private void displayProjectUploadError(UploadFileResult result) {
        MessageBox.alert("Upload failed", result.getUploadRejectedMessage());
    }

    private void createProjectFromUpload(UploadFileInfo data, UploadFileResult result, final Refreshable ... refreshables) {
        UIUtil.showLoadProgessBar(PROGRESS_DIALOG_TITLE, "Creating project...");
        ProjectManagerServiceAsync projectManagerService = GWT.create(ProjectManagerService.class);
        UserId userId = Application.get().getUserId();
        DocumentId documentId = result.getDocumentId();
        String projectName = data.getProjectSettings().getProjectName();
        String projectDescription = data.getProjectSettings().getProjectDescription();
        ProjectType projectType = data.getProjectSettings().getProjectType();
        NewProjectSettings newProjectSettings = new NewProjectSettings(userId, projectName, projectDescription, projectType, documentId);

        projectManagerService.createNewProject(newProjectSettings, new AsyncCallback<ProjectDetails>() {
            public void onFailure(Throwable caught) {
                UIUtil.hideLoadProgessBar();
                caught.printStackTrace();
                handleCreateProjectFailure(caught);
                refreshRefreshables(refreshables);
            }

            public void onSuccess(ProjectDetails result) {
                UIUtil.hideLoadProgessBar();
                MessageBox.alert("Ontology imported", "Ontology successfully uploaded.");
                EventBusManager.getManager().postEvent(new ProjectCreatedEvent(result));
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
                MessageBox.alert("The project name " + projectName + " is already taken.  Please try a different name.");
            }
            else if(caught instanceof ProjectDocumentExistsException) {
                ProjectDocumentExistsException ex = (ProjectDocumentExistsException) caught;
                String projectName = ex.getProjectId().getId();
                MessageBox.alert("There is already a non-empty project on the server which is named " + projectName + ".  This project has NOT been overwritten.  Please contact the administrator to resolve this issue.");
            }
            else {
                MessageBox.alert(caught.getMessage());
            }
        }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /////
    /////   A class that parses a raw result from a file upload.  The result is assumed to be formatted in JSON.
    /////
    
    private class UploadFileResult {

        public static final String UNKNOWN_REASON_MESSAGE = "Unknown reason";

        private String rawResult;

        private JSONObject jsonObject;

        private UploadFileResult(String rawResult) {
            this.rawResult = rawResult;
            jsonObject = parseResult();
        }
        
        private JSONObject parseResult() {
            JSONValue value = JSONParser.parseLenient(trimPreTags(rawResult));
            JSONObject object = value.isObject();
            return object;
        }

        /**
         * The result from a form submission sometimes seems to be surrounded with &lt;pre&gt; tags.  This method
         * strips these tags of the result if the are present.
         * @param result The raw result.
         * @return The raw result minus the pre tags.
         */
        private String trimPreTags(String result) {
            if(result.startsWith("<pre>")) {
                return result.substring(5, result.length() - 6);
            }
            else {
                return result;
            }
        }

        /**
         * If the upload was rejected the method gets an error message which explains why the upload was rejected.
         * @return A string representing the message (not <code>null</code>).
         */
        public String getUploadRejectedMessage() {
            if(jsonObject == null) {
                return "Invalid response";
            }
            JSONValue value = jsonObject.get(FileUploadResponseAttributes.UPLOAD_REJECTED_MESSAGE_ATTRIBUTE.name());
            if(value == null) {
                return UNKNOWN_REASON_MESSAGE;
            }
            JSONString string = value.isString();
            if(string == null) {
                return UNKNOWN_REASON_MESSAGE;
            }
            return string.stringValue();
        }

        /**
         * Determines if the upload was accepted.
         * @return <code>true</code> if the upload was accepted, otherwise <code>false</code>.
         */
        public boolean wasUploadAccepted() {
            if(jsonObject == null) {
                return false;
            }
            JSONValue value = jsonObject.get(FileUploadResponseAttributes.RESPONSE_TYPE_ATTRIBUTE.name());
            if(value == null) {
                return false;
            }
            JSONString string = value.isString();
            if(string == null) {
                return false;
            }
            return string.stringValue().equals(FileUploadResponseAttributes.RESPONSE_TYPE_VALUE_UPLOAD_ACCEPTED.name());
        }


        /**
         * For an upload that was accepted ({@link #wasUploadAccepted()} returns <code>true</code>) this method gets
         * the documentId of the upload.
         * @return The document Id
         */
        public DocumentId getDocumentId() {
            if(jsonObject == null) {
                return new DocumentId("");
            }
            if(!wasUploadAccepted()) {
                return new DocumentId("");
            }
            JSONValue value = jsonObject.get(FileUploadResponseAttributes.UPLOAD_FILE_ID.name());
            if(value == null) {
                return new DocumentId("");
            }
            JSONString string = value.isString();
            if(string == null) {
                return new DocumentId("");
            }
            return new DocumentId(string.stringValue().trim());
        }
        
    }
}
