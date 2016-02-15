package edu.stanford.bmir.protege.web.shared.selection;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.place.*;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityVisitorExAdapter;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/05/15
 */
public class SelectionModel {

    public static final Void VOID = null;

    private final EventBus eventBus;

    private final SelectedEntityDataManager<OWLClassData> selectedClassDataManager;

    private final SelectedEntityDataManager<OWLObjectPropertyData> selectedObjectPropertyDataManager;

    private final SelectedEntityDataManager<OWLDataPropertyData> selectedDataPropertyDataManager;

    private final SelectedEntityDataManager<OWLAnnotationPropertyData> selectedAnnotationPropertyDataManager;

    private final SelectedEntityDataManager<OWLDatatypeData> selectedDatatypeDataManager;

    private final SelectedEntityDataManager<OWLNamedIndividualData> selectedIndividualDataManager;

    private final PlaceController placeController;

//    private Optional<OWLEntityData> selection = Optional.absent();

    private ItemSelection selection = ItemSelection.empty();


    @Inject
    public SelectionModel(EventBus eventBus,
                          PlaceController placeController,
                          SelectedEntityDataManager<OWLClassData> selectedClassDataManager,
                          SelectedEntityDataManager<OWLObjectPropertyData> selectedObjectPropertyDataManager,
                          SelectedEntityDataManager<OWLDataPropertyData> selectedDataPropertyDataManager,
                          SelectedEntityDataManager<OWLAnnotationPropertyData> selectedAnnotationPropertyDataManager,
                          SelectedEntityDataManager<OWLDatatypeData> selectedDatatypeDataManager,
                          SelectedEntityDataManager<OWLNamedIndividualData> selectedIndividualDataManager) {
        this.eventBus = eventBus;
        this.placeController = placeController;
        this.selectedClassDataManager = checkNotNull(selectedClassDataManager);
        this.selectedObjectPropertyDataManager = checkNotNull(selectedObjectPropertyDataManager);
        this.selectedDataPropertyDataManager = checkNotNull(selectedDataPropertyDataManager);
        this.selectedAnnotationPropertyDataManager = checkNotNull(selectedAnnotationPropertyDataManager);
        this.selectedDatatypeDataManager = checkNotNull(selectedDatatypeDataManager);
        this.selectedIndividualDataManager = checkNotNull(selectedIndividualDataManager);
        eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
            @Override
            public void onPlaceChange(PlaceChangeEvent event) {
                Place newPlace = event.getNewPlace();
                if(newPlace instanceof ProjectViewPlace) {
                    ProjectViewPlace projectViewPlace = (ProjectViewPlace) newPlace;
                    updateSelectionFromPlace(projectViewPlace);
//                    Optional<OWLEntity> entity = projectViewPlace.getEntity();
//                    if(entity.isPresent()) {
//                        GWT.log("[SelectionModel] Detected place change.  Synchronising selection model with place: " + newPlace);
//                        setSelection(DataFactory.getOWLEntityData(entity.get(), "NextSelection"));
//                    }
                }
            }
        });
    }

    private void updateSelectionFromPlace(ProjectViewPlace place) {
        ItemSelection itemSelection = place.getItemSelection();
        if(!Optional.of(itemSelection).equals(selection)) {
            Optional<OWLEntityData> previousSelection;
            previousSelection = extractEntityDataFromItem(selection);
            selection = itemSelection;
            fireEvent(previousSelection);
        }
    }


    public HandlerRegistration addSelectionChangedHandler(EntityDataSelectionChangedHandler handler) {
        return eventBus.addHandler(EntityDataSelectionChangedEvent.getType(), handler);
    }

    public Optional<OWLEntityData> getSelection() {
        Place place = placeController.getWhere();
        if(!(place instanceof ProjectViewPlace)) {
            return Optional.absent();
        }
        ProjectViewPlace projectViewPlace = (ProjectViewPlace) place;
        return extractEntityDataFromItem(projectViewPlace.getItemSelection());
    }

    private static Optional<OWLEntityData> extractEntityDataFromItem(ItemSelection itemSelection) {
        Optional<Item<?>> firstItem = itemSelection.getFirst();
        if(!firstItem.isPresent()) {
            return Optional.absent();
        }
        Object selItem = firstItem.get().getItem();
        if(selItem instanceof OWLEntity) {
            OWLEntityData classData = DataFactory.getOWLEntityData((OWLEntity) selItem, "The selection");
            return Optional.of(classData);
        }
        else {
            return Optional.absent();
        }
    }

    public Optional<OWLClassData> getLastSelectedClassData() {
        return selectedClassDataManager.getLastSelection();
    }

    public Optional<OWLObjectPropertyData> getLastSelectedObjectPropertyData() {
        return selectedObjectPropertyDataManager.getLastSelection();
    }

    public Optional<OWLDataPropertyData> getLastSelectedDataPropertyData() {
        return selectedDataPropertyDataManager.getLastSelection();
    }

    public Optional<OWLAnnotationPropertyData> getLastSelectedAnnotationPropertyData() {
        return selectedAnnotationPropertyDataManager.getLastSelection();
    }

    public Optional<OWLDatatypeData> getLastSelectedDatatypeData() {
        return selectedDatatypeDataManager.getLastSelection();
    }

    public Optional<OWLNamedIndividualData> getLastSelectedNamedIndividualData() {
        return selectedIndividualDataManager.getLastSelection();
    }

    public void setSelection(OWLEntityData entityData) {
        Place place = placeController.getWhere();
        if(!(place instanceof ProjectViewPlace)) {
            return;
        }
        Item<?> item = entityData.getEntity()
                .accept(new OWLEntityVisitorExAdapter<Item<?>>() {
                    @Override
                    public Item<?> visit(OWLClass desc) {
                        return new OWLClassItem(desc);
                    }

                    @Override
                    public Item<?> visit(OWLDatatype node) {
                        return null;
                    }

                    @Override
                    public Item<?> visit(OWLDataProperty property) {
                        return new OWLDataPropertyItem(property);
                    }

                    @Override
                    public Item<?> visit(OWLObjectProperty property) {
                        return new OWLObjectPropertyItem(property);
                    }

                    @Override
                    public Item<?> visit(OWLNamedIndividual individual) {
                        return new OWLNamedIndividualItem(individual);
                    }

                    @Override
                    public Item<?> visit(OWLAnnotationProperty property) {
                        return new OWLAnnotationPropertyItem(property);
                    }
                });
        ProjectViewPlace projectViewPlace = (ProjectViewPlace) place;
        ProjectViewPlace nextPlace = projectViewPlace.builder()
                .clearSelection()
                .withSelectedItem(item)
                .build();
        placeController.goTo(nextPlace);

//
//
//        GWT.log("[SelectionModel] Request to set selection in selection model to: " + entityData);
//        Optional<OWLEntityData> previousSelection = selection;
//        selection = Optional.<OWLEntityData>of(entityData);
//        entityData.accept(new OWLEntityDataVisitorEx<Void>() {
//            @Override
//            public Void visit(OWLClassData data) {
//                selectedClassDataManager.setSelection(data);
//                return VOID;
//            }
//
//            @Override
//            public Void visit(OWLObjectPropertyData data) {
//                selectedObjectPropertyDataManager.setSelection(data);
//                return VOID;
//            }
//
//            @Override
//            public Void visit(OWLDataPropertyData data) {
//                selectedDataPropertyDataManager.setSelection(data);
//                return VOID;
//            }
//
//            @Override
//            public Void visit(OWLAnnotationPropertyData data) {
//                selectedAnnotationPropertyDataManager.setSelection(data);
//                return VOID;
//            }
//
//            @Override
//            public Void visit(OWLNamedIndividualData data) {
//                selectedIndividualDataManager.setSelection(data);
//                return VOID;
//            }
//
//            @Override
//            public Void visit(OWLDatatypeData data) {
//                selectedDatatypeDataManager.setSelection(data);
//                return VOID;
//            }
//        });
//        if (!previousSelection.equals(selection)) {
//            Place place = placeManager.getCurrentPlace();
//            GWT.log("[SelectionModel] Current place: " + place);
//            if(place instanceof ProjectViewPlace) {
//                ProjectViewPlace projectViewPlace = (ProjectViewPlace) place;
////                ProjectViewPlace nextPlace = ProjectViewPlace.builder(projectViewPlace.getProjectId(), projectViewPlace.getId()), Optional.<OWLEntity>of(selection.get().getEntity()));
////                GWT.log("[SelectionModel] Next place: " + nextPlace);
////                placeManager.setCurrentPlace(nextPlace);
//            }
//            fireEvent(previousSelection);
//
//        }
    }


    private void fireEvent(Optional<OWLEntityData> previousLastSelection) {
        Optional<OWLEntityData> curSelection = extractEntityDataFromItem(selection);
        eventBus.fireEvent(new EntityDataSelectionChangedEvent(previousLastSelection, curSelection));
    }
}
