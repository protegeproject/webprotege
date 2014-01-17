package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.primitive.DefaultPrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorGinjector;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.obo.OBORelationship;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/06/2013
 */
public class OBORelationshipEditorImpl extends Composite implements ValueEditor<OBORelationship> {

    interface OBORelationshipEditorImplUiBinder extends UiBinder<HTMLPanel, OBORelationshipEditorImpl> {

    }

    private static OBORelationshipEditorImplUiBinder ourUiBinder = GWT.create(OBORelationshipEditorImplUiBinder.class);

    @UiField(provided = true)
    protected PrimitiveDataEditor propertyField;

    @UiField(provided = true)
    protected PrimitiveDataEditor valueField;

    public OBORelationshipEditorImpl() {
        propertyField = createPrimitiveDataEditor();
        valueField = createPrimitiveDataEditor();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        propertyField.asWidget().getElement().setAttribute("placeholder", "Enter property name");
        valueField.asWidget().getElement().setAttribute("placeholder", "Enter class name");
    }

    private static PrimitiveDataEditor createPrimitiveDataEditor() {
        return PrimitiveDataEditorGinjector.INSTANCE.getEditor();
    }

    @UiHandler("propertyField")
    protected void propertyFieldValueChanged(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
        fireEvent(new DirtyChangedEvent());
        ValueChangeEvent.fire(this, getValue());
    }


    @UiHandler("valueField")
    protected void valueFieldValueChanged(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
        fireEvent(new DirtyChangedEvent());
        ValueChangeEvent.fire(this, getValue());
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public boolean isWellFormed() {
        return (getPropertyName().isEmpty() && getClassName()) || (!getPropertyName().isEmpty() && !getClassName());
    }

    private boolean getClassName() {
        return valueField.getText().trim().isEmpty();
    }

    private String getPropertyName() {
        return propertyField.getText().trim();
    }

    @Override
    public void setValue(OBORelationship object) {
        propertyField.setValue(object.getRelation());
        valueField.setValue(object.getValue());
    }

    @Override
    public void clearValue() {
        propertyField.clearValue();
        valueField.clearValue();
    }

    @Override
    public Optional<OBORelationship> getValue() {
        if(!propertyField.getValue().isPresent()) {
            return Optional.absent();
        }
        if(!valueField.getValue().isPresent()) {
            return Optional.absent();
        }
        return Optional.of(new OBORelationship((OWLObjectPropertyData) propertyField.getValue().get(), (OWLClassData) valueField.getValue().get()));
    }

    @Override
    public boolean isDirty() {
        return propertyField.isDirty() || valueField.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<OBORelationship>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}

