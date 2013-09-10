package edu.stanford.bmir.protege.web.client.actionbar.project;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitSettingsDialogController;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.crud.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2013
 */
public class ShowFreshEntitySettingsHandlerImpl implements ShowFreshEntitySettingsHandler {

    public ShowFreshEntitySettingsHandlerImpl() {

    }

    @Override
    public void handleShowFreshEntitySettings() {
        Optional<ProjectId> activeProject = Application.get().getActiveProject();
        if(!activeProject.isPresent()) {
            return;
        }
        getSettingsAndShowDialog(activeProject);
    }

    private void getSettingsAndShowDialog(Optional<ProjectId> activeProject) {
        GetEntityCrudKitSettingsAction action = new GetEntityCrudKitSettingsAction(activeProject.get());
        DispatchServiceManager.get().execute(action, new AsyncCallback<GetEntityCrudKitSettingsResult>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Problem retrieving settings");
                MessageBox.showAlert("Error", "An error occurred whilst retrieving the fresh entity settings.  Please try again.");
            }

            @Override
            public void onSuccess(GetEntityCrudKitSettingsResult result) {
                GWT.log("Got fresh entity settings: " + result.getSettings());
                showDialog(result);
            }
        });
    }

    private void showDialog(GetEntityCrudKitSettingsResult result) {
        EntityCrudKitSettingsDialogController controller = new EntityCrudKitSettingsDialogController();
        WebProtegeDialog<EntityCrudKitSettings> dlg = new WebProtegeDialog<EntityCrudKitSettings>(controller);
        dlg.setVisible(true);
        controller.getEditor().setValue(result.getSettings());

        dlg.getController().setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<EntityCrudKitSettings>() {
            @Override
            public void handleHide(EntityCrudKitSettings data, final WebProtegeDialogCloser closer) {
                updateFreshEntitySettings(data, closer);
            }
        });
    }

    private void updateFreshEntitySettings(EntityCrudKitSettings data, final WebProtegeDialogCloser closer) {
        DispatchServiceManager.get().execute(new SetEntityCrudKitSettingsAction(Application.get().getActiveProject().get(), data), new AsyncCallback<SetEntityCrudKitSettingsResult>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("There was a problem setting the EntityCrudKitSettings", caught);
                MessageBox.showAlert("Error", "An error occurred whilst updating the fresh entity settings.  Please try again.");
            }

            @Override
            public void onSuccess(SetEntityCrudKitSettingsResult result) {
                closer.hide();
            }
        });
    }
}
