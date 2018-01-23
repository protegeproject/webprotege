package edu.stanford.bmir.protege.web.client.perspective;


import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.place.ItemSelection;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.ADD_OR_REMOVE_PERSPECTIVE;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.ADD_OR_REMOVE_VIEW;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/06/2014
 */
public class PerspectiveSwitcherPresenter implements HasDispose {


    private final PerspectiveSwitcherView view;

    private final PerspectiveLinkManager perspectiveLinkManager;

    private final PlaceController placeController;

    private final CreateFreshPerspectiveRequestHandler createFreshPerspectiveRequestHandler;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private final Map<PerspectiveId, ItemSelection> perspective2Selection = new HashMap<>();

    @Inject
    public PerspectiveSwitcherPresenter(PerspectiveSwitcherView view,
                                        PerspectiveLinkManager perspectiveLinkManager,
                                        CreateFreshPerspectiveRequestHandler createFreshPerspectiveRequestHandler,
                                        PlaceController placeController,
                                        final EventBus eventBus,
                                        LoggedInUserProjectPermissionChecker permissionChecker) {
        this.view = view;
        this.createFreshPerspectiveRequestHandler = createFreshPerspectiveRequestHandler;
        this.perspectiveLinkManager = perspectiveLinkManager;
        this.placeController = placeController;
        this.permissionChecker = permissionChecker;
        eventBus.addHandler(PlaceChangeEvent.TYPE, event -> {
            if(event.getNewPlace() instanceof ProjectViewPlace) {
                displayPlace((ProjectViewPlace) event.getNewPlace());
            }
        });
        view.setPerspectiveLinkActivatedHandler(this::goToPlaceForPerspective);
        view.setAddBookMarkedPerspectiveLinkHandler(this::addNewPerspective);
        view.setResetPerspectiveToDefaultStateHandler(perspectiveId -> eventBus.fireEvent(new ResetPerspectiveEvent(perspectiveId)));
        view.setAddViewHandler(perspectiveId -> eventBus.fireEvent(new AddViewToPerspectiveEvent(perspectiveId)));
    }

    public void start(AcceptsOneWidget container, EventBus eventBus, ProjectViewPlace place) {
        GWT.log("[PerspectiveSwitcherPresenter] start with place: " + place);
        container.setWidget(view);
        perspectiveLinkManager.getLinkedPerspectives(perspectiveIds -> {
            setLinkedPerspectives(perspectiveIds);
            displayPlace(place);
        });
        perspectiveLinkManager.getBookmarkedPerspectives(view::setBookmarkedPerspectives);
        view.setAddPerspectiveAllowed(false);
        view.setClosePerspectiveAllowed(false);
        view.setAddViewAllowed(false);
        permissionChecker.hasPermission(ADD_OR_REMOVE_PERSPECTIVE,
                                        canAddRemove -> {
                                            view.setClosePerspectiveAllowed(canAddRemove);
                                            view.setAddPerspectiveAllowed(canAddRemove);
                                            view.setAddPerspectiveLinkRequestHandler(this::handleCreateNewPerspective);
                                            view.setRemovePerspectiveLinkHandler(this::handleRemoveLinkedPerspective);
                                        });
        permissionChecker.hasPermission(ADD_OR_REMOVE_VIEW,
                                        view::setAddViewAllowed);
    }

    /**
     * Sets the linked perspectives and displays the specified perspective
     * @param linkedPerspective The links to display.
     */
    private void setLinkedPerspectives(List<PerspectiveId> linkedPerspective) {
        GWT.log("[PerspectiveSwitcherPresenter] setLinkedPerspectives");
        view.setPerspectiveLinks(linkedPerspective);
        Optional<PerspectiveId> perspectiveId = getCurrentPlacePerspectiveId();
        if (perspectiveId.isPresent()) {
            ensurePerspectiveLinkExists(perspectiveId.get());
            ensurePerspectiveLinkIsActive(perspectiveId.get());
        }
    }

    /**
     * Displays the place.  The "tab" corresponding to the place PerspectiveId will be highlighted.  A fresh "tab" will be
     * added to the last position if there is no tab existing tab that corresponds to the place PerspectiveId.
     * @param place The place to display.  Not {@code null}.
     * @throws NullPointerException if {@code place} is {@code null}.
     */
    private void displayPlace(ProjectViewPlace place) {
        GWT.log("[PerspectiveSwitcherPresenter] displayPlace: " + place);
        PerspectiveId perspectiveId = checkNotNull(place).getPerspectiveId();
        ensurePerspectiveLinkExists(perspectiveId);
        ensurePerspectiveLinkIsActive(perspectiveId);
    }

    /**
     * Ensures that the specified perspectiveId has a corresponding link in the view.
     * @param perspectiveId The perspective id to check.  Not {@code null}.
     */
    private void ensurePerspectiveLinkExists(PerspectiveId perspectiveId) {
        List<PerspectiveId> currentLinks = view.getPerspectiveLinks();
        if(!currentLinks.contains(perspectiveId)) {
            GWT.log("[PerspectiveSwitcherPresenter] Adding perspective link for " + perspectiveId + " because it is not present");
            addNewPerspective(perspectiveId);
        }
    }

    private void ensurePerspectiveLinkIsActive(PerspectiveId perspectiveId) {
        view.setHighlightedPerspective(perspectiveId);
    }

    public void stop() {
    }



    private void goToPlaceForPerspective(PerspectiveId perspectiveId) {
        Place currentPlace = placeController.getWhere();
        GWT.log("[PerspectiveSwitcherPresenter] Current Place: " + currentPlace);
        if(!(currentPlace instanceof ProjectViewPlace)) {
            return;
        }
        ProjectViewPlace projectPerspectivePlace = (ProjectViewPlace) currentPlace;
        // Preserve the selection for the current perspective.
        // (This could be optional.  Previously, we carried selection over)
        perspective2Selection.put(projectPerspectivePlace.getPerspectiveId(), projectPerspectivePlace.getItemSelection());
        ItemSelection previousSelection = perspective2Selection.get(perspectiveId);
        ProjectViewPlace.Builder builder = projectPerspectivePlace.builder();
        builder.withPerspectiveId(perspectiveId);
        if(previousSelection != null) {
            // Restore the selection
            builder.clearSelection();
            builder.withSelectedItems(previousSelection);
        }
        ProjectViewPlace nextPlace = builder.build();
        placeController.goTo(nextPlace);
    }


    private void handleRemoveLinkedPerspective(final PerspectiveId perspectiveId) {
        perspectiveLinkManager.removeLinkedPerspective(perspectiveId, perspectiveIds -> {
            view.setPerspectiveLinks(perspectiveIds);
            Optional<PerspectiveId> currentPlacePerspective = getCurrentPlacePerspectiveId();
            if (currentPlacePerspective.isPresent() && currentPlacePerspective.get().equals(perspectiveId)) {
                // Need to change place
                PerspectiveId nextPerspective = perspectiveIds.get(0);
                goToPlaceForPerspective(nextPerspective);
            }
        });

    }

    private Optional<PerspectiveId> getCurrentPlacePerspectiveId() {
        Place currentPlace = placeController.getWhere();
        if(!(currentPlace instanceof ProjectViewPlace)) {
            return Optional.empty();
        }
        ProjectViewPlace projectViewPlace = (ProjectViewPlace) currentPlace;
        PerspectiveId currentPlacePerspective = projectViewPlace.getPerspectiveId();
        return Optional.of(currentPlacePerspective);
    }

    private void handleCreateNewPerspective() {
        createFreshPerspectiveRequestHandler.createFreshPerspective(this::addNewPerspective);
    }

    private void addNewPerspective(final PerspectiveId perspectiveId) {
        perspectiveLinkManager.addLinkedPerspective(perspectiveId, perspectiveIds -> {
            view.setPerspectiveLinks(perspectiveIds);
            goToPlaceForPerspective(perspectiveId);
        });
    }

    @Override
    public void dispose() {

    }
}
