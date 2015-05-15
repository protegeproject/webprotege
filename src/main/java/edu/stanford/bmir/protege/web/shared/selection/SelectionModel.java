package edu.stanford.bmir.protege.web.shared.selection;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.entity.*;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/05/15
 */
public class SelectionModel {

    public static final Void VOID = null;

    private final SelectedEntityDataManager<OWLClassData> selectedClassDataManager;

    private final SelectedEntityDataManager<OWLObjectPropertyData> selectedObjectPropertyDataManager;

    private final SelectedEntityDataManager<OWLDataPropertyData> selectedDataPropertyDataManager;

    private final SelectedEntityDataManager<OWLAnnotationPropertyData> selectedAnnotationPropertyDataManager;

    private final SelectedEntityDataManager<OWLDatatypeData> selectedDatatypeDataManager;

    private final SelectedEntityDataManager<OWLNamedIndividualData> selectedIndividualDataManager;


    @Inject
    public SelectionModel(SelectedEntityDataManager<OWLClassData> selectedClassDataManager,
                          SelectedEntityDataManager<OWLObjectPropertyData> selectedObjectPropertyDataManager,
                          SelectedEntityDataManager<OWLDataPropertyData> selectedDataPropertyDataManager,
                          SelectedEntityDataManager<OWLAnnotationPropertyData> selectedAnnotationPropertyDataManager,
                          SelectedEntityDataManager<OWLDatatypeData> selectedDatatypeDataManager,
                          SelectedEntityDataManager<OWLNamedIndividualData> selectedIndividualDataManager) {
        this.selectedClassDataManager = checkNotNull(selectedClassDataManager);
        this.selectedObjectPropertyDataManager = checkNotNull(selectedObjectPropertyDataManager);
        this.selectedDataPropertyDataManager = checkNotNull(selectedDataPropertyDataManager);
        this.selectedAnnotationPropertyDataManager = checkNotNull(selectedAnnotationPropertyDataManager);
        this.selectedDatatypeDataManager = checkNotNull(selectedDatatypeDataManager);
        this.selectedIndividualDataManager = checkNotNull(selectedIndividualDataManager);
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
        entityData.accept(new OWLEntityDataVisitorEx<Void>() {
            @Override
            public Void visit(OWLClassData data) {
                selectedClassDataManager.setSelection(data);
                return VOID;
            }

            @Override
            public Void visit(OWLObjectPropertyData data) {
                selectedObjectPropertyDataManager.setSelection(data);
                return VOID;
            }

            @Override
            public Void visit(OWLDataPropertyData data) {
                selectedDataPropertyDataManager.setSelection(data);
                return VOID;
            }

            @Override
            public Void visit(OWLAnnotationPropertyData data) {
                selectedAnnotationPropertyDataManager.setSelection(data);
                return VOID;
            }

            @Override
            public Void visit(OWLNamedIndividualData data) {
                selectedIndividualDataManager.setSelection(data);
                return VOID;
            }

            @Override
            public Void visit(OWLDatatypeData data) {
                selectedDatatypeDataManager.setSelection(data);
                return VOID;
            }
        });
    }
}
