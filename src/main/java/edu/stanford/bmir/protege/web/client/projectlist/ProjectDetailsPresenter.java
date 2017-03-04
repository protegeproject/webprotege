package edu.stanford.bmir.protege.web.client.projectlist;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.event.dom.client.ClickEvent;
import edu.stanford.bmir.protege.web.client.ui.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.projectmanager.DownloadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.projectmanager.LoadProjectInNewWindowRequestHandler;
import edu.stanford.bmir.protege.web.client.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.projectmanager.TrashManagerRequestHandler;
import edu.stanford.bmir.protege.web.shared.TimeUtil;
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
    }

    public void start() {
        view.setProject(details.getProjectId(), details.getDisplayName());
        view.setProjectOwner(details.getOwner());
        long modifiedAtTs = details.getLastModifiedAt();
        String modifiedAt;
        if(modifiedAtTs != 0) {
            modifiedAt = TimeUtil.getTimeRendering(modifiedAtTs);
        }
        else {
            modifiedAt = "";
        }
        view.setModifiedAt(modifiedAt);
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
        AbstractUiAction downloadAction = new AbstractUiAction("Download") {
            @Override
            public void execute(ClickEvent e) {
                downloadProjectRequestHandler.handleProjectDownloadRequest(details.getProjectId());
            }
        };
        downloadAction.setEnabled(false);
        view.addAction(downloadAction);
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
