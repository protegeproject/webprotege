package edu.stanford.bmir.protege.web.server.inject;


import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class UiConfigurationDirectoryProvider implements Provider<File> {

    /**
     * The name of the directory where default UI configuration data is saved to
     */
    private static final String DEFAULT_UI_CONFIGURATION_DATA_DIRECTORY_NAME = "default-ui-configuration-data";

    private final File dataDirectory;

    @Inject
    public UiConfigurationDirectoryProvider(@DataDirectory File dataDirectory) {
        this.dataDirectory = checkNotNull(dataDirectory);
    }

    @Override
    public File get() {
        return new File(dataDirectory, DEFAULT_UI_CONFIGURATION_DATA_DIRECTORY_NAME);
    }
}
