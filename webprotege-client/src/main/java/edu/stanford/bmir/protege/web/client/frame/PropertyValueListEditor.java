package edu.stanford.bmir.protege.web.client.frame;

import com.google.common.collect.Lists;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueDescriptor;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 27/02/2014
 */
public class PropertyValueListEditor extends Composite implements ValueEditor<PropertyValueList>, HasEnabled {

    private ValueListEditor<PropertyValueDescriptor> editor;

    private PropertyValueGridGrammar grammar = PropertyValueGridGrammar.getClassGrammar();

    @Inject
    public PropertyValueListEditor(final Provider<PrimitiveDataEditorImpl> primitiveDataEditorProvider) {
        this.editor = new ValueListFlexEditorImpl<>(
                () -> {
                    PropertyValueDescriptorEditorImpl propertyValueEditor = new PropertyValueDescriptorEditorImpl(primitiveDataEditorProvider.get(), primitiveDataEditorProvider.get());
                    PropertyValueDescriptorEditorPresenter presenter = new PropertyValueDescriptorEditorPresenter(propertyValueEditor);
                    presenter.setGrammar(grammar);
                    return presenter;
                }
        );
        initWidget(editor.asWidget());
        editor.addValueChangeHandler(event -> ValueChangeEvent.fire(PropertyValueListEditor.this, getValue()));
    }


    public void setGrammar(PropertyValueGridGrammar grammar) {
        this.grammar = grammar;
    }

    @Override
    public void setValue(final PropertyValueList propertyValueList) {
        fillUp(propertyValueList);
    }

    private void fillUp(PropertyValueList propertyValueList) {
        List<PropertyValueDescriptor> vals = Lists.newArrayList();
        for (PropertyValue propertyValue : propertyValueList.getPropertyValues()) {
//            if (propertyValue.getState() == State.ASSERTED) {
                Optional<PropertyValueDescriptor> val =    addRelationship(propertyValue);
                if (val.isPresent()) {
                    vals.add(val.get());
                }
//            }
        }
        editor.setValue(vals);
    }



    private Optional<PropertyValueDescriptor> addRelationship(final PropertyValue propertyValue) {
        return Optional.of(
                new PropertyValueDescriptor(
                        propertyValue.getProperty(),
                        propertyValue.getValue(),
                        propertyValue.getState(),
                        propertyValue.isValueMostSpecific(),
                        Collections.emptySet()
                )
        );

//        final Optional<OWLEntityData> propRendering = provider.getEntityData(propertyValue.getProperty());
//        OWLPrimitiveData valueRendering = null;
//        if (propertyValue.getValue() instanceof OWLEntity) {
//            final Optional<OWLEntityData> propertyData = provider.getEntityData((OWLEntity) propertyValue.getValue());
//            valueRendering = propertyData.get();
//        }
//        else if (propertyValue.getValue() instanceof OWLLiteral) {
//            valueRendering = OWLLiteralData.get((OWLLiteral) propertyValue.getValue());
//        }
//        else if (propertyValue.getValue() instanceof IRI) {
//            valueRendering = IRIData.get((IRI) propertyValue.getValue());
//        }
//        if(propRendering.isPresent()) {
//            return Optional.of(new PropertyValueDescriptor((OWLPropertyData) propRendering.get(), valueRendering, propertyValue.getState(), propertyValue.isValueMostSpecific(), Collections.emptySet()));
//        }
//        else {
//            return Optional.absent();
//        }
    }

    @Override
    public void clearValue() {
        editor.clearValue();
    }

    @Override
    public Optional<PropertyValueList> getValue() {
        Optional<List<PropertyValueDescriptor>> value =  editor.getValue();
        if(!value.isPresent()) {
            return Optional.empty();
        }
        List<PropertyValue> propertyValues = Lists.newArrayList();
        for(PropertyValueDescriptor val : value.get()) {
            java.util.Optional<PropertyValue> propertyValue = val.toPropertyValue();
            if (propertyValue.isPresent()) {
                propertyValues.add(propertyValue.get());
            }
        }
        return Optional.of(new PropertyValueList(propertyValues));
    }

    @Override
    public boolean isEnabled() {
        return editor.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        editor.setEnabled(enabled);
    }

    @Override
    public boolean isDirty() {
        return editor.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return editor.addDirtyChangedHandler(handler);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<PropertyValueList>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return editor.isWellFormed();
    }

}
