package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.frame.PropertyValueGridGrammar;
import edu.stanford.bmir.protege.web.client.frame.PropertyValueListEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-21
 */
public class SubFormFieldDescriptorViewImpl extends Composite implements SubFormFieldDescriptorView {

    interface SubFormFieldDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, SubFormFieldDescriptorViewImpl> {

    }

    private static SubFormFieldDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            SubFormFieldDescriptorViewImplUiBinder.class);

    @UiField
    SimplePanel subFormContainer;

    @UiField
    static Counter counter = new Counter();

    @UiField(provided = true)
    ValueListEditor<OWLPrimitiveData> parentsList;

    @UiField(provided = true)
    PropertyValueListEditor annotationsList;

    @UiField
    RadioButton classRadio;

    @UiField
    RadioButton individualRadio;

    @Inject
    public SubFormFieldDescriptorViewImpl(Provider<PrimitiveDataEditor> primitiveDataEditorProvider,
                                          PropertyValueListEditor annotationsList) {
        counter.increment();
        this.annotationsList = annotationsList;
        this.annotationsList.setEnabled(true);
        parentsList = new ValueListFlexEditorImpl<>(() -> {
            PrimitiveDataEditor editor = primitiveDataEditorProvider.get();
            editor.setEnabled(true);
            editor.setClassesAllowed(true);
            return editor;
        });
        parentsList.setEnabled(true);
        initWidget(ourUiBinder.createAndBindUi(this));
        this.annotationsList.setGrammar(PropertyValueGridGrammar.getAnnotationsGrammar());
        this.annotationsList.setValue(new PropertyValueList(Collections.emptyList()));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getSubFormContainer() {
        return subFormContainer;
    }

    @Nonnull
    @Override
    public EntityType<?> getEntityType() {
        if(individualRadio.getValue()) {
            return EntityType.NAMED_INDIVIDUAL;
        }
        else {
            return EntityType.CLASS;
        }
    }

    @Override
    public void setEntityType(@Nonnull EntityType<?> entityType) {
        if(entityType.equals(EntityType.NAMED_INDIVIDUAL)) {
            individualRadio.setValue(true);
        }
        else {
            classRadio.setValue(true);
        }
    }

    @Override
    public void setParents(@Nonnull List<OWLPrimitiveData> parents) {
        parentsList.setValue(parents);
    }

    @Override
    public void setAnnotationPropertyValues(@Nonnull List<PropertyAnnotationValue> propertyAnnotationValues) {
        annotationsList.setValue(new PropertyValueList(propertyAnnotationValues));
    }

    @Override
    public List<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return annotationsList.getValue().map(PropertyValueList::getAnnotationPropertyValues)
                              .map(ImmutableList::copyOf)
                              .orElse(ImmutableList.of());
    }

    @Override
    public void clear() {
        parentsList.clearValue();
        annotationsList.clearValue();
    }
}
