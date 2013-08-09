package edu.stanford.bmir.protege.web.client.ui.upload;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FormPanel;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.*;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/05/2013
 */
public class UploadFileDialog extends WebProtegeDialog<String> {

    public UploadFileDialog(final String title, UploadFileResultHandler handler) {
        super(new UploadFileDialogController(title, handler));
    }

    @Override
    public UploadFileDialogController getController() {
        return (UploadFileDialogController) super.getController();
    }
}
