package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.project.OWLAPIProjectFileStore;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public interface OWLAPIProjectFileStoreFactory {

    OWLAPIProjectFileStore get(ProjectId projectId);
}
