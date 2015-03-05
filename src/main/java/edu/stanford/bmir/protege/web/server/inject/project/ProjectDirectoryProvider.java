package edu.stanford.bmir.protege.web.server.inject.project;

import edu.stanford.bmir.protege.web.server.inject.DataDirectory;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class ProjectDirectoryProvider implements Provider<File> {

    private File dataDirectory;

    private ProjectId projectId;

    @Inject
    public ProjectDirectoryProvider(@DataDirectory File dataDirectory, ProjectId projectId) {
        this.dataDirectory = dataDirectory;
        this.projectId = projectId;
    }

    @Override
    public File get() {
        return new File(getProjectDataDirectory(), projectId.getId());
    }

    private File getProjectDataDirectory() {
        return new File(getDataStoreDirectory(), "project-data");
    }

    private File getDataStoreDirectory() {
        return new File(dataDirectory, "data-store");
    }
}
