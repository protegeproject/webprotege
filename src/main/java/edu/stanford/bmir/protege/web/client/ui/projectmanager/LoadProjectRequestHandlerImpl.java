package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.gwt.place.shared.PlaceController;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.progress.ProgressMonitor;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.LoadProjectAction;
import edu.stanford.bmir.protege.web.shared.project.LoadProjectResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/16
 */
public class LoadProjectRequestHandlerImpl implements LoadProjectRequestHandler {

    private PlaceController placeController;

    private DispatchServiceManager dispatchServiceManager;

    @Inject
    public LoadProjectRequestHandlerImpl(PlaceController placeController, DispatchServiceManager dispatchServiceManager) {
        this.placeController = placeController;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public void handleProjectLoadRequest(ProjectId projectId) {
        ProgressMonitor.get().showProgressMonitor("Loading project", "Please wait");
        dispatchServiceManager.execute(new LoadProjectAction(projectId),
                                       new DispatchServiceCallback<LoadProjectResult>() {
                                           @Override
                                           public void handleSuccess(LoadProjectResult loadProjectResult) {
                                               placeController.goTo(ProjectViewPlace.builder(projectId, new PerspectiveId("Classes")).build());
                                           }

                                           @Override
                                           public void handleFinally() {
                                               ProgressMonitor.get().hideProgressMonitor();
                                           }
                                       });

    }
}
