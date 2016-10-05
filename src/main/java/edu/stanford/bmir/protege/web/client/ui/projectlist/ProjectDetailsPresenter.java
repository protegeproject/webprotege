package edu.stanford.bmir.protege.web.client.ui.projectlist;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.event.dom.client.ClickEvent;
import edu.stanford.bmir.protege.web.client.ui.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.DownloadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.LoadProjectInNewWindowRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.TrashManagerRequestHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/16
 */
@AutoFactory
public class ProjectDetailsPresenter {

    @Nonnull
    private final ProjectDetailsView view;

    @Nonnull
    private final ProjectDetails details;

    @Nonnull
    private final TrashManagerRequestHandler trashManagerRequestHandler;

    @Nonnull
    private final LoadProjectRequestHandler loadProjectRequestHandler;

    @Nonnull
    private final DownloadProjectRequestHandler downloadProjectRequestHandler;

    @Nonnull
    private LoadProjectInNewWindowRequestHandler loadProjectInNewWindowRequestHandler;

    @Inject
    public ProjectDetailsPresenter(@Nonnull ProjectDetails details,
                                   @Provided @Nonnull ProjectDetailsView view,
                                   @Provided @Nonnull LoadProjectInNewWindowRequestHandler loadProjectInNewWindowRequestHandler,
                                   @Provided @Nonnull TrashManagerRequestHandler trashManagerRequestHandler,
                                   @Provided @Nonnull LoadProjectRequestHandler loadProjectRequestHandler,
                                   @Provided @Nonnull DownloadProjectRequestHandler downloadProjectRequestHandler) {
        this.view = checkNotNull(view);
        this.details = checkNotNull(details);
        this.loadProjectInNewWindowRequestHandler = checkNotNull(loadProjectInNewWindowRequestHandler);
        this.trashManagerRequestHandler = checkNotNull(trashManagerRequestHandler);
        this.loadProjectRequestHandler = checkNotNull(loadProjectRequestHandler);
        this.downloadProjectRequestHandler = checkNotNull(downloadProjectRequestHandler);
        view.setProject(details.getProjectId(), details.getDisplayName());
        view.setProjectOwner(details.getOwner());
        view.setDescription(details.getDescription());
        view.setInTrash(details.isInTrash());
        view.setLoadProjectRequestHandler(loadProjectRequestHandler);
        addActions();
    }

    public ProjectId getProjectId() {
        return details.getProjectId();
    }

    public UserId getOwner() {
        return details.getOwner();
    }

    public ProjectDetailsView getView() {
        return view;
    }

    private void addActions() {
        addOpenAction();
        addOpenInNewWindowAction();
        addDowloadAction();
        addTrashAction();
    }

    private void addOpenAction() {
        view.addAction(new AbstractUiAction("Open") {
            @Override
            public void execute(ClickEvent e) {
                loadProjectRequestHandler.handleProjectLoadRequest(details.getProjectId());
            }
        });
    }

    private void addOpenInNewWindowAction() {
        view.addAction(new AbstractUiAction("Open in new window") {
            @Override
            public void execute(ClickEvent e) {
                loadProjectInNewWindowRequestHandler.handleLoadProjectInNewWindow(details.getProjectId());
            }
        });
    }

    private void addDowloadAction() {
        view.addAction(new AbstractUiAction("Download") {
            @Override
            public void execute(ClickEvent e) {
                downloadProjectRequestHandler.handleProjectDownloadRequest(details.getProjectId());
            }
        });
    }

    private void addTrashAction() {
        String trashActionLabel;
        if(details.isInTrash()) {
            trashActionLabel = "Remove from trash";
        }
        else {
            trashActionLabel = "Move to trash";
        }
        view.addAction(new AbstractUiAction(trashActionLabel) {
            @Override
            public void execute(ClickEvent e) {
                if (details.isInTrash()) {
                    trashManagerRequestHandler.handleRemoveProjectFromTrash(details.getProjectId());
                }
                else {
                    trashManagerRequestHandler.handleMoveProjectToTrash(details.getProjectId());
                }
            }
        });
    }


}
