package edu.stanford.bmir.protege.web.client.projectmanager;

import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.project.CreateNewProjectDialogController;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class CreateProjectRequestHandlerImpl implements CreateProjectRequestHandler {

    @Nonnull
    private final Provider<CreateNewProjectDialogController> dialogController;

    @Inject
    public CreateProjectRequestHandlerImpl(@Nonnull Provider<CreateNewProjectDialogController> dialogController) {
        this.dialogController = checkNotNull(dialogController);
    }

    @Override
    public void handleCreateProjectRequest() {
        WebProtegeDialog.showDialog(dialogController.get());
    }
}
