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

    private UploadFileResultHandler resultHandler;

    public UploadFileDialog(final String title, UploadFileResultHandler handler) {
        super(new UploadFileDialogController(title));
        this.resultHandler = handler;
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<String>() {
            @Override
            public void handleHide(String data, final WebProtegeDialogCloser closer) {
                UIUtil.showLoadProgessBar("Uploading", "Uploading file...");
                getController().addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
                    public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                        UIUtil.hideLoadProgessBar();
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
                getController().submit();
            }
        });
    }

    @Override
    public UploadFileDialogController getController() {
        return (UploadFileDialogController) super.getController();
    }
}
