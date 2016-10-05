package edu.stanford.bmir.protege.web.shared.selection;

import com.google.common.base.Optional;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.place.*;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/05/15
 */
@Singleton
public class SelectionModel {

    private final EventBus eventBus;

    private final SelectedEntityManager<OWLClass> selectedClassManager;

    private final SelectedEntityManager<OWLObjectProperty> selectedObjectPropertyManager;

    private final SelectedEntityManager<OWLDataProperty> selectedDataPropertyManager;

    private final SelectedEntityManager<OWLAnnotationProperty> selectedAnnotationPropertyManager;

    private final SelectedEntityManager<OWLDatatype> selectedDatatypeManager;

    private final SelectedEntityManager<OWLNamedIndividual> selectedIndividualManager;

    private final PlaceController placeController;

    private ItemSelection selection = ItemSelection.empty();


    @Inject
    public SelectionModel(EventBus eventBus,
                          PlaceController placeController,
                          SelectedEntityManager<OWLClass> selectedClassManager,
                          SelectedEntityManager<OWLObjectProperty> selectedObjectPropertyManager,
                          SelectedEntityManager<OWLDataProperty> selectedDataPropertyManager,
                          SelectedEntityManager<OWLAnnotationProperty> selectedAnnotationPropertyManager,
                          SelectedEntityManager<OWLDatatype> selectedDatatypeManager,
                          SelectedEntityManager<OWLNamedIndividual> selectedIndividualManager) {
        this.eventBus = eventBus;
        this.placeController = placeController;
        this.selectedClassManager = checkNotNull(selectedClassManager);
        this.selectedObjectPropertyManager = checkNotNull(selectedObjectPropertyManager);
        this.selectedDataPropertyManager = checkNotNull(selectedDataPropertyManager);
        this.selectedAnnotationPropertyManager = checkNotNull(selectedAnnotationPropertyManager);
        this.selectedDatatypeManager = checkNotNull(selectedDatatypeManager);
        this.selectedIndividualManager = checkNotNull(selectedIndividualManager);
        eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
            @Override
            public void onPlaceChange(PlaceChangeEvent event) {
                Place newPlace = event.getNewPlace();
                if(newPlace instanceof ProjectViewPlace) {
                    ProjectViewPlace projectViewPlace = (ProjectViewPlace) newPlace;
                    updateSelectionFromPlace(projectViewPlace);
                }
            }
        });
    }

    private void updateSelectionFromPlace(ProjectViewPlace place) {
        ItemSelection itemSelection = place.getItemSelection();
        if(!itemSelection.equals(selection)) {
            Optional<OWLEntity> previousSelection = extractEntityFromItem(selection);
            selection = itemSelection;
            itemSelection.visitItems(OWLClassItem.getType(), new Item.Handler<OWLClass>() {
                @Override
                public void handleItemObject(Item<OWLClass> object) {
                    selectedClassManager.setSelection(object.getItem());
                }
            });
            itemSelection.visitItems(OWLObjectPropertyItem.getType(), new Item.Handler<OWLObjectProperty>() {
                @Override
                public void handleItemObject(Item<OWLObjectProperty> object) {
                    selectedObjectPropertyManager.setSelection(object.getItem());
                }
            });
            itemSelection.visitItems(OWLDataPropertyItem.getType(), new Item.Handler<OWLDataProperty>() {
                @Override
                public void handleItemObject(Item<OWLDataProperty> object) {
                    selectedDataPropertyManager.setSelection(object.getItem());
                }
            });
            itemSelection.visitItems(OWLAnnotationPropertyItem.getType(), new Item.Handler<OWLAnnotationProperty>() {
                @Override
                public void handleItemObject(Item<OWLAnnotationProperty> object) {
                    selectedAnnotationPropertyManager.setSelection(object.getItem());
                }
            });
            
            itemSelection.visitItems(OWLNamedIndividualItem.getType(), new Item.Handler<OWLNamedIndividual>() {
                @Override
                public void handleItemObject(Item<OWLNamedIndividual> object) {
                    selectedIndividualManager.setSelection(object.getItem());
                }
            });
            
            fireEvent(previousSelection);
        }
    }

    public HandlerRegistration addSelectionChangedHandler(EntitySelectionChangedHandler handler) {
        return eventBus.addHandler(EntitySelectionChangedEvent.getType(), handler);
    }

    public Optional<OWLEntity> getSelection() {
        Place place = placeController.getWhere();
        if(!(place instanceof ProjectViewPlace)) {
            return Optional.absent();
        }
        ProjectViewPlace projectViewPlace = (ProjectViewPlace) place;
        return extractEntityFromItem(projectViewPlace.getItemSelection());
    }

    private static Optional<OWLEntity> extractEntityFromItem(ItemSelection itemSelection) {
        Optional<Item<?>> firstItem = itemSelection.getFirst();
        if(!firstItem.isPresent()) {
            return Optional.absent();
        }
        Object selItem = firstItem.get().getItem();
        if(selItem instanceof OWLEntity) {
            return Optional.of((OWLEntity) selItem);
        }
        else {
            return Optional.absent();
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

    public void setSelection(OWLEntity entity) {
        Place place = placeController.getWhere();
        if(!(place instanceof ProjectViewPlace)) {
            return;
        }
        Item<?> item = entity
                .accept(new OWLEntityVisitorEx<Item<?>>() {
                    @Override
                    public Item<?> visit(OWLClass desc) {
                        selectedClassManager.setSelection(desc);
                        return new OWLClassItem(desc);
                    }

                    @Override
                    public Item<?> visit(OWLDatatype node) {
                        return null;
                    }

                    @Override
                    public Item<?> visit(OWLDataProperty property) {
                        selectedDataPropertyManager.setSelection(property);
                        return new OWLDataPropertyItem(property);
                    }

                    @Override
                    public Item<?> visit(OWLObjectProperty property) {
                        selectedObjectPropertyManager.setSelection(property);
                        return new OWLObjectPropertyItem(property);
                    }

                    @Override
                    public Item<?> visit(OWLNamedIndividual individual) {
                        selectedIndividualManager.setSelection(individual);
                        return new OWLNamedIndividualItem(individual);
                    }

                    @Override
                    public Item<?> visit(OWLAnnotationProperty property) {
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
        Optional<OWLEntity> curSelection = extractEntityFromItem(selection);
        eventBus.fireEvent(new EntitySelectionChangedEvent(previousLastSelection, curSelection));
    }
}
