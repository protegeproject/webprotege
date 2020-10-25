package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

public class EntityDeprecationSettingsViewImpl extends Composite implements EntityDeprecationSettingsView {

    interface EntityDeprecationSettingsViewImplUiBinder extends UiBinder<HTMLPanel, EntityDeprecationSettingsViewImpl> {

    }

    private static EntityDeprecationSettingsViewImplUiBinder ourUiBinder = GWT.create(
            EntityDeprecationSettingsViewImplUiBinder.class);

    @UiField
    PrimitiveDataEditor classesParentEditor;

    @UiField
    PrimitiveDataEditor objectPropertiesParentEditor;

    @UiField
    PrimitiveDataEditor dataPropertiesParentEditor;

    @UiField
    PrimitiveDataEditor annotationPropertiesParentEditor;

    @UiField
    PrimitiveDataEditor individualsParentEditor;

    @Inject
    public EntityDeprecationSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clear() {

    }

    @Override
    public void setDeprecatedClassesParent(@Nonnull OWLClassData data) {

    }

    @Override
    public Optional<OWLClass> getDeprecatedClassesParent() {
        return Optional.empty();
    }

    @Override
    public void setDeprecatedObjectPropertiesParent(@Nonnull OWLObjectPropertyData data) {

    }

    @Override
    public Optional<OWLObjectProperty> getDeprecatedObjectPropertiesParent() {
        return Optional.empty();
    }

    @Override
    public void setDeprecatedDataPropertiesParent(@Nonnull OWLDataFactory data) {

    }

    @Override
    public Optional<OWLDataProperty> getDeprecatedDataPropertiesParent() {
        return Optional.empty();
    }

    @Override
    public void setDeprecatedAnnotationPropertiesParent(@Nonnull OWLAnnotationPropertyData data) {

    }

    @Override
    public Optional<OWLAnnotationProperty> getDeprecatedAnnotationPropertiesParent() {
        return Optional.empty();
    }

    @Override
    public void setDeprecatedIndividualsParent(@Nonnull OWLClassData data) {

    }

    @Override
    public Optional<OWLClass> getDeprecatedIndividualsParent() {
        return Optional.empty();
    }
}