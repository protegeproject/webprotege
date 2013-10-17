package edu.stanford.bmir.protege.web.server.filesubmission;

import edu.stanford.bmir.protege.web.server.WebProtegeFileStore;

import java.io.File;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 */
public class FileUploadConstants {

    private static final String UPLOADS_DIRECTORY_NAME = "uploads";
    
    /**
     * The directory to where files are uploaded.
     */
    public static final File UPLOADS_DIRECTORY = new File(WebProtegeFileStore.getInstance().getDataDirectory(), UPLOADS_DIRECTORY_NAME);

}
