package edu.stanford.bmir.protege.web.client.ui.projectlist;

import edu.stanford.bmir.protege.web.client.ui.projectmanager.DownloadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.LoadProjectInNewWindowRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.TrashManagerRequestHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/16
 */
// TODO: Auto-generate
public class ProjectDetailsPresenterFactory {

    @Nonnull
    private Provider<ProjectDetailsView> viewProvider;

    @Nonnull
    private LoadProjectInNewWindowRequestHandler loadInNewWindowRequestHandler;

    @Nonnull
    private TrashManagerRequestHandler trashManagerRequestHandler;

    @Nonnull
    private LoadProjectRequestHandler loadProjectRequestHandler;

    @Nonnull
    private DownloadProjectRequestHandler downloadProjectRequestHandler;

    @Inject
    public ProjectDetailsPresenterFactory(@Nonnull Provider<ProjectDetailsView> viewProvider,
                                          @Nonnull LoadProjectInNewWindowRequestHandler loadInNewWindowRequestHandler,
                                          @Nonnull TrashManagerRequestHandler trashManagerRequestHandler,
                                          @Nonnull LoadProjectRequestHandler loadProjectRequestHandler,
                                          @Nonnull DownloadProjectRequestHandler downloadProjectRequestHandler) {
        this.viewProvider = checkNotNull(viewProvider);
        this.loadInNewWindowRequestHandler = checkNotNull(loadInNewWindowRequestHandler);
        this.trashManagerRequestHandler = checkNotNull(trashManagerRequestHandler);
        this.loadProjectRequestHandler = checkNotNull(loadProjectRequestHandler);
        this.downloadProjectRequestHandler = checkNotNull(downloadProjectRequestHandler);
    }

    public ProjectDetailsPresenter createPresenter(ProjectDetails projectDetails) {
        return new ProjectDetailsPresenter(projectDetails,
                                           viewProvider.get(),
                                           loadInNewWindowRequestHandler,
                                           trashManagerRequestHandler,
                                           loadProjectRequestHandler,
                                           downloadProjectRequestHandler);
    }
}
