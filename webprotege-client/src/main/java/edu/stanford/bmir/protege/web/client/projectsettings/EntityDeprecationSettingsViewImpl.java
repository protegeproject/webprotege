package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

public class EntityDeprecationSettingsViewImpl extends Composite implements EntityDeprecationSettingsView {

    interface EntityDeprecationSettingsViewImplUiBinder extends UiBinder<HTMLPanel, EntityDeprecationSettingsViewImpl> {

    }

    private static EntityDeprecationSettingsViewImplUiBinder ourUiBinder = GWT.create(
            EntityDeprecationSettingsViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditor classesParentEditor;

    @UiField(provided = true)
    PrimitiveDataEditor objectPropertiesParentEditor;

    @UiField(provided = true)
    PrimitiveDataEditor dataPropertiesParentEditor;

    @UiField(provided = true)
    PrimitiveDataEditor annotationPropertiesParentEditor;

    @UiField(provided = true)
    PrimitiveDataEditor individualsParentEditor;

    @Inject
    public EntityDeprecationSettingsViewImpl(Provider<PrimitiveDataEditor> primitiveDataEditorProvider) {
        classesParentEditor = primitiveDataEditorProvider.get();
        objectPropertiesParentEditor = primitiveDataEditorProvider.get();
        dataPropertiesParentEditor = primitiveDataEditorProvider.get();
        annotationPropertiesParentEditor = primitiveDataEditorProvider.get();
        individualsParentEditor = primitiveDataEditorProvider.get();
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clear() {
        classesParentEditor.clearValue();
        objectPropertiesParentEditor.clearValue();
        dataPropertiesParentEditor.clearValue();
        annotationPropertiesParentEditor.clearValue();
        individualsParentEditor.clearValue();
    }

    @Override
    public void setDeprecatedClassesParent(@Nonnull OWLClassData data) {
        classesParentEditor.setValue(data);
    }

    @Override
    public Optional<OWLClass> getDeprecatedClassesParent() {
        return classesParentEditor.getValue()
                .filter(v -> v instanceof OWLClassData)
                .map(v -> (OWLClassData) v)
                .map(OWLClassData::getEntity);
    }

    @Override
    public void setDeprecatedObjectPropertiesParent(@Nonnull OWLObjectPropertyData data) {
        objectPropertiesParentEditor.setValue(data);
    }

    @Override
    public Optional<OWLObjectProperty> getDeprecatedObjectPropertiesParent() {
        return objectPropertiesParentEditor.getValue()
                                  .filter(v -> v instanceof OWLObjectPropertyData)
                                  .map(v -> (OWLObjectPropertyData) v)
                                  .map(OWLObjectPropertyData::getEntity);
    }

    @Override
    public void setDeprecatedDataPropertiesParent(@Nonnull OWLDataPropertyData data) {
        dataPropertiesParentEditor.setValue(data);
    }

    @Override
    public Optional<OWLDataProperty> getDeprecatedDataPropertiesParent() {
        return dataPropertiesParentEditor.getValue()
                                  .filter(v -> v instanceof OWLDataPropertyData)
                                  .map(v -> (OWLDataPropertyData) v)
                                  .map(OWLDataPropertyData::getEntity);
    }

    @Override
    public void setDeprecatedAnnotationPropertiesParent(@Nonnull OWLAnnotationPropertyData data) {
        annotationPropertiesParentEditor.setValue(data);
    }

    @Override
    public Optional<OWLAnnotationProperty> getDeprecatedAnnotationPropertiesParent() {
        return annotationPropertiesParentEditor.getValue()
                                  .filter(v -> v instanceof OWLAnnotationPropertyData)
                                  .map(v -> (OWLAnnotationPropertyData) v)
                                  .map(OWLAnnotationPropertyData::getEntity);
    }

    @Override
    public void setDeprecatedIndividualsParent(@Nonnull OWLClassData data) {
        individualsParentEditor.setValue(data);
    }

    @Override
    public Optional<OWLClass> getDeprecatedIndividualsParent() {
        return individualsParentEditor.getValue()
                                  .filter(v -> v instanceof OWLClassData)
                                  .map(v -> (OWLClassData) v)
                                  .map(OWLClassData::getEntity);
    }
}