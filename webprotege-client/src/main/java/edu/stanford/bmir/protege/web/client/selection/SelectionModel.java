package edu.stanford.bmir.protege.web.client.selection;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Timer;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.place.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/05/15
 */
@ProjectSingleton
public class SelectionModel {

    private final DispatchServiceManager dispatch;

    private final EventBus eventBus;

    private final SelectedEntityManager<OWLClass> selectedClassManager;

    private final SelectedEntityManager<OWLObjectProperty> selectedObjectPropertyManager;

    private final SelectedEntityManager<OWLDataProperty> selectedDataPropertyManager;

    private final SelectedEntityManager<OWLAnnotationProperty> selectedAnnotationPropertyManager;

    private final SelectedEntityManager<OWLDatatype> selectedDatatypeManager;

    private final SelectedEntityManager<OWLNamedIndividual> selectedIndividualManager;

    private final PlaceController placeController;

    private ItemSelection selection = ItemSelection.empty();

    private final Timer selectionTransmitDelayTimer = new Timer() {
        @Override
        public void run() {
            processPendingSelection();
        }
    };

    private final List<OWLEntity> pendingSelection = new ArrayList<>();

    @Inject
    public SelectionModel(DispatchServiceManager dispatch,
                          EventBus eventBus,
                          PlaceController placeController,
                          SelectedEntityManager<OWLClass> selectedClassManager,
                          SelectedEntityManager<OWLObjectProperty> selectedObjectPropertyManager,
                          SelectedEntityManager<OWLDataProperty> selectedDataPropertyManager,
                          SelectedEntityManager<OWLAnnotationProperty> selectedAnnotationPropertyManager,
                          SelectedEntityManager<OWLDatatype> selectedDatatypeManager,
                          SelectedEntityManager<OWLNamedIndividual> selectedIndividualManager) {
        this.dispatch = dispatch;
        this.eventBus = eventBus;
        this.placeController = placeController;
        this.selectedClassManager = checkNotNull(selectedClassManager);
        this.selectedObjectPropertyManager = checkNotNull(selectedObjectPropertyManager);
        this.selectedDataPropertyManager = checkNotNull(selectedDataPropertyManager);
        this.selectedAnnotationPropertyManager = checkNotNull(selectedAnnotationPropertyManager);
        this.selectedDatatypeManager = checkNotNull(selectedDatatypeManager);
        this.selectedIndividualManager = checkNotNull(selectedIndividualManager);
        eventBus.addHandler(PlaceChangeEvent.TYPE, event -> {
            Place newPlace = event.getNewPlace();
            if(newPlace instanceof ProjectViewPlace) {
                ProjectViewPlace projectViewPlace = (ProjectViewPlace) newPlace;
                updateSelectionFromPlace(projectViewPlace);
            }
        });
    }

    private void processPendingSelection() {
        if(pendingSelection.isEmpty()) {
            clearSelectionNow();
        }
        else {
            OWLEntity entity = pendingSelection.get(0);
            pendingSelection.clear();
            setSelectionNow(entity);
        }
    }

    private void updateSelectionFromPlace(ProjectViewPlace place) {
        ItemSelection itemSelection = place.getItemSelection();
        if(!itemSelection.equals(selection)) {
            Optional<OWLEntity> previousSelection = extractEntityFromItem(selection);
            selection = itemSelection;
            itemSelection.visitItems(OWLClassItem.getType(),
                                     object -> selectedClassManager.setSelection(object.getItem()));

            itemSelection.visitItems(OWLObjectPropertyItem.getType(),
                                     object -> selectedObjectPropertyManager.setSelection(object.getItem()));

            itemSelection.visitItems(OWLDataPropertyItem.getType(),
                                     object -> selectedDataPropertyManager.setSelection(object.getItem()));

            itemSelection.visitItems(OWLAnnotationPropertyItem.getType(),
                                     object -> selectedAnnotationPropertyManager.setSelection(object.getItem()));
            
            itemSelection.visitItems(OWLNamedIndividualItem.getType(),
                                     object -> selectedIndividualManager.setSelection(object.getItem()));
            
            fireEvent(previousSelection);
        }
    }

    public HandlerRegistration addSelectionChangedHandler(EntitySelectionChangedHandler handler) {
        return eventBus.addHandler(EntitySelectionChangedEvent.getType(), handler);
    }

    public Optional<OWLEntity> getSelection() {
        Place place = placeController.getWhere();
        if(!(place instanceof ProjectViewPlace)) {
            return Optional.empty();
        }
        ProjectViewPlace projectViewPlace = (ProjectViewPlace) place;
        return extractEntityFromItem(projectViewPlace.getItemSelection());
    }

    private static Optional<OWLEntity> extractEntityFromItem(ItemSelection itemSelection) {
        Optional<Item<?>> firstItem = itemSelection.getFirst();
        if(!firstItem.isPresent()) {
            return Optional.empty();
        }
        Object selItem = firstItem.get().getItem();
        if(selItem instanceof OWLEntity) {
            return Optional.of((OWLEntity) selItem);
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<OWLClass> getLastSelectedClass() {
        return selectedClassManager.getLastSelection();
    }

    public Optional<OWLObjectProperty> getLastSelectedObjectProperty() {
        return selectedObjectPropertyManager.getLastSelection();
    }

    public Optional<OWLDataProperty> getLastSelectedDataProperty() {
        return selectedDataPropertyManager.getLastSelection();
    }

    public Optional<OWLAnnotationProperty> getLastSelectedAnnotationProperty() {
        return selectedAnnotationPropertyManager.getLastSelection();
    }

    public Optional<OWLDatatype> getLastSelectedDatatype() {
        return selectedDatatypeManager.getLastSelection();
    }

    public Optional<OWLNamedIndividual> getLastSelectedNamedIndividual() {
        return selectedIndividualManager.getLastSelection();
    }

    public void clearSelection() {
        pendingSelection.clear();
        restartSelectionTimer();
    }

    private void restartSelectionTimer() {
        selectionTransmitDelayTimer.cancel();
        selectionTransmitDelayTimer.schedule(100);
    }

    private void clearSelectionNow() {
        Place place = placeController.getWhere();
        if(!(place instanceof ProjectViewPlace)) {
            return;
        }
        ProjectViewPlace projectViewPlace = (ProjectViewPlace) place;
        if(projectViewPlace.getItemSelection().isEmpty()) {
            return;
        }
        Place nextPlace = projectViewPlace.builder().clearSelection().build();
        placeController.goTo(nextPlace);
    }

    public void setSelection(OWLEntity entity) {
        pendingSelection.add(0, entity);
        restartSelectionTimer();
    }

    private void setSelectionNow(OWLEntity entity) {
        Place place = placeController.getWhere();
        if(!(place instanceof ProjectViewPlace)) {
            return;
        }
        Item<?> item = entity
                .accept(new OWLEntityVisitorEx<Item<?>>() {
                    @Nonnull
                    @Override
                    public Item<?> visit(@Nonnull OWLClass desc) {
                        selectedClassManager.setSelection(desc);
                        return new OWLClassItem(desc);
                    }

                    @Nonnull
                    @Override
                    public Item<?> visit(@Nonnull OWLDatatype node) {
                        selectedDatatypeManager.setSelection(node);
                        throw new RuntimeException("Not Supported");
                    }

                    @Nonnull
                    @Override
                    public Item<?> visit(@Nonnull OWLDataProperty property) {
                        selectedDataPropertyManager.setSelection(property);
                        return new OWLDataPropertyItem(property);
                    }

                    @Nonnull
                    @Override
                    public Item<?> visit(@Nonnull OWLObjectProperty property) {
                        selectedObjectPropertyManager.setSelection(property);
                        return new OWLObjectPropertyItem(property);
                    }

                    @Nonnull
                    @Override
                    public Item<?> visit(@Nonnull OWLNamedIndividual individual) {
                        selectedIndividualManager.setSelection(individual);
                        return new OWLNamedIndividualItem(individual);
                    }

                    @Nonnull
                    @Override
                    public Item<?> visit(@Nonnull OWLAnnotationProperty property) {
                        selectedAnnotationPropertyManager.setSelection(property);
                        return new OWLAnnotationPropertyItem(property);
                    }
                });
        ItemSelection nextSelection = ItemSelection.builder().addItem(item).build();
        if(nextSelection.equals(selection)) {
            return;
        }
        ProjectViewPlace projectViewPlace = (ProjectViewPlace) place;
        ProjectViewPlace nextPlace = projectViewPlace.builder()
                .clearSelection()
                .withSelectedItem(item)
                .build();
        placeController.goTo(nextPlace);
    }


    private void fireEvent(Optional<OWLEntity> previousLastSelection) {
        // It's likely that several actions will be executed as a result of the selection
        // change.  These can be nicely batched together so that only one HTTP call takes
        // place.
        dispatch.beginBatch();
        try {
            Optional<OWLEntity> curSelection = extractEntityFromItem(selection);
            eventBus.fireEvent(new EntitySelectionChangedEvent(previousLastSelection, curSelection));
        } finally {
            dispatch.executeCurrentBatch();
        }
    }
}
