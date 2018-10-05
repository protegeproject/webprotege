package edu.stanford.bmir.protege.web.client.frame;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.editor.EditorView;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.common.EventStrategy;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataListEditor;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/12/2012
 */
public class ClassFrameEditor extends SimplePanel implements ValueEditor<ClassFrame>, ClassFrameEditorPresenter, EditorView<ClassFrame> {

    @UiField
    protected HasText iriField;

    @UiField(provided = true)
    protected final PropertyValueListEditor annotations;

    @UiField(provided = true)
    protected final PropertyValueListEditor properties;

    @UiField(provided = true)
    protected final PrimitiveDataListEditor classes;

    private OWLClassData currentSubject;

    private boolean enabled = true;

    private boolean editable = true;

    private boolean dirty;

    private EntityDisplay entityDisplay = entityData -> {};

    interface ClassFrameEditor2UiBinder extends UiBinder<HTMLPanel, ClassFrameEditor> {

    }

    private static ClassFrameEditor2UiBinder ourUiBinder = GWT.create(ClassFrameEditor2UiBinder.class);

    @Inject
    public ClassFrameEditor(Provider<PrimitiveDataEditor> primitiveDataEditorProvider,
                            PropertyValueListEditor annotations,
                            PropertyValueListEditor properties) {
        this.annotations = annotations;
        this.annotations.setGrammar(PropertyValueGridGrammar.getAnnotationsGrammar());
        this.classes = new PrimitiveDataListEditor(primitiveDataEditorProvider, PrimitiveType.CLASS);
        this.properties = properties;
        this.properties.setGrammar(PropertyValueGridGrammar.getClassGrammar());

        WebProtegeClientBundle.BUNDLE.style().ensureInjected();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        setWidget(rootElement);
    }

    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = checkNotNull(entityDisplay);
    }

    public void setValue(final ClassFrame frame) {
        setDirty(false, EventStrategy.DO_NOT_FIRE_EVENTS);
        currentSubject = frame.getSubject();
        entityDisplay.setDisplayedEntity(Optional.of(currentSubject));
        String decodedIri = URL.decode(frame.getSubject().getEntity().getIRI().toString());
        iriField.setText(decodedIri);
        ArrayList<PropertyAnnotationValue> annotationPropertyValues = new ArrayList<>(frame.getAnnotationPropertyValues());
        annotations.setValue(new PropertyValueList(annotationPropertyValues));
        ArrayList<PropertyValue> logicalPropertyValues = new ArrayList<>(frame.getLogicalPropertyValues());
        properties.setValue(new PropertyValueList(logicalPropertyValues));
        classes.setValue(new ArrayList<>(frame.getClassEntries()));
    }




    /**
     * Returns true if the widget is enabled, false if not.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether this widget is enabled.
     * @param enabled <code>true</code> to enable the widget, <code>false</code>
     * to disable it
     */
    @Override
    public void setEnabled(boolean enabled) {
        setEnabledInternal(enabled);
    }

    private void setEnabledInternal(boolean enabled) {
        this.enabled = enabled;
        annotations.setEnabled(enabled);
        properties.setEnabled(enabled);
        classes.setEnabled(enabled);
    }

    /**
     * Determines if the object implementing this interface is editable.
     * @return {@code true} if the object is editable, otherwise {@code false}.
     */
    @Override
    public boolean isEditable() {
        return editable;
    }

    /**
     * Sets the editable state of the object implementing this interface.
     * @param editable If {@code true} then the state is set to editable, if {@code false} then the state is set to
     * not editable.
     */
    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public boolean isDirty() {
        return annotations.isDirty() || classes.isDirty() || properties.isDirty() || dirty;
    }



    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    private boolean isQuoted(String s) {
        return s.startsWith("'") && s.endsWith("'");
    }

    private String removeQuotes(String s) {
        if (s.startsWith("'") && s.endsWith("'")) {
            return s.substring(1, s.length() - 1);
        }
        else {
            return s;
        }
    }


    @Override
    public Optional<ClassFrame> getValue() {
        if(currentSubject == null) {
            return Optional.empty();
        }
        else {
            ImmutableSet.Builder<OWLClassData> classesData = ImmutableSet.builder();
            classes.getValue().ifPresent(clses -> clses.forEach(cls -> classesData.add((OWLClassData) cls)));
            ImmutableSet.Builder<PropertyValue> propertyValues = ImmutableSet.builder();
            annotations.getValue().ifPresent(annos -> propertyValues.addAll(annos.getPropertyValues()));
            properties.getValue().ifPresent(props -> propertyValues.addAll(props.getPropertyValues()));
            ClassFrame clsFrame = ClassFrame.get(currentSubject, classesData.build(), propertyValues.build());
            return Optional.of(clsFrame);
        }
    }

    @Override
    public boolean isWellFormed() {
        return annotations.isWellFormed() && properties.isWellFormed();
    }

    @Override
    public void clearValue() {
        annotations.clearValue();
        properties.clearValue();
        classes.clearValue();
        entityDisplay.setDisplayedEntity(Optional.empty());
    }

    @UiHandler("annotations")
    protected void handleAnnotationsValueChanged(ValueChangeEvent<Optional<PropertyValueList>> evt) {
        if(isWellFormed()) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @UiHandler("annotations")
    protected void handleAnnotationsDirtyChanged(DirtyChangedEvent evt) {
        setDirty(true, EventStrategy.FIRE_EVENTS);
        if(isWellFormed()) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @UiHandler("properties")
    protected void handlePropertiesValueChange(ValueChangeEvent<Optional<PropertyValueList>> evt) {
        if(isWellFormed()) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @UiHandler("classes")
    protected void handleClassesValueChange(ValueChangeEvent<Optional<List<OWLPrimitiveData>>> evt) {
        if(isWellFormed()) {
            this.dirty = true;
            ValueChangeEvent.fire(this, getValue());
        }
    }



    @UiHandler("properties")
    protected void handlePropertiesDirtyChanged(DirtyChangedEvent evt) {
        setDirty(true, EventStrategy.FIRE_EVENTS);
    }

    private void setDirty(boolean dirty, EventStrategy eventStrategy) {
        this.dirty = dirty;
        if(eventStrategy == EventStrategy.FIRE_EVENTS) {
            fireEvent(new DirtyChangedEvent());
            if (isWellFormed()) {
                ValueChangeEvent.fire(this, getValue());
            }
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<ClassFrame>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public Widget getWidget() {
        return this;
    }
}
