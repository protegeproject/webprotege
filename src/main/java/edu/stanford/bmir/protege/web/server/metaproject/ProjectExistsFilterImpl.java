package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
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

    private WebProtegeLogger logger;

    @Inject
    public ProjectExistsFilterImpl(OWLAPIProjectFileStoreFactory fileStoreFactory, WebProtegeLogger logger) {
        this.fileStoreFactory = fileStoreFactory;
        this.logger = logger;
    }

    @Override
    public boolean isProjectPresent(ProjectId projectId) {
        OWLAPIProjectDocumentStore ds = new OWLAPIProjectDocumentStore(projectId, fileStoreFactory, logger);
        return ds.exists();
    }
}
