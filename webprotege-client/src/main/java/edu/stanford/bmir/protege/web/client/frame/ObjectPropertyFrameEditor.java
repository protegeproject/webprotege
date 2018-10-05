package edu.stanford.bmir.protege.web.client.frame;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.editor.EditorView;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataListEditor;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyCharacteristic;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public class ObjectPropertyFrameEditor extends FlowPanel implements EntityFrameEditor, ValueEditor<ObjectPropertyFrame>, HasEnabled, EditorView<ObjectPropertyFrame> {

    private static ObjectPropertyFrameEditorUiBinder ourUiBinder = GWT.create(ObjectPropertyFrameEditorUiBinder.class);

    @UiField(provided = true)
    protected final PropertyValueListEditor annotations;

    @UiField(provided = true)
    final PrimitiveDataListEditor domains;

    @UiField(provided = true)
    final PrimitiveDataListEditor ranges;

    @UiField
    protected HasText iriField;

    private boolean dirty = false;

    private EntityDisplay entityDisplay = entityData -> {};

    private Optional<ObjectPropertyFrame> previouslySetValue = Optional.empty();

    private Set<ObjectPropertyCharacteristic> characteristics = Sets.newHashSet();

    private boolean enabled = false;

    @Inject
    public ObjectPropertyFrameEditor(PropertyValueListEditor annotationsEditor,
                                     Provider<PrimitiveDataEditor> primitiveDataEditorProvider,
                                     Messages messages) {
        WebProtegeClientBundle.BUNDLE.style().ensureInjected();
        annotations = annotationsEditor;
        annotations.setGrammar(PropertyValueGridGrammar.getAnnotationsGrammar());
        domains = new PrimitiveDataListEditor(primitiveDataEditorProvider, PrimitiveType.CLASS);
        domains.setPlaceholder(messages.frame_enterAClassName());
        ranges = new PrimitiveDataListEditor(primitiveDataEditorProvider, PrimitiveType.CLASS);
        ranges.setPlaceholder(messages.frame_enterAClassName());
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        add(rootElement);
    }

    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = checkNotNull(entityDisplay);
    }

    private void fireEventIfWellFormed() {
        if (isWellFormed()) {
            dirty = true;
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @UiHandler("annotations")
    protected void handleAnnotationsChanged(ValueChangeEvent<Optional<PropertyValueList>> event) {
        fireEventIfWellFormed();
    }

    @UiHandler("domains")
    protected void handleDomainsChanged(ValueChangeEvent<Optional<List<OWLPrimitiveData>>> event) {
        fireEventIfWellFormed();
    }

    @UiHandler("ranges")
    protected void handleRangesChanged(ValueChangeEvent<Optional<List<OWLPrimitiveData>>> event) {
        fireEventIfWellFormed();
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
     *
     * @param enabled <code>true</code> to enable the widget, <code>false</code>
     *                to disable it
     */
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        annotations.setEnabled(enabled);
        domains.setEnabled(enabled);
        ranges.setEnabled(enabled);
    }

    private void handleChange() {
        dirty = true;
    }

    @Override
    public EntityType<?> getEntityType() {
        return EntityType.OBJECT_PROPERTY;
    }

    @Override
    public void clearValue() {
        dirty = false;
        iriField.setText("");
        annotations.clearValue();
        previouslySetValue = Optional.empty();
        entityDisplay.setDisplayedEntity(Optional.empty());
    }

    @Override
    public Optional<ObjectPropertyFrame> getValue() {
        if (!previouslySetValue.isPresent()) {
            return previouslySetValue;
        }
        ImmutableSet<PropertyAnnotationValue> annotationValues = annotations.getValue().get().getAnnotationPropertyValues();
        final ObjectPropertyFrame previousFrame = previouslySetValue.get();
        OWLObjectPropertyData subject = previousFrame.getSubject();
        ImmutableSet.Builder<OWLClassData> editedDomains = ImmutableSet.builder();
        for (OWLPrimitiveData data : domains.getValue().get()) {
            editedDomains.add((OWLClassData) data);
        }
        ImmutableSet.Builder<OWLClassData> editedRanges = ImmutableSet.builder();
        for (OWLPrimitiveData data : ranges.getValue().get()) {
            editedRanges.add((OWLClassData) data);
        }
        ObjectPropertyFrame frame = ObjectPropertyFrame.get(subject,
                                                            annotationValues,
                                                            editedDomains.build(),
                                                            editedRanges.build(),
                                                            ImmutableSet.of(),
                                                            ImmutableSet.copyOf(characteristics));
        return Optional.of(frame);
    }

    @Override
    public void setValue(ObjectPropertyFrame frame) {
        dirty = false;
        String decodedIri = URL.decode(frame.getSubject().getEntity().getIRI().toString());
        iriField.setText(decodedIri);
        annotations.setValue(new PropertyValueList(frame.getAnnotationPropertyValues()));
        characteristics.clear();
        characteristics.addAll(frame.getCharacteristics());
        domains.setValue(new ArrayList<>(frame.getDomains()));
        ranges.setValue(new ArrayList<>(frame.getRanges()));
        previouslySetValue = Optional.of(frame);
        entityDisplay.setDisplayedEntity(Optional.of(frame.getSubject()));
    }

    @Override
    public boolean isWellFormed() {
        return annotations.isWellFormed() && domains.isWellFormed() && ranges.isWellFormed();
    }

    /**
     * Determines if this object is dirty.
     *
     * @return {@code true} if the object is dirty, otherwise {@code false}.
     */
    @Override
    public boolean isDirty() {
        return dirty || annotations.isDirty() || domains.isDirty() || ranges.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<ObjectPropertyFrame>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    interface ObjectPropertyFrameEditorUiBinder extends UiBinder<HTMLPanel, ObjectPropertyFrameEditor> {

    }
}
