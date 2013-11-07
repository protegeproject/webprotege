package edu.stanford.bmir.protege.web.client.actionbar.project;

import com.google.common.base.Optional;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.ontology.sharing.SharingSettingsDialogController;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class ShareSettingsHandlerImpl implements ShowShareSettingsHandler {

    @Override
    public void handleShowShareSettings() {
        Optional<ProjectId> activeProjectId = Application.get().getActiveProject();
        if(!activeProjectId.isPresent()) {
            MessageBox.alert("No project is selected");
            return;
        }
        SharingSettingsDialogController controller = new SharingSettingsDialogController(activeProjectId.get());
        WebProtegeDialog.showDialog(controller);
    }
}
