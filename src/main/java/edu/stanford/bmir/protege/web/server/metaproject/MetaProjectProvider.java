package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.inject.Provider;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;
import org.apache.commons.io.FileUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Singleton
public class MetaProjectProvider implements Provider<MetaProject> {

    private static final String DEFAULT_PROJECTS_DIRECTORY_NAME = "default-projects";

    private MetaProjectImpl metaProject;

    private final Logger logger;

    @Inject
    public MetaProjectProvider(@MetaProjectURI URI metaProjectURI, Logger logger) {
        this.logger = logger;
        File metaProjectFile = new File(metaProjectURI);
        if(!metaProjectFile.exists()) {
            createFreshMetaProject(metaProjectFile);
        }
        metaProject = new MetaProjectImpl(metaProjectURI);
    }

    @Override
    public MetaProject get() {
        return metaProject;
    }

    private void createFreshMetaProject(File metaProjectFile) {
        try {
            logger.info("The meta-project does not exist.  Creating a fresh meta-project.");
            URL defaultProjectsDirectoryURL = Thread.currentThread()
                    .getContextClassLoader().getResource(DEFAULT_PROJECTS_DIRECTORY_NAME);
            if (defaultProjectsDirectoryURL == null) {
                throw new WebProtegeConfigurationException("Could not find default projects directory");
            }
            File templateProjectsDirectory = new File(defaultProjectsDirectoryURL.toURI());
            File templateMetaProjectDirectory = new File(templateProjectsDirectory, "metaproject");
            final File metaProjectDirectory = metaProjectFile.getParentFile();
            metaProjectDirectory.mkdirs();
            FileUtils.copyDirectory(templateMetaProjectDirectory, metaProjectDirectory);
            logger.info("Meta-project created at " + metaProjectDirectory);
        } catch (IOException | URISyntaxException e) {
            throw new WebProtegeConfigurationException("There was a problem initializing the metaproject.  Details: " + e.getMessage());
        }
    }
}
