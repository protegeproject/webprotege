package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectFileStore;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectFileStoreFactory;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/03/15
 */
public class ProjectExistsFilterImpl implements ProjectExistsFilter {

    private OWLAPIProjectFileStoreFactory fileStoreFactory;

    @Inject
    public ProjectExistsFilterImpl(OWLAPIProjectFileStoreFactory fileStoreFactory) {
        this.fileStoreFactory = fileStoreFactory;
    }

    @Override
    public boolean isProjectPresent(ProjectId projectId) {
        OWLAPIProjectFileStore fileStore = fileStoreFactory.get(projectId);
        return fileStore.getProjectDirectory().exists();
    }
}
