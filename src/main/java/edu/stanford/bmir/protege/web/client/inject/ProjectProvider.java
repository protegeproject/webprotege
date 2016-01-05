package edu.stanford.bmir.protege.web.client.inject;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/12/15
 */
public class ProjectProvider implements Provider<Project> {

    private ActiveProjectManager activeProjectManager;

    private ProjectManager projectManager;

    @Inject
    public ProjectProvider(ActiveProjectManager activeProjectManager, ProjectManager projectManager) {
        this.activeProjectManager = activeProjectManager;
        this.projectManager = projectManager;
    }

    @Override
    public Project get() {
        Optional<ProjectId> projectId = activeProjectManager.getActiveProjectId();
        if(!projectId.isPresent()) {
            throw new RuntimeException("Cannot provide Project because there is no active project");
        }
        Optional<Project> project = projectManager.getProject(projectId.get());
        if(!project.isPresent()) {
            throw new RuntimeException("Cannot provide Project because the project " + projectId.get() + " is not known");
        }
        return project.get();
    }
}
