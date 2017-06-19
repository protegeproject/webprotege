package edu.stanford.bmir.protege.web.client.upload;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.*;
import edu.stanford.bmir.protege.web.client.progress.ProgressMonitor;

import javax.annotation.Nonnull;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/05/2013
 */
public class UploadFileDialogController extends WebProtegeOKCancelDialogController<String> {

    private UploadFileDialogForm form = new UploadFileDialogForm();

    public UploadFileDialogController(String title, final UploadFileResultHandler resultHandler) {
        super(title);
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<String>() {
            @Override
            public void handleHide(String data, final WebProtegeDialogCloser closer) {
                ProgressMonitor.get().showProgressMonitor("Uploading", "Uploading file");
                form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
                    public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                        ProgressMonitor.get().hideProgressMonitor();
                        GWT.log("Submittion of file is complete");
                        FileUploadResponse result = new FileUploadResponse(event.getResults());
                        if(result.wasUploadAccepted()) {
                            GWT.log("Successful upload");
                            resultHandler.handleFileUploaded(result.getDocumentId());

                        }
                        else {
                            GWT.log("Upload rejected: " + result.getUploadRejectedMessage());
                            resultHandler.handleFileUploadFailed(result.getUploadRejectedMessage());
                        }
                        closer.hide();

                    }
                });
                form.submit();
            }
        });
    }


    @Nonnull
    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
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
}
