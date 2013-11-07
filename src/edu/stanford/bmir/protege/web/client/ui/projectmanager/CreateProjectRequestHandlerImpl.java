package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.NewProjectDialogController;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class CreateProjectRequestHandlerImpl implements CreateProjectRequestHandler {

    @Override
    public void handleCreateProjectRequest() {
        WebProtegeDialog.showDialog(new NewProjectDialogController());
    }
}
