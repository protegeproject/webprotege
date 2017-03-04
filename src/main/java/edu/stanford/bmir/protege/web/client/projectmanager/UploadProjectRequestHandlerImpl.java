package edu.stanford.bmir.protege.web.client.projectmanager;

import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.project.UploadProjectDialogController;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class UploadProjectRequestHandlerImpl implements UploadProjectRequestHandler {

    private final Provider<UploadProjectDialogController> uploadProjectDialogController;

    @Inject
    public UploadProjectRequestHandlerImpl(Provider<UploadProjectDialogController> uploadProjectDialogController) {
        this.uploadProjectDialogController = uploadProjectDialogController;
    }

    @Override
    public void handleUploadProjectRequest() {
        WebProtegeDialog.showDialog(uploadProjectDialogController.get());
    }
}
