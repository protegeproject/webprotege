package edu.stanford.bmir.protege.web.client.projectmanager;

import com.google.common.collect.ImmutableList;
import com.google.gwt.place.shared.PlaceController;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectivesAction;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectivesResult;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDescriptor;
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

    private final LoggedInUserProvider loggedInUserProvider;

    @Inject
    public LoadProjectRequestHandlerImpl(PlaceController placeController,
                                         DispatchServiceManager dispatchServiceManager,
                                         LoggedInUserProvider loggedInUserProvider) {
        this.placeController = placeController;
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    @Override
    public void handleProjectLoadRequest(ProjectId projectId) {
        dispatchServiceManager.execute(new GetPerspectivesAction(projectId, loggedInUserProvider.getCurrentUserId()),
                                       result -> handlePerspectives(result, projectId));

    }

    private void handlePerspectives(GetPerspectivesResult result, ProjectId projectId) {
        ImmutableList<PerspectiveDescriptor> perspectives = result.getPerspectives();
        if(perspectives.isEmpty()) {
            // Nothing to go to
            return;
        }
        PerspectiveDescriptor perspectiveDescriptor = perspectives.get(0);
        PerspectiveId perspectiveId = perspectiveDescriptor.getPerspectiveId();
        placeController.goTo(ProjectViewPlace.builder(projectId, perspectiveId).build());

    }
}
