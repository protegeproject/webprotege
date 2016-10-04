package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.server.inject.DataDirectory;

import javax.inject.Inject;
import java.io.File;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/05/2012
 * <p>
 *     The main management class for the WebProtege file store.  This class provides details about non-project
 *     specific storage locations.
 * </p>
 */
public class WebProtegeFileStore {

    /**
     * The name of the directory where default UI configuration data is saved to
     */
    private static final String DEFAULT_UI_CONFIGURATION_DATA_DIRECTORY_NAME = "default-ui-configuration-data";

    /**
     * The name of the directory that contains the meta project
     */
    private static final String META_PROJECT_DIRECTORY_NAME = "metaproject";

    /**
     * The name of the directory where uploads are stored
     */
    private static final String UPLOADS_DIRECTORY_NAME = "uploads";


    private final File dataDirectory;

    private final File metaprojectDirectory;

    private final File defaultUIConfigurationDataDirectory;

    private final File uploadsDirectory;


    @Inject
    public WebProtegeFileStore(@DataDirectory File dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.metaprojectDirectory = new File(dataDirectory, META_PROJECT_DIRECTORY_NAME);
        this.defaultUIConfigurationDataDirectory = new File(dataDirectory, DEFAULT_UI_CONFIGURATION_DATA_DIRECTORY_NAME);
        this.uploadsDirectory = new File(dataDirectory, UPLOADS_DIRECTORY_NAME);
    }


    /**
     * Gets the main data directory for webprotege. This value will never change once webprotege has started.
     * @return A {@link File} representing the main data directory for webprotege.  Not {@code null}.
     */
    public File getDataDirectory() {
        return dataDirectory;
    }

    /**
     * Gets the metaproject directory that holds the metaproject pprj, pont and pins files.  This value will never
     * change once webprotege has started.
     * @return A {@link File} representing the metaproject directory.  Not {@code null}.
     */
    public File getMetaProjectDirectory() {
        return metaprojectDirectory;
    }

    /**
     * Gets the default UI configuration data directory where the default layouts for projects are stored.
     * @return A {@link File} representing the directory where default UI configuration data is stored.  Not {@code null}.
     */
    public File getDefaultUIConfigurationDataDirectory() {
        return defaultUIConfigurationDataDirectory;
    }

    /**
     * Gets the directory for uploads.
     * @return A {@link File} representing the directory where uploads are stored.  Not {@code null}.
     */
    public File getUploadsDirectory() {
        return uploadsDirectory;
    }
}
