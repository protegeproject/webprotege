package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.place.ItemSelection;
import edu.stanford.bmir.protege.web.client.place.WebProtegePlaceHistoryMapper;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/16
 */
public class LoadProjectInNewWindowRequestHandlerImpl implements LoadProjectInNewWindowRequestHandler {

    private final WebProtegePlaceHistoryMapper placeHistoryMapper;

    @Inject
    public LoadProjectInNewWindowRequestHandlerImpl(WebProtegePlaceHistoryMapper placeHistoryMapper) {
        this.placeHistoryMapper = placeHistoryMapper;
    }

    @Override
    public void handleLoadProjectInNewWindow(ProjectId projectId) {
        String token = placeHistoryMapper.getToken(new ProjectViewPlace(projectId, new PerspectiveId("Classes"), ItemSelection.empty()));
        GWT.log("[LoadProjectInNewWindowRequestHandlerImpl] Token: " + token);
        String location = Window.Location.createUrlBuilder()
                .setHash(token)
                .buildString();
        GWT.log("[LoadProjectInNewWindowRequestHandlerImpl] Location: " + location);
        Window.open(location, "_blank", "");
    }
}
