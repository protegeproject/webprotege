package edu.stanford.bmir.protege.web.server;

import java.io.File;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/05/2012
 * <p>
 *     The main management class for the WebProtege file store.
 * </p>
 */
public class WebProtegeFileStore {

    private static WebProtegeFileStore instance = new WebProtegeFileStore(WebProtegeProperties.getDataDirectory());

    private File dataDirectory;


    private WebProtegeFileStore(File dataDirectory) {
        this.dataDirectory = dataDirectory;
    }


    /**
     * Gets the one and only instance of {@link WebProtegeFileStore}.
     * @return An application wide instance of {@link WebProtegeFileStore}.  Not {@code null}.
     */
    public static WebProtegeFileStore getInstance() {
        return instance;
    }

    /**
     * Gets the main data directory for webprotege. This value will never change once webprotege has started.
     * @return A {@link File} representing the main data directory for webprotege.  Not {@code null}.
     */
    public File getDataDirectory() {
        return dataDirectory;
    }
}
