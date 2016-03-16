package edu.stanford.bmir.protege.web.client.actionbar.project;

import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class ShareSettingsHandlerImpl implements ShowShareSettingsHandler {


//    private final Provider<SharingSettingsDialogController> sharingSettingsDialogControllerProvider;

    @Inject
    public ShareSettingsHandlerImpl(ActiveProjectManager activeProjectManager) {
//        this.activeProjectManager = activeProjectManager;
//        this.sharingSettingsDialogControllerProvider = sharingSettingsDialogControllerProvider;
    }

    @Override
    public void handleShowShareSettings() {
//        Optional<ProjectId> activeProjectId = activeProjectManager.getActiveProjectId();
//        if(!activeProjectId.isPresent()) {
//            MessageBox.alert("No project is selected");
//            return;
//        }
//        SharingSettingsDialogController controller = sharingSettingsDialogControllerProvider.get();
//        WebProtegeDialog.showDialog(controller);
    }
}
