package edu.stanford.bmir.protege.web.client.ui.projectconfig;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerService;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerServiceAsync;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/07/2012
 */
public class ProjectConfigurationDialog extends WebProtegeDialog<ProjectConfigurationInfo> {

    public ProjectConfigurationDialog(ProjectId projectId) {
        super(new ProjectConfigurationDialogController(projectId));
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<ProjectConfigurationInfo>() {
            public void handleHide(ProjectConfigurationInfo data, final WebProtegeDialogCloser closer) {

                ProjectManagerServiceAsync pms = GWT.create(ProjectManagerService.class);
                pms.setProjectConfiguration(data, new AsyncCallback<Void>() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess(Void result) {
                        closer.hide();
                    }
                });
            }
        });

    }
}
