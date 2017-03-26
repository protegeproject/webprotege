package edu.stanford.bmir.protege.web.client.frame;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
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
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/12/2012
 */
public class NamedIndividualFrameEditor extends SimplePanel implements ValueEditor<LabelledFrame<NamedIndividualFrame>>, HasEnabled, EditorView<LabelledFrame<NamedIndividualFrame>> {

    private java.util.Optional<LabelledFrame<NamedIndividualFrame>> editedFrame = java.util.Optional.empty();

    @UiField
    protected TextBox iriField;

    @UiField(provided = true)
    protected PropertyValueListEditor assertions;

    @UiField(provided = true)
    protected PrimitiveDataListEditor types;

    @UiField(provided = true)
    protected PrimitiveDataListEditor sameAs;

    private boolean enabled = true;

    private boolean dirty = false;

    private EntityDisplay entityDisplay = entityData -> {};

    interface NamedIndividualFrameEditorUiBinder extends UiBinder<HTMLPanel, NamedIndividualFrameEditor> {

    }

    private static NamedIndividualFrameEditorUiBinder ourUiBinder = GWT.create(NamedIndividualFrameEditorUiBinder.class);

    @Inject
    public NamedIndividualFrameEditor(PropertyValueListEditor assertionsEditor, ProjectId projectId, Provider<PrimitiveDataEditor> primitiveDataEditorProvider, DispatchServiceManager dispatchServiceManager) {
        assertions = assertionsEditor;
        assertions.setGrammar(PropertyValueGridGrammar.getNamedIndividualGrammar());
        types = new PrimitiveDataListEditor(primitiveDataEditorProvider, PrimitiveType.CLASS);
        types.setPlaceholder("Enter class name");
        sameAs = new PrimitiveDataListEditor(primitiveDataEditorProvider, PrimitiveType.NAMED_INDIVIDUAL);
        sameAs.setPlaceholder("Enter individual name");
        WebProtegeClientBundle.BUNDLE.style().ensureInjected();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        setWidget(rootElement);
        iriField.setEnabled(false);
    }


    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = checkNotNull(entityDisplay);
    }

    public boolean isDirty() {
        return assertions.isDirty() || dirty || types.isDirty() || sameAs.isDirty();
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
        types.setEnabled(enabled);
        assertions.setEnabled(enabled);
        sameAs.setEnabled(enabled);
    }

    @Override
    public void setValue(final LabelledFrame<NamedIndividualFrame> frame) {
        editedFrame = java.util.Optional.of(frame);
        iriField.setValue(frame.getFrame().getSubject().getEntity().getIRI().toString());
        assertions.setValue(frame.getFrame().getPropertyValueList());
        setDirty(false, EventStrategy.DO_NOT_FIRE_EVENTS);
        types.setValue(new ArrayList<>(frame.getFrame().getClasses()));
        sameAs.setValue(new ArrayList<>(frame.getFrame().getSameIndividuals()));
        entityDisplay.setDisplayedEntity(java.util.Optional.of(frame.getFrame().getSubject()));
    }

    @Override
    public boolean isWellFormed() {
        return types.isWellFormed() && assertions.isWellFormed() && sameAs.isWellFormed();
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public Optional<LabelledFrame<NamedIndividualFrame>> getValue() {
        GWT.log("[NamedIndividualFrameEditor] Get value: Dirty: " + isDirty() + " Edited frame: " + editedFrame);
       if(!editedFrame.isPresent()) {
           return Optional.absent();
       }
       PropertyValueList propertyValueList = assertions.getValue().get();
       Set<OWLClassData> rawTypes = getRawTypes();
       Set<OWLNamedIndividualData> sameAs = getRawSameAs();
       NamedIndividualFrame reference = new NamedIndividualFrame(
               editedFrame.get().getFrame().getSubject(),
               rawTypes, propertyValueList, sameAs);
       return Optional.of(new LabelledFrame<>(editedFrame.get().getDisplayName(), reference));
   }

    private Set<OWLClassData> getRawTypes() {
        Set<OWLClassData> rawTypes = new HashSet<>();
        Optional<List<OWLPrimitiveData>> typesList = types.getValue();
        if(typesList.isPresent()) {
            for(OWLPrimitiveData data : typesList.get()) {
                rawTypes.add((OWLClassData) data);
            }
        }
        return rawTypes;
    }

    private Set<OWLNamedIndividualData> getRawSameAs() {
        Set<OWLNamedIndividualData> rawSameAs = new HashSet<>();
        Optional<List<OWLPrimitiveData>> sameAsList = sameAs.getValue();
        if(sameAsList.isPresent()) {
            for(OWLPrimitiveData data : sameAsList.get()) {
                rawSameAs.add((OWLNamedIndividualData) data);
            }
        }
        return rawSameAs;
    }

    @UiHandler("assertions")
    protected void handleAssertionsDirtyChanged(DirtyChangedEvent event) {
        setDirty(true, EventStrategy.FIRE_EVENTS);
    }

    @UiHandler("assertions")
    protected void handleAssertionsChange(ValueChangeEvent<Optional<PropertyValueList>> event) {
        if(isWellFormed()) {
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
        if(isWellFormed()) {
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
        if(isWellFormed()) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    private void setDirty(boolean dirty, EventStrategy eventStrategy) {
        this.dirty = dirty;
        if(eventStrategy == EventStrategy.FIRE_EVENTS) {
            fireEvent(new DirtyChangedEvent());
        }
    }


    @Override
    public void clearValue() {
        iriField.setText("");
        types.clearValue();
        assertions.clearValue();
        sameAs.clearValue();
        entityDisplay.setDisplayedEntity(java.util.Optional.empty());
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<LabelledFrame<NamedIndividualFrame>>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}
