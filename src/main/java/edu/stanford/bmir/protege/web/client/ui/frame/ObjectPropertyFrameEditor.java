package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
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
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataListEditor;
import edu.stanford.bmir.protege.web.client.ui.editor.EditorView;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataResult;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.inject.Inject;
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

    private Set<ObjectPropertyCharacteristic> characteristics = Sets.newHashSet();

    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public ObjectPropertyFrameEditor(PropertyValueListEditor annotationsEditor, ProjectId projectId, DispatchServiceManager dispatchServiceManager) {
        WebProtegeClientBundle.BUNDLE.style().ensureInjected();
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
        annotations = annotationsEditor;
        annotations.setGrammar(PropertyValueGridGrammar.getAnnotationsGrammar());
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
        characteristics.clear();
        characteristics.addAll(object.getFrame().getCharacteristics());
        dispatchServiceManager.execute(new GetEntityDataAction(projectId, ImmutableSet.<OWLEntity>copyOf(frame.getDomains())), new DispatchServiceCallback<GetEntityDataResult>() {
            @Override
            public void handleSuccess(GetEntityDataResult result) {
                List<OWLPrimitiveData> primitiveDatas = new ArrayList<OWLPrimitiveData>();
                for (OWLClass cls : frame.getDomains()) {
                    final Optional<OWLEntityData> entityData = Optional.fromNullable(result.getEntityDataMap().get(cls));
                    if (entityData.isPresent()) {
                        primitiveDatas.add(entityData.get());
                    }
                }
                domains.setValue(primitiveDatas);
            }
        });
        dispatchServiceManager.execute(new GetEntityDataAction(projectId, ImmutableSet.<OWLEntity>copyOf(frame.getRanges())), new DispatchServiceCallback<GetEntityDataResult>() {
            @Override
            public void handleSuccess(GetEntityDataResult result) {
                List<OWLPrimitiveData> primitiveDatas = new ArrayList<OWLPrimitiveData>();
                for (OWLClass cls : frame.getRanges()) {
                    final Optional<OWLEntityData> entityData = Optional.fromNullable(result.getEntityDataMap().get(cls));
                    if (entityData.isPresent()) {
                        primitiveDatas.add(entityData.get());
                    }
                }
                ranges.setValue(primitiveDatas);
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
        Set<PropertyAnnotationValue> annotationValueSet = new HashSet<PropertyAnnotationValue>();
        annotationValueSet.addAll(annotations.getValue().get().getAnnotationPropertyValues());
        final ObjectPropertyFrame previousFrame = previouslySetValue.get().getFrame();
        OWLObjectProperty subject = previousFrame.getSubject();
        List<OWLClass> editedDomains = Lists.newArrayList();
        for(OWLPrimitiveData data : domains.getValue().get()) {
            editedDomains.add((OWLClass) data.getObject());
        }
        List<OWLClass> editedRanges = Lists.newArrayList();
        for(OWLPrimitiveData data : ranges.getValue().get()) {
            editedRanges.add((OWLClass) data.getObject());
        }
        ObjectPropertyFrame frame = new ObjectPropertyFrame(subject, annotationValueSet,
                new HashSet<>(editedDomains),
                new HashSet<>(editedRanges),
                Collections.<OWLObjectProperty>emptySet(),
                characteristics);
        return Optional.of(new LabelledFrame<>(displayName, frame));
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
