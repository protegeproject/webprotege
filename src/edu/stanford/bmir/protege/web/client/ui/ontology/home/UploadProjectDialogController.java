package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogValidator;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 */
public class UploadProjectDialogController extends WebProtegeOKCancelDialogController<UploadFileInfo> {

    private static final String TITLE = "Upload ontology";

    private UploadFileWidget uploadFileWidget = new UploadFileWidget();

    public UploadProjectDialogController() {
        super(TITLE);
        for(WebProtegeDialogValidator validator : uploadFileWidget.getDialogValidators()) {
            addDialogValidator(validator);
        }
    }

    @Override
    public Widget getWidget() {
        return uploadFileWidget;
    }

    @Override
    public Focusable getInitialFocusable() {
        return uploadFileWidget.getDefaultWidget();
    }

    @Override
    public UploadFileInfo getData() {
        return uploadFileWidget.getUploadFileInfo();
    }
}
