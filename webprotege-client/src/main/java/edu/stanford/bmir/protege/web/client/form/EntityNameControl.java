package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/04/16
 */
public class EntityNameControl extends Composite implements ValueEditor<FormDataValue>, HasPlaceholder {

    interface EntityNameControlUiBinder extends UiBinder<HTMLPanel, EntityNameControl> {

    }

    private static EntityNameControlUiBinder ourUiBinder = GWT.create(EntityNameControlUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl editor;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;


    private Optional<OWLClass> currentValue = Optional.empty();

    @Inject
    public EntityNameControl(@Nonnull ProjectId projectId,
                             @Nonnull DispatchServiceManager dispatchServiceManager,
                             Provider<PrimitiveDataEditor> primitiveDataEditorProvider) {
        editor = (PrimitiveDataEditorImpl) primitiveDataEditorProvider.get();
        initWidget(ourUiBinder.createAndBindUi(this));
        editor.addValueChangeHandler(this::handleEditorValueChanged);
        editor.setAutoSelectSuggestions(true);
        editor.setClassesAllowed(true);
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    private void handleEditorValueChanged(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
        currentValue = event.getValue()
                            .filter(val -> val instanceof OWLClassData)
                            .map(val -> (OWLClassData) val)
                            .map(OWLClassData::getEntity);
        ValueChangeEvent.fire(EntityNameControl.this, getValue());
    }

    @Override
    public void setValue(FormDataValue object) {
        Optional<OWLEntity> entity = object.asOWLEntity();
        this.currentValue = entity.filter(OWLEntity::isOWLClass)
                                  .map(OWLEntity::asOWLClass);
        entity.ifPresent(e -> {
            dispatchServiceManager.execute(new GetEntityRenderingAction(projectId, e),
                                           result -> editor.setValue(result.getEntityData()));
        });
        if(!entity.isPresent()) {
            editor.clearValue();
        }
    }

    @Override
    public String getPlaceholder() {
        return editor.getPlaceholder();
    }

    @Override
    public void setPlaceholder(String placeholder) {
        editor.setPlaceholder(placeholder);
    }

    @Override
    public void clearValue() {
        currentValue = Optional.empty();
        editor.clearValue();
    }

    @Override
    public Optional<FormDataValue> getValue() {
        return currentValue.map(FormDataPrimitive::get);
    }

    @Override
    public boolean isDirty() {
        return editor.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return editor.addDirtyChangedHandler(handler);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormDataValue>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return getValue().isPresent();
    }
}
