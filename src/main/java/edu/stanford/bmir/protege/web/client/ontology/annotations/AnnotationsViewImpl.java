package edu.stanford.bmir.protege.web.client.ontology.annotations;

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
import edu.stanford.bmir.protege.web.client.frame.PropertyValueGridGrammar;
import edu.stanford.bmir.protege.web.client.frame.PropertyValueListEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;
import edu.stanford.bmir.protege.web.shared.frame.State;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
public class AnnotationsViewImpl extends Composite implements AnnotationsView {

    interface AnnotationsViewImplUiBinder extends UiBinder<HTMLPanel, AnnotationsViewImpl> {

    }

    private static AnnotationsViewImplUiBinder ourUiBinder = GWT.create(AnnotationsViewImplUiBinder.class);

    @UiField(provided = true)
    protected PropertyValueListEditor editor;

    @Inject
    public AnnotationsViewImpl(PropertyValueListEditor propertyValueListEditor) {
        editor = propertyValueListEditor;
        editor.setGrammar(PropertyValueGridGrammar.getAnnotationsGrammar());
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @UiHandler("editor")
    protected void handleEditorChanged(ValueChangeEvent<Optional<PropertyValueList>> event) {
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("editor")
    protected void handleDirtyChanged(DirtyChangedEvent event) {
        fireEvent(event);
    }

    @Override
    public Widget getWidget() {
        return super.getWidget();
    }

    @Override
    public boolean isWellFormed() {
        return editor.isWellFormed();
    }

    @Override
    public void setValue(Set<PropertyAnnotationValue> object) {
        List<PropertyAnnotationValue> values = new ArrayList<>();
        for(PropertyAnnotationValue annotation : object) {
            values.add(new PropertyAnnotationValue(annotation.getProperty(), annotation.getValue(), State.ASSERTED));
        }
        editor.setValue(new PropertyValueList(values));
    }

    @Override
    public void clearValue() {
        editor.clearValue();
    }

    @Override
    public Optional<Set<PropertyAnnotationValue>> getValue() {
        Optional<PropertyValueList> valueList = editor.getValue();
        if(!valueList.isPresent()) {
            return Optional.absent();
        }
        Set<PropertyAnnotationValue> result = new HashSet<PropertyAnnotationValue>();
        for(PropertyAnnotationValue value : valueList.get().getAnnotationPropertyValues()) {
            result.add(value);
        }
        return Optional.of(result);
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
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Optional<Set<PropertyAnnotationValue>>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

}
