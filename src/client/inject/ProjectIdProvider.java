package edu.stanford.bmir.protege.web.client.inject;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/12/15
 */
public class ProjectIdProvider implements Provider<ProjectId> {

    private static ProjectId projectId;

    public static void setProjectId(ProjectId projectId) {
        ProjectIdProvider.projectId = checkNotNull(projectId);
    }

    @Override
    public ProjectId get() {
        if(projectId == null) {
            throw new RuntimeException("Cannot provide ProjectId as no ProjectId has been set");
        }
        return projectId;
    }
}
