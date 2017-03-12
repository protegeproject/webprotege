package edu.stanford.bmir.protege.web.client.project;

import edu.stanford.bmir.protege.web.client.projectsettings.ProjectSettingsPresenter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2013
 */
public class ShowProjectDetailsHandlerImpl implements ShowProjectDetailsHandler {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ProjectSettingsPresenter presenter;

    @Inject
    public ShowProjectDetailsHandlerImpl(@Nonnull ProjectId projectId,
                                         @Nonnull ProjectSettingsPresenter presenter) {
        this.projectId = projectId;
        this.presenter = presenter;
    }

    @Override
    public void handleShowProjectDetails() {
        presenter.showDialog(projectId);
    }
}
