package edu.stanford.bmir.protege.web.client.ui.projectlist;

import com.google.inject.assistedinject.Assisted;
import edu.stanford.bmir.protege.web.client.ui.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.DownloadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.LoadProjectInNewWindowRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.TrashManagerRequestHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/16
 */
public class ProjectDetailsPresenter {

    private final ProjectDetailsView view;

    private final ProjectDetails details;

    private final TrashManagerRequestHandler trashManagerRequestHandler;

    private final LoadProjectRequestHandler loadProjectRequestHandler;

    private final DownloadProjectRequestHandler downloadProjectRequestHandler;

    private LoadProjectInNewWindowRequestHandler loadProjectInNewWindowRequestHandler;

    @Inject
    public ProjectDetailsPresenter(@Assisted ProjectDetails details, ProjectDetailsView view, LoadProjectInNewWindowRequestHandler loadProjectInNewWindowRequestHandler, TrashManagerRequestHandler trashManagerRequestHandler, LoadProjectRequestHandler loadProjectRequestHandler, DownloadProjectRequestHandler downloadProjectRequestHandler) {
        this.view = view;
        this.details = details;
        this.loadProjectInNewWindowRequestHandler = loadProjectInNewWindowRequestHandler;
        this.trashManagerRequestHandler = trashManagerRequestHandler;
        this.loadProjectRequestHandler = loadProjectRequestHandler;
        this.downloadProjectRequestHandler = downloadProjectRequestHandler;
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
            public void execute() {
                loadProjectRequestHandler.handleProjectLoadRequest(details.getProjectId());
            }
        });
    }

    private void addOpenInNewWindowAction() {
        view.addAction(new AbstractUiAction("Open in new window") {
            @Override
            public void execute() {
                loadProjectInNewWindowRequestHandler.handleLoadProjectInNewWindow(details.getProjectId());
            }
        });
    }

    private void addDowloadAction() {
        view.addAction(new AbstractUiAction("Download") {
            @Override
            public void execute() {
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
            public void execute() {
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
