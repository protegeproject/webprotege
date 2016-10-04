package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.gwt.place.shared.PlaceController;
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

    @Inject
    public LoadProjectRequestHandlerImpl(PlaceController placeController) {
        this.placeController = placeController;
    }

    @Override
    public void handleProjectLoadRequest(ProjectId projectId) {
        placeController.goTo(ProjectViewPlace.builder(projectId, new PerspectiveId("Classes")).build());
    }
}
