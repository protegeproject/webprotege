package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.WebProtegeFileStore;
import edu.stanford.bmir.protege.web.server.inject.DefaultUiConfigurationDirectory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectType;

import javax.inject.Inject;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class DefaultUIConfigurationFileManagerImpl implements DefaultUIConfigurationFileManager {

    private static final String DEFAULT_OBO_CONFIGURATION_FILE_NAME = "default-obo-configuration.xml";

    private static final String DEFAULT_OWL_CONFIGURATION_FILE_NAME = "default-owl-configuration.xml";


    private File defaultUiConfigurationDirectory;

    @Inject
    public DefaultUIConfigurationFileManagerImpl(@DefaultUiConfigurationDirectory File defaultUiConfigurationDirectory) {
        this.defaultUiConfigurationDirectory = defaultUiConfigurationDirectory;
    }

    @Override
    public File getDefaultConfigurationFile(OWLAPIProjectType projectType) {
        if(projectType == OWLAPIProjectType.getOBOProjectType()) {
            return new File(
                    defaultUiConfigurationDirectory,
                    DEFAULT_OBO_CONFIGURATION_FILE_NAME
            );
        }
        else {
            return new File(
                    defaultUiConfigurationDirectory,
                    DEFAULT_OWL_CONFIGURATION_FILE_NAME
            );
        }
    }
}
