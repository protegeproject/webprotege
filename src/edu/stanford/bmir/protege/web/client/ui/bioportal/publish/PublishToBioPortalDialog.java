package edu.stanford.bmir.protege.web.client.ui.bioportal.publish;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioPortalAPIService;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioPortalAPIServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.PublishToBioPortalInfo;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;

import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class PublishToBioPortalDialog extends WebProtegeDialog<PublishToBioPortalInfo> {

    public PublishToBioPortalDialog(final ProjectDetails details, final UserDetails userDetails) {
        super(new PublishToBioPortalDialogController(details, userDetails));
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<PublishToBioPortalInfo>() {
            public void handleHide(PublishToBioPortalInfo data, WebProtegeDialogCloser closer) {
                try {
                    handleUpload(details, userDetails, data, closer);
                    UIUtil.showLoadProgessBar("Publishing ontology to BioPortal", "Publishing");
                }
                catch (IOException e) {
                    e.printStackTrace();
                    MessageBox.alert("There was a problem publishing the ontology to BioPortal");
                }
                finally {
                    closer.hide();
                }
            }
        });
    }

    private void handleUpload(ProjectDetails details, UserDetails userDetails, PublishToBioPortalInfo publishInfo, final WebProtegeDialogCloser closer) throws IOException {
        BioPortalAPIServiceAsync service = GWT.create(BioPortalAPIService.class);
        RevisionNumber revisionNumber = RevisionNumber.getHeadRevisionNumber();
        service.uploadProjectToBioPortal(details.getProjectId(), revisionNumber, publishInfo, new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                UIUtil.hideLoadProgessBar();
                MessageBox.alert("There was a problem publishing the ontology to BioPortal.  Error message: " + caught.getMessage());
            }

            public void onSuccess(Void result) {
                UIUtil.hideLoadProgessBar();
                MessageBox.alert("The ontology was successfully published to BioPortal");
                closer.hide();
            }
        });
    }
}
