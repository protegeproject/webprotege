package edu.stanford.bmir.protege.web.client.project;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/12/15
 */
public interface ActiveProjectManager {

    Optional<ProjectId> getActiveProjectId();

    void setActiveProject(Optional<ProjectId> project);

}
