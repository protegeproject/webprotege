package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchService;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.place.ItemSelection;
import edu.stanford.bmir.protege.web.client.place.WebProtegePlaceHistoryMapper;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectivesAction;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectivesResult;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/16
 */
public class LoadProjectInNewWindowRequestHandlerImpl implements LoadProjectInNewWindowRequestHandler {

    private final WebProtegePlaceHistoryMapper placeHistoryMapper;

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    @Inject
    public LoadProjectInNewWindowRequestHandlerImpl(DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider, WebProtegePlaceHistoryMapper placeHistoryMapper) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.placeHistoryMapper = placeHistoryMapper;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    @Override
    public void handleLoadProjectInNewWindow(final ProjectId projectId) {
        UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectivesAction(projectId, userId), new DispatchServiceCallback<GetPerspectivesResult>() {
            @Override
            public void handleSuccess(GetPerspectivesResult result) {
                ImmutableList<PerspectiveId> perspectives = result.getPerspectives();
                handleOpenInNewWindow(perspectives, projectId);
            }
        });
    }

    private void handleOpenInNewWindow(ImmutableList<PerspectiveId> perspectives, ProjectId projectId) {
        PerspectiveId perspectiveId;
        if(perspectives.isEmpty()) {
            perspectiveId = new PerspectiveId("Other");
        }
        else {
            perspectiveId = perspectives.get(0);
        }
        handleOpenInNewWindow(projectId, perspectiveId);
    }

    private void handleOpenInNewWindow(final ProjectId projectId, final PerspectiveId perspectiveId) {
        String token = placeHistoryMapper.getToken(new ProjectViewPlace(projectId, perspectiveId, ItemSelection.empty()));
        GWT.log("[LoadProjectInNewWindowRequestHandlerImpl] Token: " + token);
        String location = Window.Location.createUrlBuilder()
                .setHash(token)
                .buildString();
        GWT.log("[LoadProjectInNewWindowRequestHandlerImpl] Location: " + location);
        Window.open(location, "_blank", "");
    }
}
