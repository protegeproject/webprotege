package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/12/2012
 */
public class DefaultPropertyValueEditor extends SimplePanel implements PropertyValueEditor {

    interface DefaultPropertyValueEditorUiBinder extends UiBinder<HTMLPanel, DefaultPropertyValueEditor> {

    }

    @UiField(provided = true)
    final protected DefaultPrimitiveDataEditor propertyEditor;

    @UiField(provided = true)
    final protected DefaultPrimitiveDataEditor valueEditor;

    @UiField(provided = true)
    final protected DefaultLanguageEditor languageEditor;

    private static DefaultPropertyValueEditorUiBinder ourUiBinder = GWT.create(DefaultPropertyValueEditorUiBinder.class);

    public DefaultPropertyValueEditor(DefaultPrimitiveDataEditor propertyEditor, DefaultPrimitiveDataEditor valueEditor) {
        this.propertyEditor = propertyEditor;
        this.valueEditor = valueEditor;
        this.languageEditor = (DefaultLanguageEditor) valueEditor.getLanguageEditor();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        setWidget(rootElement);

    }


    @Override
    public void setValue(PropertyValue object) {
//        propertyEditor.setValue(object.getProperty());

    }

    /**
     * Determines if this object is dirty.
     * @return {@code true} if the object is dirty, otherwise {@code false}.
     */
    @Override
    public boolean isDirty() {
        return false;
    }

    private PropertyValue getObject() {
//        Optional<OWLPrimitiveData> property = propertyEditor.getValue();
//        if(!property.isPresent()) {
//            return
//        }
//        OWLPrimitiveData propertyData = property.get();
//        OWLPrimitiveData propertyValue = valueEditor.getObject();
//
//        if (propertyData instanceof OWLAnnotationPropertyData) {
//            if (propertyValue instanceof OWLLiteralData) {
//                return new PropertyAnnotationValue(((OWLAnnotationPropertyData) propertyData).getEntity(), ((OWLLiteralData) propertyValue).getLiteral());
//            }
//        }
//        else if (propertyData instanceof OWLObjectPropertyData) {
//            if (propertyValue instanceof OWLClassData) {
//                return new PropertyClassValue(((OWLObjectPropertyData) propertyData).getEntity(), ((OWLClassData) propertyValue).getEntity());
//            }
//            else if (propertyValue instanceof OWLNamedIndividualData) {
//                return new PropertyIndividualValue(((OWLObjectPropertyData) propertyData).getEntity(), ((OWLNamedIndividualData) propertyValue).getEntity());
//            }
//        }
//        else if (propertyData instanceof OWLDataPropertyData) {
//            if (propertyValue instanceof OWLDatatypeData) {
//                return new PropertyDatatypeValue(((OWLDataPropertyData) propertyData).getEntity(), ((OWLDatatypeData) propertyValue).getEntity());
//            }
//            else if (propertyValue instanceof OWLLiteralData) {
//                return new PropertyLiteralValue(((OWLDataPropertyData) propertyData).getEntity(), ((OWLLiteralData) propertyValue).getLiteral());
//            }
//        }
        return null;
    }

    @Override
    public Optional<PropertyValue> getValue() {
        if (!propertyEditor.getValue().isPresent()) {
            return Optional.absent();
        }

        if (!valueEditor.getValue().isPresent()) {
            return Optional.absent();
        }

        PropertyValue object = getObject();
        if (object == null) {
            return Optional.absent();
        }
        else {
            return Optional.of(object);
        }
    }

    @Override
    public void clearValue() {
        propertyEditor.clearValue();
        valueEditor.clearValue();
    }

    @Override
    public boolean isWellFormed() {
        return propertyEditor.isWellFormed() && valueEditor.isWellFormed();
    }

    @UiHandler("propertyEditor")
    public void blah2(ValueChangeEvent<Optional<OWLPrimitiveData>> evt) {
        GWT.log("PROPERTY CHANGED: " + evt.getValue());
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("valueEditor")
    public void blah(ValueChangeEvent<Optional<OWLPrimitiveData>> evt) {
        GWT.log("PROPERTY VALUE CHANGED: " + evt.getValue());
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("languageEditor")
    public void blahblah(ValueChangeEvent<Optional<String>> evt) {
        GWT.log("LANG CHANGED: " + evt.getValue());
        ValueChangeEvent.fire(this, getValue());
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<PropertyValue>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}