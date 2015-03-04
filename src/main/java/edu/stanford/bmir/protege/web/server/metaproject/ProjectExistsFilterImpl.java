package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/03/15
 */
public class ProjectExistsFilterImpl implements ProjectExistsFilter {

    @Override
    public boolean isProjectPresent(ProjectId projectId) {
        OWLAPIProjectDocumentStore documentStore = OWLAPIProjectDocumentStore.getProjectDocumentStore(projectId);
        return documentStore.exists();
    }
}
