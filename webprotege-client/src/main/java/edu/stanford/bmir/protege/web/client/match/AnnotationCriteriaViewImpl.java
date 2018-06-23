package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.primitive.EntityLinkMode;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.match.AnnotationPresence;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityAnnotationCriteria;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public class AnnotationCriteriaViewImpl extends Composite implements AnnotationCriteriaView {

    interface EntityAnnotationCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, AnnotationCriteriaViewImpl> {

    }

    private static EntityAnnotationCriteriaViewImplUiBinder ourUiBinder = GWT.create(EntityAnnotationCriteriaViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl propertyEditor;

    @UiField
    SimplePanel valueCriteriaContainer;

    @Inject
    public AnnotationCriteriaViewImpl(@Nonnull PrimitiveDataEditorImpl primitiveDataEditor) {
        this.propertyEditor = primitiveDataEditor;
        this.propertyEditor.setAllowedType(PrimitiveType.ANNOTATION_PROPERTY, true);
        initWidget(ourUiBinder.createAndBindUi(this));
        primitiveDataEditor.setEntityLinkMode(EntityLinkMode.DO_NOT_SHOW_LINKS_FOR_ENTITIES);
    }

    @Override
    public void setSelectedProperty(@Nonnull OWLAnnotationPropertyData data) {
        propertyEditor.setValue(data);
    }

    @Override
    public Optional<OWLAnnotationProperty> getSelectedProperty() {
        return propertyEditor.getValue().map(entityData -> (OWLAnnotationProperty) entityData.getObject());
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getValueCriteriaViewContainer() {
        return valueCriteriaContainer;
    }

    @Override
    public void clearProperty() {
        propertyEditor.clearValue();
    }
}