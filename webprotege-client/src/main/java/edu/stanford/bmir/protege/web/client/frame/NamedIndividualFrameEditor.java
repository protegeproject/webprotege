package edu.stanford.bmir.protege.web.client.frame;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
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
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
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
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.Collections.emptyList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/12/2012
 */
public class NamedIndividualFrameEditor extends SimplePanel implements ValueEditor<NamedIndividualFrame>, HasEnabled, EditorView<NamedIndividualFrame> {

    private static final Messages MESSAGES = GWT.create(Messages.class);

    private static NamedIndividualFrameEditorUiBinder ourUiBinder = GWT.create(NamedIndividualFrameEditorUiBinder.class);

    @UiField
    protected HasText iriField;

    @UiField(provided = true)
    PropertyValueListEditor annotations;

    @UiField(provided = true)
    protected PropertyValueListEditor assertions;

    @UiField(provided = true)
    protected PrimitiveDataListEditor types;

    @UiField(provided = true)
    protected PrimitiveDataListEditor sameAs;


    private Optional<NamedIndividualFrame> editedFrame = Optional.empty();

    private boolean enabled = true;

    private boolean dirty = false;

    private EntityDisplay entityDisplay = entityData -> {};

    @Inject
    public NamedIndividualFrameEditor(PropertyValueListEditor annotationsEditor,
                                      PropertyValueListEditor assertionsEditor,
                                      Provider<PrimitiveDataEditor> primitiveDataEditorProvider, DispatchServiceManager dispatchServiceManager) {
        this.annotations = annotationsEditor;
        this.annotations.setGrammar(PropertyValueGridGrammar.getAnnotationsGrammar());
        assertions = assertionsEditor;
        assertions.setGrammar(PropertyValueGridGrammar.getNamedIndividualGrammar());
        types = new PrimitiveDataListEditor(primitiveDataEditorProvider, PrimitiveType.CLASS);
        types.setPlaceholder(MESSAGES.frame_enterAClassName());
        sameAs = new PrimitiveDataListEditor(primitiveDataEditorProvider, PrimitiveType.NAMED_INDIVIDUAL);
        sameAs.setPlaceholder(MESSAGES.frame_enterIndividualName());
        WebProtegeClientBundle.BUNDLE.style().ensureInjected();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        setWidget(rootElement);
    }

    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = checkNotNull(entityDisplay);
    }

    public boolean isDirty() {
        return annotations.isDirty() || assertions.isDirty() || dirty || types.isDirty() || sameAs.isDirty();
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
        types.setEnabled(enabled);
        annotations.setEnabled(enabled);
        assertions.setEnabled(enabled);
        sameAs.setEnabled(enabled);
    }

    @Override
    public boolean isWellFormed() {
        return types.isWellFormed() && annotations.isWellFormed() && assertions.isWellFormed() && sameAs.isWellFormed();
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public Optional<NamedIndividualFrame> getValue() {
        return editedFrame.map(frame -> NamedIndividualFrame.get(editedFrame.get().getSubject(),
                                                                 getTypes(),
                                                                 getPropertyValues(),
                                                                 getSameAs()));
    }

    @Override
    public void setValue(final NamedIndividualFrame frame) {
        editedFrame = Optional.of(frame);
        String decodedIri = URL.decode(frame.getSubject().getEntity().getIRI().toString());
        iriField.setText(decodedIri);
        annotations.setValue(new PropertyValueList(frame.getAnnotationPropertyValues()));
        assertions.setValue(new PropertyValueList(frame.getLogicalPropertyValues()));
        setDirty(false, EventStrategy.DO_NOT_FIRE_EVENTS);
        types.setValue(new ArrayList<>(frame.getClasses()));
        sameAs.setValue(new ArrayList<>(frame.getSameIndividuals()));
        entityDisplay.setDisplayedEntity(Optional.of(frame.getSubject()));
    }

    private ImmutableSet<PropertyValue> getPropertyValues() {
        Stream<PropertyValue> annosStream = annotations.getValue()
                                                  .map(PropertyValueList::getPropertyValues)
                                                  .orElse(ImmutableSet.of())
                                                  .stream();

        Stream<PropertyValue> logicalStream = assertions.getValue()
                         .map(PropertyValueList::getPropertyValues)
                         .map(ImmutableSet::copyOf)
                         .orElse(ImmutableSet.of())
                         .stream();
        return Streams.concat(annosStream, logicalStream).collect(toImmutableSet());
    }

    private ImmutableSet<OWLClassData> getTypes() {
        return types.getValue()
                    .orElse(emptyList())
                    .stream()
                    .map(type -> (OWLClassData) type)
                    .collect(toImmutableSet());
    }

    private ImmutableSet<OWLNamedIndividualData> getSameAs() {
        return sameAs.getValue()
                     .orElse(emptyList())
                     .stream()
                     .map(ind -> (OWLNamedIndividualData) ind)
                     .collect(toImmutableSet());
    }

    @UiHandler("annotations")
    protected void handleAnnotationsDirtyChanged(DirtyChangedEvent event) {
        setDirty(true, EventStrategy.FIRE_EVENTS);
    }

    @UiHandler("annotations")
    protected void handleAnnotationsChange(ValueChangeEvent<Optional<PropertyValueList>> event) {
        if (isWellFormed()) {
            ValueChangeEvent.fire(this, getValue());
        }
    }
    @UiHandler("assertions")
    protected void handleAssertionsDirtyChanged(DirtyChangedEvent event) {
        setDirty(true, EventStrategy.FIRE_EVENTS);
    }

    @UiHandler("assertions")
    protected void handleAssertionsChange(ValueChangeEvent<Optional<PropertyValueList>> event) {
        if (isWellFormed()) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @UiHandler("types")
    protected void handleTypesDirtyChanged(DirtyChangedEvent event) {
        setDirty(true, EventStrategy.FIRE_EVENTS);
    }

    @UiHandler("types")
    protected void handleTypesChanged(ValueChangeEvent<Optional<List<OWLPrimitiveData>>> event) {
        GWT.log("[NamedIndividualFrameEditor] Types changed");
        if (isWellFormed()) {
            GWT.log("[NamedIndividualFrameEditor] Types are well formed");
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @UiHandler("sameAs")
    protected void handleSameAsDirtyChanged(DirtyChangedEvent event) {
        setDirty(true, EventStrategy.FIRE_EVENTS);
    }

    @UiHandler("sameAs")
    protected void handleSameAsChanged(ValueChangeEvent<Optional<List<OWLPrimitiveData>>> event) {
        if (isWellFormed()) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    private void setDirty(boolean dirty, EventStrategy eventStrategy) {
        this.dirty = dirty;
        if (eventStrategy == EventStrategy.FIRE_EVENTS) {
            fireEvent(new DirtyChangedEvent());
        }
    }

    @Override
    public void clearValue() {
        iriField.setText("");
        types.clearValue();
        annotations.clearValue();
        assertions.clearValue();
        sameAs.clearValue();
        entityDisplay.setDisplayedEntity(Optional.empty());
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<NamedIndividualFrame>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    interface NamedIndividualFrameEditorUiBinder extends UiBinder<HTMLPanel, NamedIndividualFrameEditor> {

    }
}
