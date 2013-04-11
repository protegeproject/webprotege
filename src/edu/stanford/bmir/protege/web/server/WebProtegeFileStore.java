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

//    /**
//     * To be called by the setup mechanism (usually {@link WebProtegeInitializer}) to init the store.
//     * Important: For safety purposes this method should only be called ONCE (at startup).  Calling it a second time will cause an
//     * {@link IllegalStateException} to be thrown.
//     * @param dataDirectory  The data directory for webprotege.  Not {@code null}.
//     * @throws NullPointerException if {@code dataDirectory} is {@code null}.
//     * @throws IllegalStateException if this method is called more than once.
//     */
//    protected static synchronized void setup(File dataDirectory) {
//        if(instance != null) {
//            throw new IllegalStateException("WebProtegeFileStore has already been initialised");
//        }
//        instance = new WebProtegeFileStore(checkNotNull(dataDirectory, "dataDirectory must not be null"));
//    }

    /**
     * Gets the one and only instance of {@link WebProtegeFileStore}.
     * @return An application wide instance of {@link WebProtegeFileStore}.  Not {@code null}.
     */
    public static synchronized WebProtegeFileStore getInstance() {
//        if(instance == null) {
//            throw new IllegalStateException("WebProtegeFileStore has not been initialised");
//        }
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
