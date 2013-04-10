package edu.stanford.bmir.protege.web.client.ui.ontology.sharing;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.SharingSettingsServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.SharingSettingsServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/02/2012
 */
public class SharingSettingsDialog extends WebProtegeDialog<ProjectSharingSettings> {

    public SharingSettingsDialog(final ProjectId projectId) {
        super(new SharingSettingsDialogController(projectId));
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<ProjectSharingSettings>() {
            public void handleHide(ProjectSharingSettings data, WebProtegeDialogCloser closer) {
                updateSharingSettingsOnServer(data);
                closer.hide();
            }
        });
    }

    private void updateSharingSettingsOnServer(ProjectSharingSettings sharingSettings) {
        SharingSettingsServiceAsync service = SharingSettingsServiceManager.getService();
        service.updateSharingSettings(sharingSettings, new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(Void result) {
                // TODO FIRE PERMISSIONS CHANGED!
            }
        });
    }
}
