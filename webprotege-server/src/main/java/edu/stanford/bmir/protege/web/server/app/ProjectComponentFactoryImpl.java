package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.server.inject.ProjectComponent;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectModule;
import edu.stanford.bmir.protege.web.server.project.ProjectComponentFactory;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jan 2018
 */
public class ProjectComponentFactoryImpl implements ProjectComponentFactory {

    @Nonnull
    private final ServerComponent serverComponent;

    @Inject
    public ProjectComponentFactoryImpl(@Nonnull ServerComponent serverComponent) {
        this.serverComponent = serverComponent;
    }

    @Nonnull
    @Override
    public ProjectComponent createProjectComponent(@Nonnull ProjectId projectId) {
        return serverComponent.getProjectComponent(new ProjectModule(projectId));
    }
}
