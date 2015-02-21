package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.WebProtegeFileStore;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectType;

import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class DefaultUIConfigurationFileManagerImpl implements DefaultUIConfigurationFileManager {

    private static final String DEFAULT_OBO_CONFIGURATION_FILE_NAME = "default-obo-configuration.xml";

    private static final String DEFAULT_OWL_CONFIGURATION_FILE_NAME = "default-owl-configuration.xml";


    private static final File DEFAULT_OWL_CONFIGURATION_FILE = new File(WebProtegeFileStore.getInstance().getDefaultUIConfigurationDataDirectory(), DEFAULT_OWL_CONFIGURATION_FILE_NAME);

    private static final File DEFAULT_OBO_CONFIGURATION_FILE = new File(WebProtegeFileStore.getInstance().getDefaultUIConfigurationDataDirectory(), DEFAULT_OBO_CONFIGURATION_FILE_NAME);


    @Override
    public File getDefaultConfigurationFile(OWLAPIProjectType projectType) {
        if(projectType == OWLAPIProjectType.getOBOProjectType()) {
            return DEFAULT_OBO_CONFIGURATION_FILE;
        }
        else {
            return DEFAULT_OWL_CONFIGURATION_FILE;
        }
    }
}
