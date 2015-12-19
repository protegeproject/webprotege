package edu.stanford.bmir.protege.web.client.actionbar.project;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.ontology.sharing.SharingSettingsDialogController;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class ShareSettingsHandlerImpl implements ShowShareSettingsHandler {

    private final EventBus eventBus;

    private final DispatchServiceManager dispatchServiceManager;

    public ShareSettingsHandlerImpl(EventBus eventBus, DispatchServiceManager dispatchServiceManager) {
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public void handleShowShareSettings() {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess() {
                Optional<ProjectId> activeProjectId = Application.get().getActiveProject();
                if(!activeProjectId.isPresent()) {
                    MessageBox.alert("No project is selected");
                    return;
                }
                SharingSettingsDialogController controller = new SharingSettingsDialogController(activeProjectId.get(), eventBus, dispatchServiceManager);
                WebProtegeDialog.showDialog(controller);
            }
        });
    }
}
