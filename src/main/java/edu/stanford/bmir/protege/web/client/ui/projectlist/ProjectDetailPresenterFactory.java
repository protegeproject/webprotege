package edu.stanford.bmir.protege.web.client.ui.projectlist;

import edu.stanford.bmir.protege.web.client.ui.projectmanager.DownloadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.LoadProjectInNewWindowRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.TrashManagerRequestHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/16
 */
// TODO: Auto-generate
public class ProjectDetailPresenterFactory {

    @Nonnull
    private ProjectDetailsView view;

    @Nonnull
    private LoadProjectInNewWindowRequestHandler loadInNewWindowRequestHandler;

    @Nonnull
    private TrashManagerRequestHandler trashManagerRequestHandler;

    @Nonnull
    private LoadProjectRequestHandler loadProjectRequestHandler;

    @Nonnull
    private DownloadProjectRequestHandler downloadProjectRequestHandler;

    @Inject
    public ProjectDetailPresenterFactory(@Nonnull ProjectDetailsView view,
                                         @Nonnull LoadProjectInNewWindowRequestHandler loadInNewWindowRequestHandler,
                                         @Nonnull TrashManagerRequestHandler trashManagerRequestHandler,
                                         @Nonnull LoadProjectRequestHandler loadProjectRequestHandler,
                                         @Nonnull DownloadProjectRequestHandler downloadProjectRequestHandler) {
        this.view = view;
        this.loadInNewWindowRequestHandler = loadInNewWindowRequestHandler;
        this.trashManagerRequestHandler = trashManagerRequestHandler;
        this.loadProjectRequestHandler = loadProjectRequestHandler;
        this.downloadProjectRequestHandler = downloadProjectRequestHandler;
    }

    public ProjectDetailsPresenter createPresenter(ProjectDetails projectDetails) {
        return new ProjectDetailsPresenter(projectDetails,
                                           view,
                                           loadInNewWindowRequestHandler,
                                           trashManagerRequestHandler,
                                           loadProjectRequestHandler,
                                           downloadProjectRequestHandler);
    }
}
