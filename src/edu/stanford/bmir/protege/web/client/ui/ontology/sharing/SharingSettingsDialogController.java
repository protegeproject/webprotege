package edu.stanford.bmir.protege.web.client.ui.ontology.sharing;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;

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

    }

    @Override
    public Widget getWidget() {
        return sharingSettingsPanel;
    }

    @Override
    public Focusable getInitialFocusable() {
        return null;
    }

    @Override
    public ProjectSharingSettings getData() {
        return sharingSettingsPanel.getSharingSettingsListData();
    }
}
