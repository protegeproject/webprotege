package edu.stanford.bmir.protege.web.server.init;

import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectFile;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import org.apache.commons.io.FileUtils;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2013
 */
public class CheckMetaProjectExists implements ConfigurationTask {

    private static final String DEFAULT_PROJECTS_DIRECTORY_NAME = "default-projects";

    private File metaProjectFile;

    @Inject
    public CheckMetaProjectExists(@MetaProjectFile File metaProjectFile) {
        this.metaProjectFile = metaProjectFile;
    }

    @Override
    public void run(ServletContext servletContext) throws WebProtegeConfigurationException {
        if (metaProjectFile.exists()) {
            return;
        }
        createFreshMetaProject(servletContext, metaProjectFile);
    }

    private void createFreshMetaProject(ServletContext servletContext, File metaProjectFile) {
        try {
            File webappRoot = new File(servletContext.getRealPath(""));
            File templateProjectsDirectory = new File(webappRoot, DEFAULT_PROJECTS_DIRECTORY_NAME);
            File templateMetaProjectDirectory = new File(templateProjectsDirectory, "metaproject");
            final File metaProjectDirectory = metaProjectFile.getParentFile();
            metaProjectDirectory.mkdirs();
            FileUtils.copyDirectory(templateMetaProjectDirectory, metaProjectDirectory);
        } catch (IOException e) {
            throw new WebProtegeConfigurationException("There was a problem initializing the metaproject.  Details: " + e.getMessage());
        }
    }
}
