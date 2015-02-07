package edu.stanford.bmir.protege.web.client.ui.ontology.sharing;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsResult;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/02/2012
 */
public class SharingSettingsDialogController extends WebProtegeOKCancelDialogController<ProjectSharingSettings> {

    public static final String TITLE = "Sharing settings";

    private SharingSettingsPanel sharingSettingsPanel;

    public SharingSettingsDialogController(ProjectId projectId) {
        super(TITLE);
        sharingSettingsPanel = new SharingSettingsPanel(projectId);
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<ProjectSharingSettings>() {
            public void handleHide(ProjectSharingSettings data, WebProtegeDialogCloser closer) {
                updateSharingSettingsOnServer(data);
                closer.hide();
            }
        });
    }

    private void updateSharingSettingsOnServer(final ProjectSharingSettings sharingSettings) {
        DispatchServiceManager.get().execute(new SetProjectSharingSettingsAction(sharingSettings), new AbstractWebProtegeAsyncCallback<SetProjectSharingSettingsResult>() {
            @Override
            public void onSuccess(SetProjectSharingSettingsResult setProjectSharingSettingsResult) {
                EventBusManager.getManager().postEvent(new PermissionsChangedEvent(sharingSettings.getProjectId()));
            }
        });
    }

    @Override
    public Widget getWidget() {
        return sharingSettingsPanel;
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return null;
    }

    @Override
    public ProjectSharingSettings getData() {
        return sharingSettingsPanel.getSharingSettingsListData();
    }
}
