package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/03/15
 */
public interface ProjectExistsFilter {

    boolean isProjectPresent(ProjectId projectId);
}
