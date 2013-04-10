package edu.stanford.bmir.protege.web.client.ui.projectconfig;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerService;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerServiceAsync;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/07/2012
 */
public class ProjectConfigurationDialogController extends WebProtegeOKCancelDialogController<ProjectConfigurationInfo> {

    public static final String DIALOG_TITLE = "Project configuration";

    private ProjectConfigurationForm dialogForm;

    private ProjectManagerServiceAsync projectManagerService;
    
    public ProjectConfigurationDialogController(ProjectId projectId) {
        super(DIALOG_TITLE);
        dialogForm = new ProjectConfigurationForm(projectId);
        projectManagerService = GWT.create(ProjectManagerService.class);
        dataToForm(projectId);
    }

    
    private void dataToForm(final ProjectId projectId) {
        projectManagerService.getAvailableProjectTypes(new AsyncCallback<List<ProjectType>>() {
            public void onFailure(Throwable caught) {
                GWT.log("Problem with retrieving available project types: " + caught.getMessage());
            }

            public void onSuccess(final List<ProjectType> projectTypes) {
                projectManagerService.getProjectConfiguration(projectId, new AsyncCallback<ProjectConfigurationInfo>() {
                    public void onFailure(Throwable caught) {
                        GWT.log("Problem with retrieving project configuration: " + caught.getMessage());
                    }

                    public void onSuccess(ProjectConfigurationInfo projectConfigurationInfo) {
                        dialogForm.setAllowedProjectTypes(projectTypes);
                        dialogForm.setData(projectConfigurationInfo);
                    }
                });
            }
        });
        

    }


    /**
     * Gets the widget that is displayed in the dialog and allows information to be entered into the dialog.
     * @return The widget that the user interacts with.  Not <code>null</code>.
     */
    @Override
    public Widget getWidget() {
        return dialogForm;
    }

    /**
     * Gets the focusable that should receive the focus when the dialog is shown.
     * @return The focusable that will receive the focus. Not <code>null</code>
     */
    @Override
    public Focusable getInitialFocusable() {
        return dialogForm.getInitialFocusable();
    }

    @Override
    public ProjectConfigurationInfo getData() {
        return dialogForm.getData();
    }
}
