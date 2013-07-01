package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import edu.stanford.bmir.protege.web.client.ui.library.common.Refreshable;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.ValidationState;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogValidator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 */
public class UploadFileWidget extends NewProjectInfoWidget {
    
    private static final String SUBMIT_FILE_URL = GWT.getModuleBaseURL() + "submitfile";

    public static final String FILE_NAME_FIELD_LABEL = "File";

    private final FileUpload fileUpload;

    public UploadFileWidget() {
        setPostURL(SUBMIT_FILE_URL);
        fileUpload = new FileUpload();
        fileUpload.setName("file");
        addWidget(FILE_NAME_FIELD_LABEL, fileUpload);
        fileUpload.setWidth(FIELD_WIDTH);
        addDialogValidator(new FileNameValidator());
    }
    
    public String getFileName() {
        return fileUpload.getFilename().trim();
    }


    public UploadFileInfo getUploadFileInfo() {
        return new UploadFileInfo(getNewProjectInfo(), this);
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
