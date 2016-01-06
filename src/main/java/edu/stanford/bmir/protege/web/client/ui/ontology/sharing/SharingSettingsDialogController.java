package edu.stanford.bmir.protege.web.client.ui.ontology.sharing;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.PermissionManager;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsResult;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/02/2012
 */
public class SharingSettingsDialogController extends WebProtegeOKCancelDialogController<ProjectSharingSettings> {

    public static final String TITLE = "Sharing settings";

    private final DispatchServiceManager dispatchServiceManager;

    private SharingSettingsPanel sharingSettingsPanel;

    private PermissionManager permissionManager;

    @Inject
    public SharingSettingsDialogController(PermissionManager permissionManager, DispatchServiceManager dispatchServiceManager, SharingSettingsPanel sharingSettingsPanel) {
        super(TITLE);
        this.permissionManager = permissionManager;
        this.dispatchServiceManager = dispatchServiceManager;
        this.sharingSettingsPanel = sharingSettingsPanel;
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<ProjectSharingSettings>() {
            public void handleHide(ProjectSharingSettings data, WebProtegeDialogCloser closer) {
                updateSharingSettingsOnServer(data);
                closer.hide();
            }
        });
    }

    private void updateSharingSettingsOnServer(final ProjectSharingSettings sharingSettings) {
        dispatchServiceManager.execute(new SetProjectSharingSettingsAction(sharingSettings), new DispatchServiceCallback<SetProjectSharingSettingsResult>() {
            @Override
            public void handleSuccess(SetProjectSharingSettingsResult result) {
                GWT.log("[SharingSettingsDialogController] Sharing settings have been updated.  Firing PermissionsChangedEvent.");
                permissionManager.firePermissionsChanged();
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
