package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-09
 *
 * Adapts a form presenter to a {@link FormElementEditor}.
 */
public class FormPresenterAdapter implements FormElementEditor, HasValueChangeHandlers<Optional<FormDataValue>> {

    @Nonnull
    private final SimplePanel container = new SimplePanel();

    @Nonnull
    private final FormDescriptor formDescriptor;

    @Nonnull
    private final FormPresenter formPresenter;

    private final HandlerManager handlerManager = new HandlerManager(this);

    public FormPresenterAdapter(@Nonnull FormDescriptor formDescriptor,
                                @Nonnull FormPresenter formPresenter) {
        this.formDescriptor = checkNotNull(formDescriptor);
        this.formPresenter = checkNotNull(formPresenter);
    }

    public void start() {
        formPresenter.start(container);
        formPresenter.setFormDataChangedHandler(this::handleFormDataChanged);
        formPresenter.displayForm(formDescriptor, FormData.empty());
    }

    @Override
    public void requestFocus() {
        formPresenter.requestFocus();
    }

    private void handleFormDataChanged() {
        ValueChangeEvent.fire(this, getValue());
    }

    @Override
    public void setValue(FormDataValue object) {
        clearValue();
        if(!(object instanceof FormData)) {
            return;
        }
        FormData formData = (FormData) object;
        formPresenter.displayForm(formDescriptor, formData);
    }

    @Override
    public void clearValue() {
        formPresenter.clearData();
    }

    @Override
    public Optional<FormDataValue> getValue() {
        return Optional.of(formPresenter.getFormData());
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormDataValue>> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    @Override
    public boolean isDirty() {
        return formPresenter.isDirty();
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
        return true;
    }
}
