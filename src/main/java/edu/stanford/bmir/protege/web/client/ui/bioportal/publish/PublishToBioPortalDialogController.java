package edu.stanford.bmir.protege.web.client.ui.bioportal.publish;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioPortalAPIService;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioPortalAPIServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.PublishToBioPortalInfo;
import edu.stanford.bmir.protege.web.client.ui.library.progress.ProgressMonitor;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.*;
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
public class PublishToBioPortalDialogController extends WebProtegeOKCancelDialogController<PublishToBioPortalInfo> {

    public static final String TITLE = "Publish Ontology to BioPortal";

    private PublishToBioPortalForm publishForm;

    public PublishToBioPortalDialogController(final ProjectDetails details, final UserDetails userDetails) {
        super(TITLE);
        this.publishForm = new PublishToBioPortalForm(details, userDetails);
        for(WebProtegeDialogValidator validator : publishForm.getValidators()) {
            addDialogValidator(validator);
        }
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<PublishToBioPortalInfo>() {
            public void handleHide(PublishToBioPortalInfo data, WebProtegeDialogCloser closer) {
                try {
                    handleUpload(details, userDetails, data, closer);
                    ProgressMonitor.get().showProgressMonitor("Publishing ontology to BioPortal", "Please wait");
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
        service.uploadProjectToBioPortal(details.getDisplayName(), details.getProjectId(), revisionNumber, publishInfo, new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                ProgressMonitor.get().hideProgressMonitor();
                MessageBox.alert("There was a problem publishing the ontology to BioPortal.  Error message: " + caught.getMessage());
            }

            public void onSuccess(Void result) {
                ProgressMonitor.get().hideProgressMonitor();
                MessageBox.alert("The ontology was successfully published to BioPortal");
                closer.hide();
            }
        });
    }

    @Override
    public Widget getWidget() {
        return publishForm;
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return publishForm.getInitialFocusable();
    }

    @Override
    public PublishToBioPortalInfo getData() {
        return publishForm.getData();
    }
}
