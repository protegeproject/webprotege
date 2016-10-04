package edu.stanford.bmir.protege.web.client.project;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitSettingsDialogController;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.YesNoHandler;
import edu.stanford.bmir.protege.web.shared.crud.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2013
 */
public class ShowFreshEntitySettingsHandlerImpl implements ShowFreshEntitySettingsHandler {

    private final DispatchServiceManager dispatchServiceManager;

    private final ActiveProjectManager activeProjectManager;

    private final Provider<EntityCrudKitSettingsDialogController> dialogControllerProvider;

    @Inject
    public ShowFreshEntitySettingsHandlerImpl(DispatchServiceManager dispatchServiceManager,
                                              ActiveProjectManager activeProjectManager,
                                              Provider<EntityCrudKitSettingsDialogController> dialogControllerProvider) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.activeProjectManager = activeProjectManager;
        this.dialogControllerProvider = dialogControllerProvider;
    }

    @Override
    public void handleShowFreshEntitySettings() {
        Optional<ProjectId> activeProject = activeProjectManager.getActiveProjectId();
        if(!activeProject.isPresent()) {
            return;
        }
        getSettingsAndShowDialog(activeProject);
    }

    private void getSettingsAndShowDialog(Optional<ProjectId> activeProject) {
        GetEntityCrudKitSettingsAction action = new GetEntityCrudKitSettingsAction(activeProject.get());
        dispatchServiceManager.execute(action, new DispatchServiceCallbackWithProgressDisplay<GetEntityCrudKitSettingsResult>() {
            @Override
            public String getProgressDisplayTitle() {
                return "Retrieving settings";
            }

            @Override
            public String getProgressDisplayMessage() {
                return "Please wait.";
            }

            @Override
            public void handleSuccess(GetEntityCrudKitSettingsResult result) {
                showDialog(result);
            }
        });
    }

    private void showDialog(final GetEntityCrudKitSettingsResult result) {
        EntityCrudKitSettingsDialogController controller = dialogControllerProvider.get();

        WebProtegeDialog<EntityCrudKitSettings<?>> dlg = new WebProtegeDialog<>(controller);

        dlg.getController().setDialogButtonHandler(DialogButton.OK,
                (data, closer) -> updateFreshEntitySettings(result.getSettings(), data, closer));

        dlg.setPopupPositionAndShow(
                (w, h) -> dlg.setPopupPosition((Window.getClientWidth() - w) / 2, 100));
        controller.getEditor().setValue(result.getSettings());


    }

    private void updateFreshEntitySettings(final EntityCrudKitSettings<?> fromSettings, final EntityCrudKitSettings<?> toSettings, final WebProtegeDialogCloser closer) {
        String oldPrefix = fromSettings.getPrefixSettings().getIRIPrefix();
        GWT.log("[ShowFreshEntitySettingsHandlerImpl] Old Prefix: " + oldPrefix);
        String newPrefix = toSettings.getPrefixSettings().getIRIPrefix();
        GWT.log("[ShowFreshEntitySettingsHandlerImpl] New Prefix: " + newPrefix);

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
        Optional<ProjectId> activeProject = activeProjectManager.getActiveProjectId();
        if(!activeProject.isPresent()) {
            GWT.log("Active project is not present");
            return;
        }
        ProjectId projectId = activeProject.get();
        dispatchServiceManager.execute(new SetEntityCrudKitSettingsAction(projectId, fromSettings, toSettings, iriPrefixUpdateStrategy), new DispatchServiceCallback<SetEntityCrudKitSettingsResult>() {
            @Override
            public void handleSuccess(SetEntityCrudKitSettingsResult result) {
                closer.hide();
            }
        });
    }
}
