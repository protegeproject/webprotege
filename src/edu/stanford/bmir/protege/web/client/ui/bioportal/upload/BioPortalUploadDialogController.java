package edu.stanford.bmir.protege.web.client.ui.bioportal.upload;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioPortalUploadInfo;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.ValidationState;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogValidator;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class BioPortalUploadDialogController extends WebProtegeOKCancelDialogController<BioPortalUploadInfo> {

    public static final String TITLE = "Publish Ontology to BioPortal";

    private BioPortalUploadForm uploadForm;

    public BioPortalUploadDialogController(ProjectData projectData, UserData userData) {
        super(TITLE);
        this.uploadForm = new BioPortalUploadForm(projectData, userData);
    }

    @Override
    public Widget getWidget() {
        return uploadForm;
    }

    @Override
    public Focusable getInitialFocusable() {
        return uploadForm.getInitialFocusable();
    }

    @Override
    public BioPortalUploadInfo getData() {
        return uploadForm.getData();
    }
}
