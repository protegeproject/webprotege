package edu.stanford.bmir.protege.web.client.perspective;


import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.ui.CreateFreshPerspectiveRequestHandler;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;

import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/06/2014
 */
public class PerspectiveSwitcherPresenter implements HasDispose {


    private final PerspectiveSwitcherView view;

    private final PerspectiveLinkManager perspectiveLinkManager;

    private final PlaceController placeController;

    private final CreateFreshPerspectiveRequestHandler createFreshPerspectiveRequestHandler;

    @Inject
    public PerspectiveSwitcherPresenter(PerspectiveSwitcherView view,
                                        PerspectiveLinkManager perspectiveLinkManager,
                                        CreateFreshPerspectiveRequestHandler createFreshPerspectiveRequestHandler,
                                        PlaceController placeController,
                                        final EventBus eventBus) {
        this.view = view;
        this.createFreshPerspectiveRequestHandler = createFreshPerspectiveRequestHandler;
        this.perspectiveLinkManager = perspectiveLinkManager;
        this.placeController = placeController;
        eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
            @Override
            public void onPlaceChange(PlaceChangeEvent event) {
                if(event.getNewPlace() instanceof ProjectViewPlace) {
                    displayPlace((ProjectViewPlace) event.getNewPlace());
                }
            }
        });
        view.setPerspectiveLinkActivatedHandler(new PerspectiveSwitcherView.PerspectiveLinkActivatedHandler() {
            public void handlePerspectiveLinkActivated(PerspectiveId perspectiveId) {
                goToPlaceForPerspective(perspectiveId);
            }
        });
        view.setRemovePerspectiveLinkHandler(new PerspectiveSwitcherView.RemovePerspectiveLinkRequestHandler() {
            public void handleRemovePerspectiveLinkRequest(PerspectiveId perspectiveId) {
                handleRemoveLinkedPerspective(perspectiveId);
            }
        });
        view.setAddPerspectiveLinkRequestHandler(new PerspectiveSwitcherView.AddPerspectiveLinkRequestHandler() {
            public void handleAddNewPerspectiveLinkRequest() {
                handleCreateNewPerspective();
            }
        });
        view.setAddBookMarkedPerspectiveLinkHandler(new PerspectiveSwitcherView.AddBookmarkedPerspectiveLinkHandler() {
            @Override
            public void handleAddBookmarkedPerspective(PerspectiveId perspectiveId) {
                addNewPerspective(perspectiveId);
            }
        });
        view.setResetPerspectiveToDefaultStateHandler(new PerspectiveSwitcherView.ResetPerspectiveToDefaultStateHandler() {
            @Override
            public void handleResetPerspectiveToDefaultState(PerspectiveId perspectiveId) {
                eventBus.fireEvent(new ResetPerspectiveEvent(perspectiveId));
            }
        });
        view.setAddViewHandler(new PerspectiveSwitcherView.AddViewHandler() {
            @Override
            public void handleAddViewToPerspective(PerspectiveId perspectiveId) {
                eventBus.fireEvent(new AddViewToPerspectiveEvent(perspectiveId));
            }
        });
    }

    public void start(AcceptsOneWidget container, final ProjectViewPlace place) {
        GWT.log("[PerspectiveSwitcherPresenter] start with place: " + place);
        checkNotNull(container);
        checkNotNull(place);
        container.setWidget(view);
        perspectiveLinkManager.getLinkedPerspectives(new PerspectiveLinkManager.Callback() {
            public void handlePerspectives(List<PerspectiveId> perspectiveIds) {
                setLinkedPerspectives(perspectiveIds);
                displayPlace(place);
            }
        });
        perspectiveLinkManager.getBookmarkedPerspectives(new PerspectiveLinkManager.Callback() {
            @Override
            public void handlePerspectives(List<PerspectiveId> perspectiveIds) {
                view.setBookmarkedPerspectives(perspectiveIds);
            }
        });

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
        checkNotNull(place);
        PerspectiveId perspectiveId = place.getPerspectiveId();
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
        ProjectViewPlace nextPlace = projectPerspectivePlace.builder().withPerspectiveId(perspectiveId).build();
        placeController.goTo(nextPlace);
    }


    private void handleRemoveLinkedPerspective(final PerspectiveId perspectiveId) {
        perspectiveLinkManager.removeLinkedPerspective(perspectiveId, new PerspectiveLinkManager.Callback() {
            public void handlePerspectives(List<PerspectiveId> perspectiveIds) {
                view.setPerspectiveLinks(perspectiveIds);
                Optional<PerspectiveId> currentPlacePerspective = getCurrentPlacePerspectiveId();
                if (currentPlacePerspective.isPresent() && currentPlacePerspective.get().equals(perspectiveId)) {
                    // Need to change place
                    PerspectiveId nextPerspective = perspectiveIds.get(0);
                    goToPlaceForPerspective(nextPerspective);
                }
            }
        });

    }

    private Optional<PerspectiveId> getCurrentPlacePerspectiveId() {
        Place currentPlace = placeController.getWhere();
        if(!(currentPlace instanceof ProjectViewPlace)) {
            return Optional.absent();
        }
        ProjectViewPlace projectViewPlace = (ProjectViewPlace) currentPlace;
        PerspectiveId currentPlacePerspective = projectViewPlace.getPerspectiveId();
        return Optional.of(currentPlacePerspective);
    }

    private void handleCreateNewPerspective() {
        createFreshPerspectiveRequestHandler.createFreshPerspective(new CreateFreshPerspectiveRequestHandler.Callback() {
            @Override
            public void createNewPerspective(PerspectiveId perspectiveId) {
                addNewPerspective(perspectiveId);
            }
        });
    }

    private void addNewPerspective(final PerspectiveId perspectiveId) {
        perspectiveLinkManager.addLinkedPerspective(perspectiveId, new PerspectiveLinkManager.Callback() {
            public void handlePerspectives(List<PerspectiveId> perspectiveIds) {
                view.setPerspectiveLinks(perspectiveIds);
                goToPlaceForPerspective(perspectiveId);
            }
        });
    }

    @Override
    public void dispose() {

    }
}
