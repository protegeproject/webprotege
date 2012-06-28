package edu.stanford.bmir.protege.web.server;

import javax.servlet.ServletContext;
import java.io.File;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/05/2012
 */
public class WebProtegeFileStore {

    public static final String WEB_PROTEGE_DATA_DIRECTORY_PARAM_NAME = "web-protege-data-directory";

    public static final String DEFAULT_WEB_PROTEGE_DATA_DIRECTORY = "/apps/bmir.apps/web-protege-data";


    private static WebProtegeFileStore instance;

    private File dataDirectory;


    private WebProtegeFileStore(ServletContext servletContext) {
        dataDirectory = new File(getWebProtegeDataDirectory(servletContext));
    }

    private String getWebProtegeDataDirectory(ServletContext servletContext) {
        String dirPathName = servletContext.getInitParameter(WEB_PROTEGE_DATA_DIRECTORY_PARAM_NAME);
        if(dirPathName == null) {
            dirPathName = DEFAULT_WEB_PROTEGE_DATA_DIRECTORY;
        }
        return dirPathName;
    }

    protected static synchronized void setup(ServletContext servletContext) {
        if(instance != null) {
            throw new IllegalStateException("WebProtegeFileStore has already been initialised");
        }
        instance = new WebProtegeFileStore(servletContext);
    }

    public static synchronized WebProtegeFileStore getInstance() {
        if(instance == null) {
            throw new IllegalStateException("WebProtegeFileStore has not been initialised");
        }
        return instance;
    }

    public File getDataDirectory() {
        return dataDirectory;
    }
}
