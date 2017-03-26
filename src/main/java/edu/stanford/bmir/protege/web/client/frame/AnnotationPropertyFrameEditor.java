package edu.stanford.bmir.protege.web.client.frame;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.editor.EditorView;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataListEditor;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/04/2013
 */
public class AnnotationPropertyFrameEditor extends Composite implements EditorView<LabelledFrame<AnnotationPropertyFrame>>, HasEnabled {

    private Optional<LabelledFrame<AnnotationPropertyFrame>> lastFrame = Optional.absent();

    interface AnnotationPropertyFrameEditorUiBinder extends UiBinder<HTMLPanel, AnnotationPropertyFrameEditor> {

    }

    private static AnnotationPropertyFrameEditorUiBinder ourUiBinder = GWT.create(AnnotationPropertyFrameEditorUiBinder.class);


    @UiField
    protected TextBox iriField;

    @UiField(provided = true)
    protected final PropertyValueListEditor annotations;

    @UiField(provided = true)
    protected final PrimitiveDataListEditor domains;

    @UiField(provided = true)
    protected final PrimitiveDataListEditor ranges;

    private boolean enabled;

    private boolean dirty = false;

    private EntityDisplay entityDisplay = entityData -> {};


    @Inject
    public AnnotationPropertyFrameEditor(PropertyValueListEditor annotationsEditor,
                                         Provider<PrimitiveDataEditor> primitiveDataEditorProvider) {
        annotations = annotationsEditor;
        annotations.setGrammar(PropertyValueGridGrammar.getAnnotationsGrammar());
        domains = new PrimitiveDataListEditor(primitiveDataEditorProvider,
                                              PrimitiveType.CLASS,
                                              PrimitiveType.OBJECT_PROPERTY,
                                              PrimitiveType.DATA_PROPERTY,
                                              PrimitiveType.ANNOTATION_PROPERTY,
                                              PrimitiveType.NAMED_INDIVIDUAL,
                                              PrimitiveType.DATA_TYPE);
        domains.setPlaceholder("Enter an entity name");
        ranges = new PrimitiveDataListEditor(primitiveDataEditorProvider,
                                             PrimitiveType.DATA_TYPE,
                                             PrimitiveType.CLASS,
                                             PrimitiveType.OBJECT_PROPERTY,
                                             PrimitiveType.DATA_PROPERTY,
                                             PrimitiveType.ANNOTATION_PROPERTY,
                                             PrimitiveType.NAMED_INDIVIDUAL);
        ranges.setPlaceholder("Enter an entity name");
        WebProtegeClientBundle.BUNDLE.style().ensureInjected();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        iriField.setEnabled(false);
        setEnabled(false);
    }

    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = checkNotNull(entityDisplay);
    }

    @UiHandler("annotations")
    protected void handleAnnotationsChanged(ValueChangeEvent<Optional<PropertyValueList>> event) {
        fireValueChangedIfWellFormed();
    }

    @UiHandler("domains")
    protected void handleDomainsChanged(ValueChangeEvent<Optional<List<OWLPrimitiveData>>> event) {
        fireValueChangedIfWellFormed();
    }


    @UiHandler("ranges")
    protected void handleRangesChanged(ValueChangeEvent<Optional<List<OWLPrimitiveData>>> event) {
        fireValueChangedIfWellFormed();
    }

    private void fireValueChangedIfWellFormed() {
        if(isWellFormed()) {
            dirty = true;
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public void setValue(LabelledFrame<AnnotationPropertyFrame> object) {
        dirty = false;
        lastFrame = Optional.of(object);
        entityDisplay.setDisplayedEntity(java.util.Optional.of(object.getFrame().getSubject()));
        final AnnotationPropertyFrame frame = object.getFrame();
        iriField.setText(frame.getSubject().getEntity().getIRI().toString());
        annotations.setValue(frame.getPropertyValueList());
        domains.setValue(new ArrayList<>(frame.getDomains()));
        ranges.setValue(new ArrayList<>(frame.getRanges()));
    }

    @Override
    public void clearValue() {
        iriField.setText("");
        annotations.clearValue();
        domains.clearValue();
        ranges.clearValue();
        entityDisplay.setDisplayedEntity(java.util.Optional.empty());
    }

    @Override
    public Optional<LabelledFrame<AnnotationPropertyFrame>> getValue() {
        if(!lastFrame.isPresent()) {
            return Optional.absent();
        }
        final Set<OWLEntityData> domainsClasses = Sets.newHashSet();
        for(OWLPrimitiveData data : domains.getValue().get()) {
            domainsClasses.add((OWLClassData) data);
        }
        final Set<OWLEntityData> rangeTypes = Sets.newHashSet();
        for(OWLPrimitiveData data : ranges.getValue().get()) {
            rangeTypes.add((OWLEntityData) data);
        }
        AnnotationPropertyFrame frame = new AnnotationPropertyFrame(lastFrame.get().getFrame().getSubject(),
                                                                    annotations.getValue().get().getAnnotationPropertyValues(),
                                                                    domainsClasses,
                                                                    rangeTypes);
        return Optional.of(new LabelledFrame<>(lastFrame.get().getDisplayName(), frame));
    }

    @Override
    public boolean isDirty() {
        return dirty || annotations.isDirty() || domains.isDirty() || ranges.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<LabelledFrame<AnnotationPropertyFrame>>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return !getIRIString().isEmpty() && annotations.isWellFormed() && domains.isWellFormed() && ranges.isWellFormed();
    }

    private String getIRIString() {
        return iriField.getText().trim();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        annotations.setEnabled(enabled);
        domains.setEnabled(enabled);
        ranges.setEnabled(enabled);
    }
}
