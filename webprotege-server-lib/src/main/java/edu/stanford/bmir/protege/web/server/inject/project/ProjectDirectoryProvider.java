package edu.stanford.bmir.protege.web.server.inject.project;

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

    private ProjectDirectoryFactory projectDirectoryFactory;

    private ProjectId projectId;

    @Inject
    public ProjectDirectoryProvider(ProjectDirectoryFactory projectDirectoryFactory, ProjectId projectId) {
        this.projectDirectoryFactory = projectDirectoryFactory;
        this.projectId = projectId;
    }

    @Override
    public File get() {
        return projectDirectoryFactory.getProjectDirectory(projectId);
    }
}
