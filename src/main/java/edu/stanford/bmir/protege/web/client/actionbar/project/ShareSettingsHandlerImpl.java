package edu.stanford.bmir.protege.web.client.actionbar.project;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsDialogController;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Provider;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class ShareSettingsHandlerImpl implements ShowShareSettingsHandler {

    private final ActiveProjectManager activeProjectManager;

    private final Provider<SharingSettingsDialogController> sharingSettingsDialogControllerProvider;

    @Inject
    public ShareSettingsHandlerImpl(ActiveProjectManager activeProjectManager, Provider<SharingSettingsDialogController> sharingSettingsDialogControllerProvider) {
        this.activeProjectManager = activeProjectManager;
        this.sharingSettingsDialogControllerProvider = sharingSettingsDialogControllerProvider;
    }

    @Override
    public void handleShowShareSettings() {
        Optional<ProjectId> activeProjectId = activeProjectManager.getActiveProjectId();
        if(!activeProjectId.isPresent()) {
            MessageBox.alert("No project is selected");
            return;
        }
        SharingSettingsDialogController controller = sharingSettingsDialogControllerProvider.get();
        WebProtegeDialog.showDialog(controller);
    }
}
