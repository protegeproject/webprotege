package edu.stanford.bmir.protege.web.client.upload;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FileUpload;
import edu.stanford.bmir.protege.web.client.library.dlg.ValidationState;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogForm;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogValidator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/05/2013
 */
public class UploadFileDialogForm extends WebProtegeDialogForm {

    private static final String SUBMIT_FILE_URL = GWT.getModuleBaseURL() + "submitfile";

    private final FileUpload fileUpload;

    public static final String FILE_NAME_FIELD_LABEL = "File";

    public UploadFileDialogForm() {
        setPostURL(SUBMIT_FILE_URL);
        fileUpload = new FileUpload();
        fileUpload.setName("file");
        addWidget(FILE_NAME_FIELD_LABEL, fileUpload);
        fileUpload.setWidth("300px");
        addDialogValidator(new FileNameValidator());
    }

    public String getFileName() {
        return fileUpload.getFilename().trim();
    }


    private class FileNameValidator implements WebProtegeDialogValidator {

        public ValidationState getValidationState() {
            return getFileName().isEmpty() ? ValidationState.INVALID : ValidationState.VALID;
        }

        public String getValidationMessage() {
            return "A file name must be specified.  Please specify a file name";
        }
    }
}
