package edu.stanford.bmir.protege.web.client.ui.bioportal.publish;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.PublishToBioPortalInfo;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogValidator;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class PublishToBioPortalDialogController extends WebProtegeOKCancelDialogController<PublishToBioPortalInfo> {

    public static final String TITLE = "Publish Ontology to BioPortal";

    private PublishToBioPortalForm publishForm;

    public PublishToBioPortalDialogController(ProjectData projectData, UserDetails userDetails) {
        super(TITLE);
        this.publishForm = new PublishToBioPortalForm(projectData, userDetails);
        for(WebProtegeDialogValidator validator : publishForm.getValidators()) {
            addDialogValidator(validator);
        }
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
    public PublishToBioPortalInfo getData() {
        return publishForm.getData();
    }
}
