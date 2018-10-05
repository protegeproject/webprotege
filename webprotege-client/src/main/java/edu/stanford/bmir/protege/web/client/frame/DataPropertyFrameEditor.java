package edu.stanford.bmir.protege.web.client.frame;

import com.google.common.collect.ImmutableSet;
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
import edu.stanford.bmir.protege.web.client.editor.EditorView;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataListEditor;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDatatypeData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.DataPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class DataPropertyFrameEditor extends Composite implements EditorView<DataPropertyFrame>, HasEnabled {

    private Optional<DataPropertyFrame> lastDataPropertyFrame = Optional.empty();

    interface DataPropertyFrameEditorUiBinder extends UiBinder<HTMLPanel, DataPropertyFrameEditor> {

    }

    private static DataPropertyFrameEditorUiBinder ourUiBinder = GWT.create(DataPropertyFrameEditorUiBinder.class);

    @UiField
    protected HasText iriField;

    @UiField(provided = true)
    protected final PropertyValueListEditor annotations;

    @UiField(provided = true)
    final PrimitiveDataListEditor domains;

    @UiField(provided = true)
    final PrimitiveDataListEditor ranges;

    @UiField
    protected CheckBox functionalCheckBox;

    private boolean enabled;

    private boolean dirty = false;

    private EntityDisplay entityDisplay = entityData -> {};


    @Inject
    public DataPropertyFrameEditor(PropertyValueListEditor editor, Provider<PrimitiveDataEditor> primitiveDataEditorProvider) {
        annotations = editor;
        annotations.setGrammar(PropertyValueGridGrammar.getAnnotationsGrammar());
        domains = new PrimitiveDataListEditor(primitiveDataEditorProvider, PrimitiveType.CLASS);
        ranges = new PrimitiveDataListEditor(primitiveDataEditorProvider, PrimitiveType.DATA_TYPE);
        WebProtegeClientBundle.BUNDLE.style().ensureInjected();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
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

    @UiHandler("functionalCheckBox")
    protected void handleFunctionalCheckBoxChanged(ValueChangeEvent<Boolean> event) {
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
    public void setValue(DataPropertyFrame frame) {
        dirty = false;
        lastDataPropertyFrame = Optional.of(frame);
        String decodedIri = URL.decode(frame.getSubject().getEntity().getIRI().toString());
        iriField.setText(decodedIri);
        annotations.setValue(frame.getPropertyValueList());
        domains.setValue(new ArrayList<>(frame.getDomains()));
        ranges.setValue(new ArrayList<>(frame.getRanges()));
        functionalCheckBox.setValue(frame.isFunctional());
        entityDisplay.setDisplayedEntity(Optional.of(frame.getSubject()));

    }

    @Override
    public void clearValue() {
        iriField.setText("");
        annotations.clearValue();
        domains.clearValue();
        ranges.clearValue();
        entityDisplay.setDisplayedEntity(Optional.empty());
    }

    @Override
    public Optional<DataPropertyFrame> getValue() {
        if(!lastDataPropertyFrame.isPresent()) {
            return Optional.empty();
        }
        final ImmutableSet.Builder<OWLClassData> domainsClasses = ImmutableSet.builder();
        if (domains.getValue().isPresent()) {
            for(OWLPrimitiveData primitiveData : domains.getValue().get()) {
                domainsClasses.add(((OWLClassData) primitiveData));
            }
        }
        final ImmutableSet.Builder<OWLDatatypeData> rangeTypes = ImmutableSet.builder();
        if (ranges.getValue().isPresent()) {
            for(OWLPrimitiveData primitiveData : ranges.getValue().get()) {
                rangeTypes.add(((OWLDatatypeData) primitiveData));
            }
        }
        DataPropertyFrame frame = DataPropertyFrame.get(lastDataPropertyFrame.get().getSubject(),
                                                        annotations.getValue().get().getPropertyValues(),
                                                        domainsClasses.build(),
                                                        rangeTypes.build(),
                                                        functionalCheckBox.getValue());
        return Optional.of(frame);
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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<DataPropertyFrame>> handler) {
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
        functionalCheckBox.setEnabled(enabled);
    }
}
