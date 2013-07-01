package edu.stanford.bmir.protege.web.client.ui.upload;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;



/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/05/2013
 */
public class UploadFileDialogController extends WebProtegeOKCancelDialogController<String> {

    private UploadFileDialogForm form = new UploadFileDialogForm();

    public UploadFileDialogController(String title) {
        super(title);
    }

    public void addSubmitCompleteHandler(FormPanel.SubmitCompleteHandler submitCompleteHandler) {
        form.addSubmitCompleteHandler(submitCompleteHandler);
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return form.getInitialFocusable();
    }

    @Override
    public String getData() {
        return form.getFileName();
    }

    @Override
    public Widget getWidget() {
        return form;
    }

    public void submit() {
        form.submit();
    }
}
