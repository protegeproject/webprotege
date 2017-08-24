package edu.stanford.bmir.protege.web.client.frame;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.TextBox;
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
import edu.stanford.bmir.protege.web.shared.frame.*;
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
public class ObjectPropertyFrameEditor extends FlowPanel implements EntityFrameEditor, ValueEditor<LabelledFrame<ObjectPropertyFrame>>, HasEnabled, EditorView<LabelledFrame<ObjectPropertyFrame>> {

    @UiField
    protected TextBox iriField;

    @UiField(provided = true)
    protected final PropertyValueListEditor annotations;

    @UiField(provided = true)
    final PrimitiveDataListEditor domains;

    @UiField(provided = true)
    final PrimitiveDataListEditor ranges;

    private boolean dirty = false;

    private EntityDisplay entityDisplay = entityData -> {};


    private Optional<LabelledFrame<ObjectPropertyFrame>> previouslySetValue = Optional.empty();

    interface ObjectPropertyFrameEditorUiBinder extends UiBinder<HTMLPanel, ObjectPropertyFrameEditor> {

    }

    private static ObjectPropertyFrameEditorUiBinder ourUiBinder = GWT.create(ObjectPropertyFrameEditorUiBinder.class);

    private Set<ObjectPropertyCharacteristic> characteristics = Sets.newHashSet();

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

    private boolean enabled = false;

    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = checkNotNull(entityDisplay);
    }

    private void fireEventIfWellFormed() {
        if(isWellFormed()) {
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
     * @param enabled <code>true</code> to enable the widget, <code>false</code>
     * to disable it
     */
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        iriField.setEnabled(false);
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
    public void setValue(LabelledFrame<ObjectPropertyFrame> object) {
        dirty = false;
        final ObjectPropertyFrame frame = object.getFrame();
        iriField.setValue(frame.getSubject().getEntity().getIRI().toString());
        annotations.setValue(new PropertyValueList(Collections.<PropertyValue>unmodifiableSet(frame.getAnnotationPropertyValues())));
        characteristics.clear();
        characteristics.addAll(object.getFrame().getCharacteristics());
        domains.setValue(new ArrayList<>(object.getFrame().getDomains()));
        ranges.setValue(new ArrayList<>(object.getFrame().getRanges()));
        previouslySetValue = Optional.of(object);
        entityDisplay.setDisplayedEntity(java.util.Optional.of(object.getFrame().getSubject()));
    }

    @Override
    public void clearValue() {
        dirty = false;
        iriField.setValue("");
        annotations.clearValue();
        previouslySetValue = Optional.empty();
        entityDisplay.setDisplayedEntity(java.util.Optional.empty());
    }

    @Override
    public Optional<LabelledFrame<ObjectPropertyFrame>> getValue() {
        if(!previouslySetValue.isPresent()) {
            return previouslySetValue;
        }
        Set<PropertyAnnotationValue> annotationValueSet = new HashSet<PropertyAnnotationValue>();
        annotationValueSet.addAll(annotations.getValue().get().getAnnotationPropertyValues());
        final ObjectPropertyFrame previousFrame = previouslySetValue.get().getFrame();
        OWLObjectPropertyData subject = previousFrame.getSubject();
        List<OWLClassData> editedDomains = Lists.newArrayList();
        for(OWLPrimitiveData data : domains.getValue().get()) {
            editedDomains.add((OWLClassData) data);
        }
        List<OWLClassData> editedRanges = Lists.newArrayList();
        for(OWLPrimitiveData data : ranges.getValue().get()) {
            editedRanges.add((OWLClassData) data);
        }
        ObjectPropertyFrame frame = new ObjectPropertyFrame(subject, annotationValueSet,
                new HashSet<>(editedDomains),
                new HashSet<>(editedRanges),
                Collections.emptySet(),
                characteristics);
        return Optional.of(new LabelledFrame<>(previouslySetValue.get().getDisplayName(), frame));
    }


    @Override
    public boolean isWellFormed() {
        return annotations.isWellFormed() && domains.isWellFormed() && ranges.isWellFormed();
    }

    /**
     * Determines if this object is dirty.
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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<LabelledFrame<ObjectPropertyFrame>>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}
