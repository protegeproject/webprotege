package edu.stanford.bmir.protege.web.server;

import javax.servlet.ServletContext;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/05/2012
 * <p>
 *     The main management class for the WebProtege file store.  The default storage directory is given by
 *     {@link #DEFAULT_WEB_PROTEGE_DATA_DIRECTORY} value.  When WebProtege is started the ServletContext is examined
 *     for an init parameter named by {@link #WEB_PROTEGE_DATA_DIRECTORY_PARAM_NAME} which specifies the directory to
 *     use.  If this parameter is not registered then the default path is used.
 * </p>
 */
public class WebProtegeFileStore {

    public static final String WEB_PROTEGE_DATA_DIRECTORY_PARAM_NAME = "web-protege-data-directory";

    public static final String DEFAULT_WEB_PROTEGE_DATA_DIRECTORY = "/apps/bmir.apps/web-protege-data";


    private static WebProtegeFileStore instance;

    private File dataDirectory;


    private WebProtegeFileStore(ServletContext servletContext) {
        dataDirectory = new File(getWebProtegeDataDirectoryFromServletContext(servletContext));
    }

    /**
     * Extracts the webprotege data directory from the servlet context.
     * @param servletContext The servlet context.  Not {@code null}.
     * @return A string representing the data directory path name.  If the servlet context does not contain
     * an init param named by {@link #WEB_PROTEGE_DATA_DIRECTORY_PARAM_NAME} then the default data directory path name
     * specified by {@link #DEFAULT_WEB_PROTEGE_DATA_DIRECTORY} will be returned.
     */
    private String getWebProtegeDataDirectoryFromServletContext(ServletContext servletContext) {
        checkNotNull(servletContext);
        String dirPathName = servletContext.getInitParameter(WEB_PROTEGE_DATA_DIRECTORY_PARAM_NAME);
        if(dirPathName == null) {
            dirPathName = DEFAULT_WEB_PROTEGE_DATA_DIRECTORY;
        }
        return dirPathName;
    }

    /**
     * To be called by the setup mechanism (usually a {@link javax.servlet.ServletContextListener}) to init the store.
     * Important: For safety purposes this method should only be called ONCE (at startup).  Calling it a second time will cause an
     * {@link IllegalStateException} to be thrown.
     * @param servletContext The {@link ServletContext} that is specific to the running instance of webprotege. Not
     * {@code null}.
     * @throws NullPointerException if {@code servletContext} is {@code null}.
     * @throws IllegalStateException if this method is called more than once.
     */
    protected static synchronized void setup(ServletContext servletContext) {
        if(instance != null) {
            throw new IllegalStateException("WebProtegeFileStore has already been initialised");
        }
        instance = new WebProtegeFileStore(checkNotNull(servletContext, "servletContext must not be null"));
    }

    /**
     * Gets the one and only instance of {@link WebProtegeFileStore}.
     * @return An application wide instance of {@link WebProtegeFileStore}.  Not {@code null}.
     */
    public static synchronized WebProtegeFileStore getInstance() {
        if(instance == null) {
            throw new IllegalStateException("WebProtegeFileStore has not been initialised");
        }
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
