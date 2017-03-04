package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class ProjectSettingsPresenter {

    @Nonnull
    private EventBus eventBus;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private ProjectSettingsView projectSettingsView;

    @Inject
    public ProjectSettingsPresenter(@Nonnull EventBus eventBus,
                                    @Nonnull DispatchServiceManager dispatchServiceManager,
                                    @Nonnull ProjectSettingsView projectSettingsView) {
        this.eventBus = checkNotNull(eventBus);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.projectSettingsView = checkNotNull(projectSettingsView);
    }

    public void showDialog(ProjectId projectId) {
        final ProjectSettingsDialogController controller = new ProjectSettingsDialogController(projectSettingsView);

        controller.setDialogButtonHandler(DialogButton.OK, (data, closer) -> hideDialogAndSaveSettings(data, closer));

        dispatchServiceManager.execute(new GetProjectSettingsAction(projectId),
                new DispatchServiceCallback<GetProjectSettingsResult>() {
                    @Override
                    public void handleSuccess(GetProjectSettingsResult result) {
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
        dispatchServiceManager.execute(new SetProjectSettingsAction(data), new DispatchServiceCallback<SetProjectSettingsResult>() {
            @Override
            public void handleSuccess(SetProjectSettingsResult setProjectSettingsResult) {
                closer.hide();
                eventBus.fireEvent(new ProjectSettingsChangedEvent(data).asGWTEvent());
            }
        });
    }

}
