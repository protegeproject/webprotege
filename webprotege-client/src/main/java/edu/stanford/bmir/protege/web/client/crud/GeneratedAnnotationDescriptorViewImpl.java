package edu.stanford.bmir.protege.web.client.crud;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class GeneratedAnnotationDescriptorViewImpl extends Composite implements GeneratedAnnotationDescriptorView {

    private SelectedTypeNameChangedHandler handler = typeName -> {};

    interface GeneratedAnnotationDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, GeneratedAnnotationDescriptorViewImpl> {

    }

    private static GeneratedAnnotationDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            GeneratedAnnotationDescriptorViewImplUiBinder.class);


    @UiField(provided = true)
    PrimitiveDataEditor propertyEditor;

    @UiField
    ListBox typeNameCombo;

    @UiField
    SimplePanel valuePresenterContainer;

    @UiField
    SimplePanel criteriaContainer;

    @Inject
    public GeneratedAnnotationDescriptorViewImpl(@Nonnull PrimitiveDataEditor primitiveDataEditor) {
        propertyEditor = primitiveDataEditor;
        initWidget(ourUiBinder.createAndBindUi(this));
        typeNameCombo.addChangeHandler(event -> handler.handleTypeNameChanged(typeNameCombo.getSelectedValue()));
    }

    @Override
    public void setSelectedTypeNameChangedHandler(@Nonnull SelectedTypeNameChangedHandler handler) {
        this.handler = checkNotNull(handler);
    }

    @Override
    public void setTypeNames(ImmutableList<String> typeNames) {
        typeNameCombo.clear();
        typeNames.forEach(typeName -> typeNameCombo.addItem(typeName, typeName));
    }

    @Override
    public void setSelectedTypeName(@Nonnull String choiceName) {
        typeNameCombo.clear();
        for(int i = 0; i < typeNameCombo.getItemCount(); i++) {
            String value = typeNameCombo.getValue(i);
            if(value.equals(choiceName)) {
                typeNameCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public String getSelectedTypeName() {
        return typeNameCombo.getSelectedValue();
    }

    @Override
    public void setProperty(@Nonnull OWLAnnotationPropertyData property) {
        propertyEditor.setValue(property);
    }

    @Nonnull
    @Override
    public Optional<OWLAnnotationPropertyData> getProperty() {
        return propertyEditor.getValue()
                .filter(value -> value instanceof OWLAnnotationPropertyData)
                .map(value -> (OWLAnnotationPropertyData) value);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getValueDescriptorContainer() {
        return valuePresenterContainer;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getActivatedByCriteriaContainer() {
        return criteriaContainer;
    }
}