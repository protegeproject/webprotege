package edu.stanford.bmir.protege.web.client.projectmanager;

import com.google.gwt.place.shared.PlaceController;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
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
        placeController.goTo(ProjectViewPlace.builder(projectId, new PerspectiveId("Classes")).build());
    }
}
