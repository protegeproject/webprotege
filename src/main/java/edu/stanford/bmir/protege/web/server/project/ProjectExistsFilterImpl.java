package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/03/15
 */
public class ProjectExistsFilterImpl implements ProjectExistsFilter {

    private ProjectFileStoreFactory fileStoreFactory;

    @Inject
    public ProjectExistsFilterImpl(ProjectFileStoreFactory fileStoreFactory) {
        this.fileStoreFactory = fileStoreFactory;
    }

    @Override
    public boolean isProjectPresent(ProjectId projectId) {
        ProjectFileStore fileStore = fileStoreFactory.get(projectId);
        return fileStore.getProjectDirectory().exists();
    }
}
