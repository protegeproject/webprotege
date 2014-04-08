package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataListEditor;
import edu.stanford.bmir.protege.web.client.ui.editor.EditorView;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.library.common.EventStrategy;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.HasEntityDataProvider;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import edu.stanford.bmir.protege.web.shared.frame.OWLPrimitiveDataList;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/12/2012
 */
public class NamedIndividualFrameEditor extends AbstractFrameEditor<LabelledFrame<NamedIndividualFrame>> implements ValueEditor<LabelledFrame<NamedIndividualFrame>>, HasEnabled, EditorView<LabelledFrame<NamedIndividualFrame>> {

    private NamedIndividualFrame editedFrame;

    @UiField
    protected TextBox displayNameField;

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

    interface NamedIndividualFrameEditorUiBinder extends UiBinder<HTMLPanel, NamedIndividualFrameEditor> {

    }

    private static NamedIndividualFrameEditorUiBinder ourUiBinder = GWT.create(NamedIndividualFrameEditorUiBinder.class);

    public NamedIndividualFrameEditor(ProjectId projectId) {
        super(projectId);
//        PropertyValueGridGrammar grammar = new PropertyValueGridGrammar();
//        grammar.addProduction(PrimitiveType.ANNOTATION_PROPERTY, PrimitiveType.LITERAL);
//        grammar.addProduction(PrimitiveType.ANNOTATION_PROPERTY, PrimitiveType.IRI);
//        grammar.addProduction(PrimitiveType.OBJECT_PROPERTY, PrimitiveType.NAMED_INDIVIDUAL);
//        grammar.addProduction(PrimitiveType.OBJECT_PROPERTY, PrimitiveType.CLASS);
//        grammar.addProduction(PrimitiveType.DATA_PROPERTY, PrimitiveType.LITERAL);
//        grammar.addProduction(PrimitiveType.DATA_PROPERTY, PrimitiveType.DATA_TYPE);
        assertions = new PropertyValueListEditor(projectId);
        assertions.setGrammar(PropertyValueGridGrammar.getNamedIndividualGrammar());
        types = new PrimitiveDataListEditor(projectId, PrimitiveType.CLASS);
        types.setPlaceholder("Enter class name");
        sameAs = new PrimitiveDataListEditor(projectId, PrimitiveType.NAMED_INDIVIDUAL);
        sameAs.setPlaceholder("Enter individual name");
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        setWidget(rootElement);
        iriField.setEnabled(false);
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
        displayNameField.setEnabled(enabled);
        iriField.setEnabled(false);
        types.setEnabled(enabled);
        assertions.setEnabled(enabled);
        sameAs.setEnabled(enabled);
    }

    @Override
    public void setValue(final LabelledFrame<NamedIndividualFrame> frame, HasEntityDataProvider entityDataProvider) {
        editedFrame = frame.getFrame();
        displayNameField.setValue(frame.getDisplayName());
        iriField.setValue(editedFrame.getSubject().getIRI().toString());
        assertions.setValue(editedFrame.getPropertyValueList());
        setDirty(false, EventStrategy.DO_NOT_FIRE_EVENTS);

        List<OWLPrimitiveData> dataList = new ArrayList<OWLPrimitiveData>();
        for (OWLClass cls : editedFrame.getClasses()) {
            final Optional<OWLEntityData> rendering = entityDataProvider.getEntityData(cls);
            if (rendering.isPresent()) {
                dataList.add(rendering.get());
            }
        }
        OWLPrimitiveDataList list = new OWLPrimitiveDataList(dataList);
        types.setValue(list);

        List<OWLPrimitiveData> sameAsList = new ArrayList<OWLPrimitiveData>();
        for (OWLNamedIndividual individual : editedFrame.getSameIndividuals()) {
            Optional<OWLEntityData> individualRendering = entityDataProvider.getEntityData(individual);
            if (individualRendering.isPresent()) {
                sameAsList.add(individualRendering.get());
            }
        }
        sameAs.setValue(new OWLPrimitiveDataList(sameAsList));
    }

    @Override
    public boolean isWellFormed() {
        return !getDisplayName().isEmpty() && types.isWellFormed() && assertions.isWellFormed() && sameAs.isWellFormed();
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public Optional<LabelledFrame<NamedIndividualFrame>> getValue() {
       if(editedFrame == null) {
           return Optional.absent();
       }
       PropertyValueList propertyValueList = assertions.getValue().get();
       Set<OWLClass> rawTypes = getRawTypes();
       Set<OWLNamedIndividual> sameAs = getRawSameAs();
       NamedIndividualFrame reference = new NamedIndividualFrame(editedFrame.getSubject(), rawTypes, propertyValueList.getPropertyValues(), sameAs);
       return Optional.of(new LabelledFrame<NamedIndividualFrame>(getDisplayName(), reference));
   }

    private Set<OWLClass> getRawTypes() {
        Set<OWLClass> rawTypes = new HashSet<OWLClass>();
        Optional<OWLPrimitiveDataList> typesList = types.getValue();
        if(typesList.isPresent()) {
            rawTypes.addAll(typesList.get().getEntitiesOfType(EntityType.CLASS));
        }
        return rawTypes;
    }

    private Set<OWLNamedIndividual> getRawSameAs() {
        Set<OWLNamedIndividual> rawSameAs = new HashSet<OWLNamedIndividual>();
        Optional<OWLPrimitiveDataList> sameAsList = sameAs.getValue();
        if(sameAsList.isPresent()) {
            rawSameAs.addAll(sameAsList.get().getEntitiesOfType(EntityType.NAMED_INDIVIDUAL));
        }
        return rawSameAs;
    }

    private String getDisplayName() {
        return displayNameField.getText().trim();
    }

    @UiHandler("displayNameField")
    protected void handleDisplayNameChange(ValueChangeEvent<String> evt) {
        setDirty(true, EventStrategy.FIRE_EVENTS);
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
        if(isWellFormed()) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @UiHandler("types")
    protected void handleTypesDirtyChanged(DirtyChangedEvent event) {
        setDirty(true, EventStrategy.FIRE_EVENTS);
    }

    @UiHandler("types")
    protected void handleTypesChanged(ValueChangeEvent<Optional<OWLPrimitiveDataList>> event) {
        if(isWellFormed()) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @UiHandler("sameAs")
    protected void handleSameAsDirtyChanged(DirtyChangedEvent event) {
        setDirty(true, EventStrategy.FIRE_EVENTS);
    }

    @UiHandler("sameAs")
    protected void handleSameAsChanged(ValueChangeEvent<Optional<OWLPrimitiveDataList>> event) {
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
        displayNameField.setText("");
        iriField.setText("");
        types.clearValue();
        assertions.clearValue();
        sameAs.clearValue();
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
