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

    }
}
