package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.form.OWLEntity2FormControlDataVisitor;
import edu.stanford.bmir.protege.web.shared.form.OWLPrimitive2FormControlDataConverter;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlData;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class ChoiceDescriptorPresenter implements ValueEditor<ChoiceDescriptor>, HasRequestFocus {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ChoiceDescriptorView view;

    @Nonnull
    private DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final HandlerManager handlerManager = new HandlerManager(this);

    @Inject
    public ChoiceDescriptorPresenter(@Nonnull ProjectId projectId,
                                     @Nonnull ChoiceDescriptorView view,
                                     @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        view.setChoiceValueChangedHandler(this::handleChoiceValueChanged);
    }

    @Override
    public void setValue(ChoiceDescriptor object) {
        view.setLabel(object.getLabel());
        object.getValue().asEntity().ifPresent(entity -> {
            dispatchServiceManager.execute(new GetEntityRenderingAction(projectId, entity),
                                           result -> view.setPrimitiveData(result.getEntityData()));
        });
        object.getValue().asIri().ifPresent(iri -> {
            view.setPrimitiveData(IRIData.get(iri, ImmutableMap.of()));
        });
        object.getValue().asLiteral().ifPresent(literal -> {
            view.setPrimitiveData(OWLLiteralData.get(literal));
        });
    }

    @Override
    public void clearValue() {
        view.clear();
    }

    @Override
    public Optional<ChoiceDescriptor> getValue() {
        return view.getDataValue().map(primitiveData -> {
            OWLPrimitive2FormControlDataConverter converter = new OWLPrimitive2FormControlDataConverter(new OWLEntity2FormControlDataVisitor());
            PrimitiveFormControlData dataValue = converter.toFormControlData(primitiveData.getObject());
            return ChoiceDescriptor.choice(view.getLabel(), dataValue);
        });
    }

    private void handleChoiceValueChanged() {
        view.getDataValue().ifPresent(value -> {
            if(view.getLabel().asMap().isEmpty()) {
                LanguageMap.LanguageMapBuilder builder = LanguageMap.builder();
                value.getShortForms().forEach((dictLang, shortForm) -> {
                    builder.put(dictLang.getLang(), shortForm);
                });
                view.setLabel(builder.build());
            }
        });
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<ChoiceDescriptor>> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return handlerManager.addHandler(DirtyChangedEvent.TYPE, handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    @Override
    public boolean isWellFormed() {
        return getValue().isPresent();
    }

    @Override
    public void requestFocus() {
        view.requestFocus();
    }
}
