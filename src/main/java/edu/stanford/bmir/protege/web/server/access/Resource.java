package edu.stanford.bmir.protege.web.server.access;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Jan 2017
 */
public interface Resource {

    Optional<ProjectId> getProjectId();

    boolean isApplicationTarget();

    boolean isProjectTarget(ProjectId projectId);
}

