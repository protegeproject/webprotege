package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataListEditor;
import edu.stanford.bmir.protege.web.client.rpc.GetRendering;
import edu.stanford.bmir.protege.web.client.rpc.GetRenderingResponse;
import edu.stanford.bmir.protege.web.client.rpc.RenderingServiceManager;
import edu.stanford.bmir.protege.web.client.ui.editor.EditorView;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public class ObjectPropertyFrameEditor extends FlowPanel implements EntityFrameEditor, ValueEditor<LabelledFrame<ObjectPropertyFrame>>, HasEnabled, EditorView<LabelledFrame<ObjectPropertyFrame>> {

    @UiField
    protected TextBox displayNameField;

    @UiField
    protected TextBox iriField;

    @UiField(provided = true)
    protected final PropertyValueListEditor annotations;

    @UiField(provided = true)
    final PrimitiveDataListEditor domains;

    @UiField(provided = true)
    final PrimitiveDataListEditor ranges;

    private boolean dirty = false;

    private Optional<LabelledFrame<ObjectPropertyFrame>> previouslySetValue = Optional.absent();

    interface ObjectPropertyFrameEditorUiBinder extends UiBinder<HTMLPanel, ObjectPropertyFrameEditor> {

    }

    private static ObjectPropertyFrameEditorUiBinder ourUiBinder = GWT.create(ObjectPropertyFrameEditorUiBinder.class);

    private ProjectId projectId;

    public ObjectPropertyFrameEditor(ProjectId projectId) {
        this.projectId = projectId;
        annotations = new PropertyValueListEditor(projectId, PropertyValueGridGrammar.getAnnotationsGrammar());
        domains = new PrimitiveDataListEditor(projectId, PrimitiveType.CLASS);
        domains.setPlaceholder("Enter class name");
        ranges = new PrimitiveDataListEditor(projectId, PrimitiveType.CLASS);
        ranges.setPlaceholder("Enter class name");
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        add(rootElement);
        displayNameField.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                handleChange();
            }
        });
    }

    private boolean enabled = false;


    private void fireEventIfWellFormed() {
        if(isWellFormed()) {
            dirty = true;
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @UiHandler("displayNameField")
    protected void handleDisplayNameChanged(ValueChangeEvent<String> event) {
        fireEventIfWellFormed();
    }

    @UiHandler("annotations")
    protected void handleAnnotationsChanged(ValueChangeEvent<Optional<PropertyValueList>> event) {
        fireEventIfWellFormed();
    }

    @UiHandler("domains")
    protected void handleDomainsChanged(ValueChangeEvent<Optional<OWLPrimitiveDataList>> event) {
        fireEventIfWellFormed();
    }

    @UiHandler("ranges")
    protected void handleRangesChanged(ValueChangeEvent<Optional<OWLPrimitiveDataList>> event) {
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
        displayNameField.setEnabled(enabled);
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
        displayNameField.setValue(object.getDisplayName());
        final ObjectPropertyFrame frame = object.getFrame();
        iriField.setValue(frame.getSubject().getIRI().toString());
        annotations.setValue(new PropertyValueList(Collections.<PropertyValue>unmodifiableSet(frame.getAnnotationPropertyValues())));
        RenderingServiceManager.getManager().execute(new GetRendering(projectId, frame.getDomains()), new AsyncCallback<GetRenderingResponse>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(GetRenderingResponse result) {
                List<OWLPrimitiveData> primitiveDatas = new ArrayList<OWLPrimitiveData>();
                for (OWLClass cls : frame.getDomains()) {
                    final Optional<OWLEntityData> entityData = result.getEntityData(cls);
                    if (entityData.isPresent()) {
                        primitiveDatas.add(entityData.get());
                    }
                }
                domains.setValue(new OWLPrimitiveDataList(primitiveDatas));
            }
        });
        RenderingServiceManager.getManager().execute(new GetRendering(projectId, frame.getRanges()), new AsyncCallback<GetRenderingResponse>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(GetRenderingResponse result) {
                List<OWLPrimitiveData> primitiveDatas = new ArrayList<OWLPrimitiveData>();
                for (OWLClass cls : frame.getRanges()) {
                    final Optional<OWLEntityData> entityData = result.getEntityData(cls);
                    if (entityData.isPresent()) {
                        primitiveDatas.add(entityData.get());
                    }
                }
                ranges.setValue(new OWLPrimitiveDataList(primitiveDatas));
            }
        });
        previouslySetValue = Optional.of(object);
    }

    @Override
    public void clearValue() {
        dirty = false;
        displayNameField.setValue("");
        iriField.setValue("");
        annotations.clearValue();
        previouslySetValue = Optional.absent();
    }

    @Override
    public Optional<LabelledFrame<ObjectPropertyFrame>> getValue() {
        if(!previouslySetValue.isPresent()) {
            return previouslySetValue;
        }
        String displayName = getDisplayName();
        String iriString = iriField.getValue().trim();
        Set<PropertyAnnotationValue> annotationValueSet = new HashSet<PropertyAnnotationValue>();
        annotationValueSet.addAll(annotations.getValue().get().getAnnotationPropertyValues());
        final ObjectPropertyFrame previousFrame = previouslySetValue.get().getFrame();
        OWLObjectProperty subject = previousFrame.getSubject();
        List<OWLClass> editedDomains = domains.getValue().get().getEntitiesOfType(EntityType.CLASS);
        List<OWLClass> editedRanges = ranges.getValue().get().getEntitiesOfType(EntityType.CLASS);
        ObjectPropertyFrame frame = new ObjectPropertyFrame(subject, annotationValueSet, new HashSet<OWLClass>(editedDomains), new HashSet<OWLClass>(editedRanges), Collections.<OWLObjectProperty>emptySet());
        return Optional.of(new LabelledFrame<ObjectPropertyFrame>(displayName, frame));
    }

    private String getDisplayName() {
        return displayNameField.getText().trim();
    }

    @Override
    public boolean isWellFormed() {
        return !getDisplayName().isEmpty() && annotations.isWellFormed() && domains.isWellFormed() && ranges.isWellFormed();
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