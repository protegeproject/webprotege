package edu.stanford.bmir.protege.web.client.perspective;


import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDescriptor;
import edu.stanford.bmir.protege.web.shared.place.ItemSelection;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/06/2014
 */
public class PerspectiveSwitcherPresenter implements HasDispose {

    private final ProjectId projectId;

    private final PerspectiveSwitcherView view;

    private final ProjectPerspectivesService projectPerspectivesService;

    private final PlaceController placeController;

    private final CreateFreshPerspectiveRequestHandler createFreshPerspectiveRequestHandler;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private final Map<PerspectiveId, ItemSelection> perspective2Selection = new HashMap<>();

    private final List<PerspectiveDescriptor> perspectiveDescriptors = new ArrayList<>();

    @Inject
    public PerspectiveSwitcherPresenter(ProjectId projectId, PerspectiveSwitcherView view,
                                        ProjectPerspectivesService projectPerspectivesService,
                                        CreateFreshPerspectiveRequestHandler createFreshPerspectiveRequestHandler,
                                        PlaceController placeController,
                                        final EventBus eventBus,
                                        LoggedInUserProjectPermissionChecker permissionChecker) {
        this.projectId = checkNotNull(projectId);
        this.view = view;
        this.createFreshPerspectiveRequestHandler = createFreshPerspectiveRequestHandler;
        this.projectPerspectivesService = projectPerspectivesService;
        this.placeController = placeController;
        this.permissionChecker = permissionChecker;
        eventBus.addHandler(PlaceChangeEvent.TYPE, event -> {
            if(event.getNewPlace() instanceof ProjectViewPlace) {
                displayPlace((ProjectViewPlace) event.getNewPlace());
            }
        });
        view.setPerspectiveActivatedHandler(this::goToPlaceForPerspective);
        view.setAddToFavoritePerspectivesHandler(perspectiveDescriptor -> addFavoritePerspective(perspectiveDescriptor.getPerspectiveId()));
        view.setResetPerspectiveToDefaultStateHandler(perspectiveDescriptor -> eventBus.fireEvent(new ResetPerspectiveEvent(perspectiveDescriptor)));
        view.setAddViewHandler(perspectiveId -> eventBus.fireEvent(new AddViewToPerspectiveEvent(perspectiveId)));
    }

    public void start(AcceptsOneWidget container, EventBus eventBus, ProjectViewPlace place) {
        GWT.log("[PerspectiveSwitcherPresenter] start with place: " + place);
        container.setWidget(view);
        projectPerspectivesService.getPerspectives((perspectives, resettablePerspectives) -> {
            view.setResettablePerspectives(resettablePerspectives);
            setUserProjectPerspectives(perspectives);
            displayPlace(place);
        });
        view.setAddPerspectiveAllowed(false);
        view.setClosePerspectiveAllowed(false);
        view.setAddViewAllowed(false);
        view.setManagePerspectivesAllowed(false);
        permissionChecker.hasPermission(ADD_OR_REMOVE_PERSPECTIVE,
                                        canAddRemove -> {
                                            view.setClosePerspectiveAllowed(canAddRemove);
                                            view.setAddPerspectiveAllowed(canAddRemove);
                                            view.setManagePerspectivesAllowed(canAddRemove);
                                            if(canAddRemove) {
                                                view.setAddBlankPerspectiveHandler(this::handleCreateNewPerspective);
                                                view.setRemoveFromFavoritePerspectivesHandler(this::handleRemoveFavoritePerspective);
                                                view.setManagePerspectivesHandler(this::handleManage);
                                            }
        });
        permissionChecker.hasPermission(ADD_OR_REMOVE_VIEW,
                                        view::setAddViewAllowed);
    }

    private void handleManage() {
        Place currentPlace = placeController.getWhere();
        placeController.goTo(PerspectivesManagerPlace.get(projectId, currentPlace));
    }

    /**
     * Sets the linked perspectives and displays the  specified perspective
     * @param perspectiveDescriptors The perspectives to display.
     */
    private void setUserProjectPerspectives(List<PerspectiveDescriptor> perspectiveDescriptors) {
        GWT.log("[PerspectiveSwitcherPresenter] setUserProjectPerspectives");
        this.perspectiveDescriptors.clear();
        this.perspectiveDescriptors.addAll(perspectiveDescriptors);
        ImmutableList<PerspectiveDescriptor> favorites = perspectiveDescriptors.stream()
                                                                             .filter(PerspectiveDescriptor::isFavorite)
                                                                             .collect(toImmutableList());
        view.setFavourites(favorites);
        view.setAvailablePerspectives(perspectiveDescriptors);
        Optional<PerspectiveId> perspectiveId = getCurrentPlacePerspectiveId();
        if (perspectiveId.isPresent()) {
//            ensurePerspectiveLinkExists(perspectiveId.get());
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
//        ensurePerspectiveLinkExists(perspectiveId);
        ensurePerspectiveLinkIsActive(perspectiveId);
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


    private void handleRemoveFavoritePerspective(final PerspectiveId perspectiveId) {
        List<PerspectiveDescriptor> updatedPerspectivesList = withFavorite(perspectiveId, false);
        setUserProjectPerspectives(updatedPerspectivesList);
        projectPerspectivesService.setPerspectives(updatedPerspectivesList, (descriptors, resettable) -> {
            view.setResettablePerspectives(resettable);
            Optional<PerspectiveId> currentPlacePerspective = getCurrentPlacePerspectiveId();
            if (currentPlacePerspective.isPresent() && currentPlacePerspective.get().equals(perspectiveId)) {
                // Need to change place
                PerspectiveId nextPerspective = perspectiveDescriptors.get(0).getPerspectiveId();
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
        createFreshPerspectiveRequestHandler.createFreshPerspective(newPerspectiveDescriptor -> {
            GWT.log("[PerspectiveSwitcherPresenter] Create new perspective: " + newPerspectiveDescriptor);
            ArrayList<PerspectiveDescriptor> updatedList = new ArrayList<>(this.perspectiveDescriptors);
            updatedList.add(newPerspectiveDescriptor.withFavorite(true));
            projectPerspectivesService.setPerspectives(updatedList, (perspectives, resettablePerspectives) -> {
                view.setResettablePerspectives(resettablePerspectives);
                setUserProjectPerspectives(updatedList);
                goToPlaceForPerspective(newPerspectiveDescriptor.getPerspectiveId());
            });
        });
    }

    private void addFavoritePerspective(PerspectiveId perspectiveId) {
        ImmutableList<PerspectiveDescriptor> updatedList = withFavorite(perspectiveId, true);
        projectPerspectivesService.setPerspectives(updatedList, (perspectives, resettablePerspectives) -> {
            view.setResettablePerspectives(resettablePerspectives);
            setUserProjectPerspectives(updatedList);
            goToPlaceForPerspective(perspectiveId);
        });
    }

    private ImmutableList<PerspectiveDescriptor> withFavorite(@Nonnull PerspectiveId perspectiveId, boolean favorite) {
        return perspectiveDescriptors.stream()
                              .map(perspectiveDescriptor -> {
                                  if(perspectiveDescriptor.getPerspectiveId().equals(perspectiveId)) {
                                      return perspectiveDescriptor.withFavorite(favorite);
                                  }
                                  else {
                                      return perspectiveDescriptor;
                                  }
                              })
                              .collect(toImmutableList());
    }

    @Override
    public void dispose() {

    }
}
