package edu.stanford.bmir.protege.web.client.projectmanager;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.project.NewProjectDialogController;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;

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
    private final Provider<NewProjectDialogController> dialogController;

    @Inject
    public CreateProjectRequestHandlerImpl(@Nonnull Provider<NewProjectDialogController> dialogController) {
        this.dialogController = checkNotNull(dialogController);
    }

    @Override
    public void handleCreateProjectRequest() {
        WebProtegeDialog.showDialog(dialogController.get());
    }
}
