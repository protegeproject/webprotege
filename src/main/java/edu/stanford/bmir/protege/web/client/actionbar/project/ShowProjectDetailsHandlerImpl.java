package edu.stanford.bmir.protege.web.client.actionbar.project;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.projectconfig.ProjectConfigurationDialogController;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2013
 */
public class ShowProjectDetailsHandlerImpl implements ShowProjectDetailsHandler {

    @Override
    public void handleShowProjectDetails() {
        Optional<ProjectId> projectId = Application.get().getActiveProject();
        if(!projectId.isPresent()) {
            return;
        }
        WebProtegeDialog.showDialog(new ProjectConfigurationDialogController(projectId.get()));
    }
}
