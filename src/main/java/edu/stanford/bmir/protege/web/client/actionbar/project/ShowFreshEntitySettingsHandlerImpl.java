package edu.stanford.bmir.protege.web.client.actionbar.project;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitSettingsDialogController;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.YesNoHandler;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
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
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess() {
                Optional<ProjectId> activeProject = Application.get().getActiveProject();
                if(!activeProject.isPresent()) {
                    return;
                }
                getSettingsAndShowDialog(activeProject);
            }
        });
    }

    private void getSettingsAndShowDialog(Optional<ProjectId> activeProject) {
        UIUtil.showLoadProgessBar("Retrieving settings", "Please wait.");
        GetEntityCrudKitSettingsAction action = new GetEntityCrudKitSettingsAction(activeProject.get());
        DispatchServiceManager.get().execute(action, new AsyncCallback<GetEntityCrudKitSettingsResult>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Problem retrieving settings");
                UIUtil.hideLoadProgessBar();
                MessageBox.showAlert("Error", "An error occurred whilst retrieving the fresh entity settings.  Please try again.");

            }

            @Override
            public void onSuccess(GetEntityCrudKitSettingsResult result) {
                GWT.log("Got fresh entity settings: " + result.getSettings());
                UIUtil.hideLoadProgessBar();
                showDialog(result);

            }
        });
    }

    private void showDialog(final GetEntityCrudKitSettingsResult result) {
        EntityCrudKitSettingsDialogController controller = new EntityCrudKitSettingsDialogController();
        WebProtegeDialog<EntityCrudKitSettings<?>> dlg = new WebProtegeDialog<EntityCrudKitSettings<?>>(controller);
        dlg.setVisible(true);
        controller.getEditor().setValue(result.getSettings());

        dlg.getController().setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<EntityCrudKitSettings<?>>() {
            @Override
            public void handleHide(EntityCrudKitSettings<?> data, final WebProtegeDialogCloser closer) {
                updateFreshEntitySettings(result.getSettings(), data, closer);
            }
        });
    }

    private void updateFreshEntitySettings(final EntityCrudKitSettings<?> fromSettings, final EntityCrudKitSettings<?> toSettings, final WebProtegeDialogCloser closer) {
        String oldPrefix = fromSettings.getPrefixSettings().getIRIPrefix();
        String newPrefix = toSettings.getPrefixSettings().getIRIPrefix();
        if(!oldPrefix.equals(newPrefix)) {
            MessageBox.showYesNoConfirmBox("Find and replace prefix?", "You changed the IRI prefix for new entities from <span style=\"font-weight:bold;\">" + oldPrefix + "</span> to <span style=\"font-weight:bold;\">" + newPrefix + "</span>. " +
                    "<br><br>Do you want to find and <span style=\"font-weight:bold;\">replace existing occurrences</span> of the previous prefix with the new prefix?"
                    , new YesNoHandler() {
                @Override
                public void handleYes() {
                    updateFreshEntitySettingsWithStrategy(fromSettings, toSettings, closer, IRIPrefixUpdateStrategy.FIND_AND_REPLACE);
                }

                @Override
                public void handleNo() {
                    updateFreshEntitySettingsWithStrategy(fromSettings, toSettings, closer, IRIPrefixUpdateStrategy.LEAVE_INTACT);
                }
            });
        }
        else {
            updateFreshEntitySettingsWithStrategy(fromSettings, toSettings, closer, IRIPrefixUpdateStrategy.LEAVE_INTACT);
        }
    }


    private void updateFreshEntitySettingsWithStrategy(EntityCrudKitSettings<?> fromSettings, EntityCrudKitSettings<?> toSettings, final WebProtegeDialogCloser closer, IRIPrefixUpdateStrategy iriPrefixUpdateStrategy) {
        Optional<ProjectId> activeProject = Application.get().getActiveProject();
        if(!activeProject.isPresent()) {
            GWT.log("Active project is not present");
            return;
        }
        ProjectId projectId = activeProject.get();
        DispatchServiceManager.get().execute(new SetEntityCrudKitSettingsAction(projectId, fromSettings, toSettings, iriPrefixUpdateStrategy), new AsyncCallback<SetEntityCrudKitSettingsResult>() {
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
