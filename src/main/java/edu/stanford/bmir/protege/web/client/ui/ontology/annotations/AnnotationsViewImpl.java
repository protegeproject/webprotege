package edu.stanford.bmir.protege.web.client.ui.ontology.annotations;

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
import edu.stanford.bmir.protege.web.client.ui.frame.PropertyValueGridGrammar;
import edu.stanford.bmir.protege.web.client.ui.frame.PropertyValueListEditor;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotation;

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

    public AnnotationsViewImpl(ProjectId projectId) {
        editor = new PropertyValueListEditor(projectId, PropertyValueGridGrammar.getAnnotationsGrammar());
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
    public void setValue(Set<OWLAnnotation> object) {
        List<PropertyAnnotationValue> values = new ArrayList<PropertyAnnotationValue>();
        for(OWLAnnotation annotation : object) {
            values.add(new PropertyAnnotationValue(annotation.getProperty(), annotation.getValue()));
        }
        editor.setValue(new PropertyValueList(values));
    }

    @Override
    public void clearValue() {
        editor.clearValue();
    }

    @Override
    public Optional<Set<OWLAnnotation>> getValue() {
        Optional<PropertyValueList> valueList = editor.getValue();
        if(!valueList.isPresent()) {
            return Optional.absent();
        }
        Set<OWLAnnotation> result = new HashSet<OWLAnnotation>();
        for(PropertyAnnotationValue value : valueList.get().getAnnotationPropertyValues()) {
            result.add(DataFactory.get().getOWLAnnotation(value.getProperty(), value.getValue()));
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
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Optional<Set<OWLAnnotation>>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

}