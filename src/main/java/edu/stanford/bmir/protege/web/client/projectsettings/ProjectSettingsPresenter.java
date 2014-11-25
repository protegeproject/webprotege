package edu.stanford.bmir.protege.web.client.projectsettings;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class ProjectSettingsPresenter {

    private ProjectSettingsView projectSettingsView;

    private EventBusManager eventBusManager;

    public ProjectSettingsPresenter(ProjectSettingsView projectSettingsView, EventBusManager eventBusManager) {
        this.projectSettingsView = projectSettingsView;
        this.eventBusManager = eventBusManager;
    }

    public void showDialog(ProjectId projectId) {
        final ProjectSettingsDialogController controller = new ProjectSettingsDialogController(projectSettingsView);

        controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<ProjectSettings>() {
            public void handleHide(ProjectSettings data, final WebProtegeDialogCloser closer) {
                hideDialogAndSaveSettings(data, closer);

            }
        });


        DispatchServiceManager.get().execute(new GetProjectSettingsAction(projectId),
                new AbstractWebProtegeAsyncCallback<GetProjectSettingsResult>() {
                    @Override
                    public void onSuccess(GetProjectSettingsResult result) {
                        setProjectSettingsAndShowDialog(result, controller);
                    }
                });


    }

    private void setProjectSettingsAndShowDialog(GetProjectSettingsResult result, ProjectSettingsDialogController controller) {
        projectSettingsView.setValue(result.getProjectSettings());
        WebProtegeDialog<ProjectSettings> dlg = new WebProtegeDialog<ProjectSettings>(controller);
        dlg.setVisible(true);
    }


    private void hideDialogAndSaveSettings(final ProjectSettings data, final WebProtegeDialogCloser closer) {
        DispatchServiceManager.get().execute(new SetProjectSettingsAction(data), new AbstractWebProtegeAsyncCallback<SetProjectSettingsResult>() {
            @Override
            public void onSuccess(SetProjectSettingsResult setProjectSettingsResult) {
                closer.hide();
                eventBusManager.postEvent(new ProjectSettingsChangedEvent(data));
            }
        });
    }

}
