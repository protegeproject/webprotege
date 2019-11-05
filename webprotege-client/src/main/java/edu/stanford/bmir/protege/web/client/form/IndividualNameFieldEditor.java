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
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-05
 */
public class IndividualNameFieldEditor extends Composite implements FormElementEditor {

    private ProjectId projectId;

    private Optional<OWLEntity> currentValue = Optional.empty();

    interface IndividualNameFieldEditorUiBinder extends UiBinder<HTMLPanel, IndividualNameFieldEditor> {

    }

    private static IndividualNameFieldEditorUiBinder ourUiBinder = GWT.create(IndividualNameFieldEditorUiBinder.class);

    @UiField(provided = true)
    protected PrimitiveDataEditor editor;

    private DispatchServiceManager dispatchServiceManager;

    @Inject
    public IndividualNameFieldEditor(ProjectId projectId,
                                     PrimitiveDataEditor editor,
                                     DispatchServiceManager dispatchServiceManager) {
        this.projectId = projectId;
        this.editor = editor;
        this.editor.setNamedIndividualsAllowed(true);
        this.editor.setAutoSelectSuggestions(true);
        this.dispatchServiceManager = dispatchServiceManager;
        initWidget(ourUiBinder.createAndBindUi(this));
        editor.addValueChangeHandler(event -> {
            currentValue = event.getValue()
                                 .filter(val -> val instanceof OWLNamedIndividualData)
                                 .map(val -> (OWLNamedIndividualData) val)
                                 .map(OWLNamedIndividualData::getEntity);
            ValueChangeEvent.fire(this, getValue());
        });
    }

    public void setPlaceholder(String placeholder) {
        editor.setPlaceholder(placeholder);
    }

    @Override
    public void setValue(FormDataValue object) {
        object.asOWLEntity()
              .filter(OWLEntity::isOWLNamedIndividual)
              .ifPresent(individual -> {
                  this.currentValue = Optional.of(individual);
                    dispatchServiceManager.execute(new GetEntityRenderingAction(projectId,
                                                                                individual),
                                                   result -> editor.setValue(result.getEntityData()));
              });
    }

    @Override
    public void clearValue() {
        this.currentValue = Optional.empty();
        editor.clearValue();
    }

    @Override
    public Optional<FormDataValue> getValue() {
        return currentValue.map(FormDataPrimitive::get);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormDataValue>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }
}
