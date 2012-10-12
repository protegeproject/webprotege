package edu.stanford.bmir.protege.web.client.ui.bioportal.publish;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioPortalUploadInfo;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class BioPortalUploadDialogController extends WebProtegeOKCancelDialogController<BioPortalUploadInfo> {

    public static final String TITLE = "Publish Ontology to BioPortal";

    private PublishToBioPortalForm publishForm;

    public BioPortalUploadDialogController(ProjectData projectData, UserData userData) {
        super(TITLE);
        this.publishForm = new PublishToBioPortalForm(projectData, userData);
    }

    @Override
    public Widget getWidget() {
        return publishForm;
    }

    @Override
    public Focusable getInitialFocusable() {
        return publishForm.getInitialFocusable();
    }

    @Override
    public BioPortalUploadInfo getData() {
        return publishForm.getData();
    }
}
