package edu.stanford.bmir.protege.web.server.shortform;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-14
 */
@Module
public class ProjectIdModule {

    private final ProjectId projectId;

    public ProjectIdModule(ProjectId projectId) {
        this.projectId = projectId;
    }


    @Provides
    @ProjectSingleton
    public ProjectId getProjectId() {
        return projectId;
    }
}
