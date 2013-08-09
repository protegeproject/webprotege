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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.editor.EditorView;
import edu.stanford.bmir.protege.web.client.ui.library.common.EventStrategy;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrameType;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/12/2012
 */
public class ClassFrameEditor extends SimplePanel implements ClassFrameEditorPresenter, EditorView<LabelledFrame<ClassFrame>> {

    @UiField
    protected TextBox displayNameField;

    @UiField
    protected TextBox iriField;

//    @UiField(provided = true)
//    protected final PrimitiveDataListEditor subclassof;

    @UiField(provided = true)
    protected final PropertyValueListEditor annotations;

    @UiField(provided = true)
    protected final PropertyValueListEditor properties;

    private final ProjectId projectId;

    private LabelledFrame<ClassFrame> lastClassFrame;

    private OWLClass currentSubject;

    private Set<OWLClass> currentClasses = new HashSet<OWLClass>();

    private boolean enabled = true;

    private boolean editable = true;

    private boolean lastEnabled = enabled;

    private boolean dirty;

    interface ClassFrameEditor2UiBinder extends UiBinder<HTMLPanel, ClassFrameEditor> {

    }

    private static ClassFrameEditor2UiBinder ourUiBinder = GWT.create(ClassFrameEditor2UiBinder.class);

    public ClassFrameEditor(ProjectId projectId, PropertyValueListEditor annotations, PropertyValueListEditor properties) {
        this.projectId = projectId;
        this.annotations = annotations;
        this.properties = properties;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        setWidget(rootElement);

    }

    public void setValue(final LabelledFrame<ClassFrame> lcf) {
        lastClassFrame = lcf;
        currentSubject = lcf.getFrame().getSubject();
        currentClasses.clear();
        currentClasses.addAll(lcf.getFrame().getClasses());

        String unquoted = removeQuotes(lcf.getDisplayName());
        displayNameField.setValue(unquoted);

        iriField.setValue(lcf.getFrame().getSubject().getIRI().toString());
        annotations.setValue(new PropertyValueList(new ArrayList<PropertyValue>(lcf.getFrame().getAnnotationPropertyValues())));
        properties.setValue(new PropertyValueList(new ArrayList<PropertyValue>(lcf.getFrame().getLogicalPropertyValues())));

        setDirty(false, EventStrategy.DO_NOT_FIRE_EVENTS);

        updatePropertiesEnabled();

    }

    private void updatePropertiesEnabled() {
        if(lastClassFrame == null) {
            return;
        }
        if(lastClassFrame.getFrame().getClassFrameType() == ClassFrameType.ASSERTED) {
            properties.setEnabled(enabled);
        }
        else {
            properties.setEnabled(false);
        }
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
        lastEnabled = enabled;
        setEnabledInternal(enabled);
    }

    private void setEnabledInternal(boolean enabled) {
        this.enabled = enabled;
        displayNameField.setEnabled(enabled);
        iriField.setEnabled(false);
        annotations.setEnabled(enabled);
        properties.setEnabled(enabled);
        updatePropertiesEnabled();
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
        return annotations.isDirty() || properties.isDirty() || dirty;
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
    public Optional<LabelledFrame<ClassFrame>> getValue() {
        if(currentSubject == null) {
            return Optional.absent();
        }
        else {

            ClassFrame.Builder builder = new ClassFrame.Builder(currentSubject);
            builder.addPropertyValues(annotations.getValue().get().getPropertyValues());
            builder.addPropertyValues(properties.getValue().get().getPropertyValues());
            for(OWLClass cls : currentClasses) {
                builder.addClass(cls);
            }
            ClassFrame cf = builder.build();
            LabelledFrame<ClassFrame> labelledClassFrame = new LabelledFrame<ClassFrame>(getDisplayName(), cf);
            return Optional.of(labelledClassFrame);
        }
    }



    private String getDisplayName() {
        return displayNameField.getText().trim();
    }

    @Override
    public boolean isWellFormed() {
        return !getDisplayName().isEmpty() && annotations.isWellFormed() && properties.isWellFormed();
    }

    @Override
    public void clearValue() {
        displayNameField.setText("");
        annotations.clearValue();
        properties.clearValue();
    }


    @UiHandler("displayNameField")
    protected void handeDisplayNameChange(ValueChangeEvent<String> evt) {
        setDirty(true, EventStrategy.FIRE_EVENTS);
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
    }

    @UiHandler("properties")
    protected void handlePropertiesValueChange(ValueChangeEvent<Optional<PropertyValueList>> evt) {
        if(isWellFormed()) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @UiHandler("properties")
    protected void handlePropertiesDirtyChanged(DirtyChangedEvent evt) {
        setDirty(true, EventStrategy.FIRE_EVENTS);
    }



//    @UiHandler("subclassof")
//    protected void handleSubClassOfChanged(ValueChangeEvent<Optional<OWLPrimitiveDataList>> evt) {
//        if(isWellFormed()) {
//            ValueChangeEvent.fire(this, getValue());
//        }
//    }
//
//    @UiHandler("subclassof")
//    protected void handleSubClassOfDirty(DirtyChangedEvent evt) {
//        setDirty(true, EventStrategy.FIRE_EVENTS);
//    }


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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<LabelledFrame<ClassFrame>>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public Widget getWidget() {
        return this;
    }
}