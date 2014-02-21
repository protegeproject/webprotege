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
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataListEditor;
import edu.stanford.bmir.protege.web.client.rpc.GetRendering;
import edu.stanford.bmir.protege.web.client.rpc.GetRenderingResponse;
import edu.stanford.bmir.protege.web.client.rpc.RenderingServiceManager;
import edu.stanford.bmir.protege.web.client.ui.editor.EditorView;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.OWLPrimitiveDataList;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/04/2013
 */
public class AnnotationPropertyFrameEditor extends Composite implements EditorView<LabelledFrame<AnnotationPropertyFrame>>, HasEnabled {

    interface AnnotationPropertyFrameEditorUiBinder extends UiBinder<HTMLPanel, AnnotationPropertyFrameEditor> {

    }

    private static AnnotationPropertyFrameEditorUiBinder ourUiBinder = GWT.create(AnnotationPropertyFrameEditorUiBinder.class);


    @UiField
    protected TextBox displayNameField;

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

    private ProjectId projectId;


    public AnnotationPropertyFrameEditor(ProjectId projectId) {
        this.projectId = projectId;
        annotations = new PropertyValueListEditor(projectId, PropertyValueGridGrammar.getAnnotationsGrammar());
        domains = new PrimitiveDataListEditor(projectId, PrimitiveType.CLASS);
        ranges = new PrimitiveDataListEditor(projectId, PrimitiveType.DATA_TYPE);
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        iriField.setEnabled(false);
        setEnabled(false);
    }

    @UiHandler("displayNameField")
    protected void handleDisplayNameChanged(ValueChangeEvent<String> event) {
        fireValueChangedIfWellFormed();
    }

    @UiHandler("annotations")
    protected void handleAnnotationsChanged(ValueChangeEvent<Optional<PropertyValueList>> event) {
        fireValueChangedIfWellFormed();
    }

    @UiHandler("domains")
    protected void handleDomainsChanged(ValueChangeEvent<Optional<OWLPrimitiveDataList>> event) {
        fireValueChangedIfWellFormed();
    }


    @UiHandler("ranges")
    protected void handleRangesChanged(ValueChangeEvent<Optional<OWLPrimitiveDataList>> event) {
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
        displayNameField.setText(object.getDisplayName());
        final AnnotationPropertyFrame frame = object.getFrame();
        iriField.setText(frame.getSubject().getIRI().toString());
        annotations.setValue(frame.getPropertyValueList());
        RenderingServiceManager.getManager().execute(new GetRendering(projectId, frame.getDomains()), new AsyncCallback<GetRenderingResponse>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(GetRenderingResponse result) {
                List<OWLPrimitiveData> primitiveDatas = new ArrayList<OWLPrimitiveData>();
                for (OWLEntity cls : frame.getDomains()) {
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
                for (OWLEntity dt : frame.getRanges()) {
                    final Optional<OWLEntityData> entityData = result.getEntityData(dt);
                    if (entityData.isPresent()) {
                        primitiveDatas.add(entityData.get());
                    }
                }
                ranges.setValue(new OWLPrimitiveDataList(primitiveDatas));
            }
        });


    }

    @Override
    public void clearValue() {
        displayNameField.setText("");
        iriField.setText("");
        annotations.clearValue();
        domains.clearValue();
        ranges.clearValue();
    }

    @Override
    public Optional<LabelledFrame<AnnotationPropertyFrame>> getValue() {
        OWLAnnotationProperty property = DataFactory.getOWLAnnotationProperty(getIRIString());
        final Set<OWLEntity> domainsClasses = new HashSet<OWLEntity>(domains.getValue().get().getSignature());
        final Set<OWLEntity> rangeTypes = new HashSet<OWLEntity>(ranges.getValue().get().getSignature());
        AnnotationPropertyFrame frame = new AnnotationPropertyFrame(property, annotations.getValue().get().getAnnotationPropertyValues(), domainsClasses, rangeTypes);
        return Optional.of(new LabelledFrame<AnnotationPropertyFrame>(getDisplayName(), frame));
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
        return !getDisplayName().isEmpty() && !getIRIString().isEmpty() && annotations.isWellFormed() && domains.isWellFormed() && ranges.isWellFormed();
    }

    private String getIRIString() {
        return iriField.getText().trim();
    }

    private String getDisplayName() {
        return displayNameField.getText().trim();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        displayNameField.setEnabled(enabled);
        annotations.setEnabled(enabled);
        domains.setEnabled(enabled);
        ranges.setEnabled(enabled);
    }
}