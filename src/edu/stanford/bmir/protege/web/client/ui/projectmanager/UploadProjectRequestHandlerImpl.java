package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.UploadProjectDialogController;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class UploadProjectRequestHandlerImpl implements UploadProjectRequestHandler {

    @Override
    public void handleUploadProjectRequest() {
        WebProtegeDialog.showDialog(new UploadProjectDialogController());
    }
}
